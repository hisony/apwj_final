package com.example.UniversityManagement.services;



import com.example.UniversityManagement.entity.Attendance;
import com.example.UniversityManagement.entity.Student;
import com.example.UniversityManagement.entity.Course;
import com.example.UniversityManagement.repository.AttendanceRepository;
import com.example.UniversityManagement.repository.StudentRepository;
import com.example.UniversityManagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;

    @Autowired
    public AttendanceService(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            EnrollmentService enrollmentService) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentService = enrollmentService;
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    public Attendance getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id);
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance record not found with id: " + id);
        }
        return attendance;
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        validateStudent(studentId);
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceByCourse(Long courseId) {
        validateCourse(courseId);
        return attendanceRepository.findByCourseId(courseId);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public List<Attendance> getAttendanceByStudentAndCourse(Long studentId, Long courseId) {
        validateStudentCourseEnrollment(studentId, courseId);
        return attendanceRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    public void markAttendance(Long studentId, Long courseId, LocalDate date, boolean present) {
        validateStudentCourseEnrollment(studentId, courseId);

        Attendance existingAttendance = attendanceRepository
                .findByStudentIdAndCourseIdAndDate(studentId, courseId, date);

        if (existingAttendance != null) {
            existingAttendance.setPresent(present);
            attendanceRepository.save(existingAttendance);
        } else {
            Attendance attendance = new Attendance();
            attendance.setStudent(studentRepository.findById(studentId));
            attendance.setCourse(courseRepository.findById(courseId));
            attendance.setDate(date);
            attendance.setPresent(present);
            attendanceRepository.save(attendance);
        }
    }

    public void markBulkAttendance(Long courseId, LocalDate date, List<Long> presentStudentIds) {
        validateCourse(courseId);

        // Get all enrolled students for the course
        List<Student> enrolledStudents = enrollmentService.getStudentsByCourse(courseId);

        for (Student student : enrolledStudents) {
            boolean isPresent = presentStudentIds.contains(student.getId());
            markAttendance(student.getId(), courseId, date, isPresent);
        }
    }

    public double getAttendancePercentage(Long studentId, Long courseId) {
        validateStudentCourseEnrollment(studentId, courseId);

        List<Attendance> attendanceRecords = attendanceRepository
                .findByStudentIdAndCourseId(studentId, courseId);

        if (attendanceRecords.isEmpty()) {
            return 0.0;
        }

        long presentCount = attendanceRecords.stream()
                .filter(Attendance::isPresent)
                .count();

        return (double) presentCount / attendanceRecords.size() * 100;
    }

    public void deleteAttendance(Long id) {
        if (attendanceRepository.findById(id) == null) {
            throw new IllegalArgumentException("Attendance record not found with id: " + id);
        }
        attendanceRepository.deleteById(id);
    }

    public void deleteAttendanceByStudent(Long studentId) {
        validateStudent(studentId);
        attendanceRepository.deleteByStudentId(studentId);
    }

    public void deleteAttendanceByCourse(Long courseId) {
        validateCourse(courseId);
        attendanceRepository.deleteByCourseId(courseId);
    }

    private void validateStudent(Long studentId) {
        if (studentRepository.findById(studentId) == null) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }
    }

    private void validateCourse(Long courseId) {
        if (courseRepository.findById(courseId) == null) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }
    }

    private void validateStudentCourseEnrollment(Long studentId, Long courseId) {
        validateStudent(studentId);
        validateCourse(courseId);

        if (!enrollmentService.isStudentEnrolledInCourse(studentId, courseId)) {
            throw new IllegalArgumentException(
                    "Student with id " + studentId + " is not enrolled in course with id " + courseId
            );
        }
    }
}

