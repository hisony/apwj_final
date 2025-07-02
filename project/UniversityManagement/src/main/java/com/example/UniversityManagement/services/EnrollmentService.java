package com.example.UniversityManagement.services;



import com.example.UniversityManagement.entity.Enrollment;
import com.example.UniversityManagement.entity.Student;
import com.example.UniversityManagement.entity.Course;
import com.example.UniversityManagement.repository.EnrollmentRepository;
import com.example.UniversityManagement.repository.StudentRepository;
import com.example.UniversityManagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentService(
                EnrollmentRepository enrollmentRepository,
                StudentRepository studentRepository,
                CourseRepository courseRepository) {
            this.enrollmentRepository = enrollmentRepository;
            this.studentRepository = studentRepository;
            this.courseRepository = courseRepository;
    }
    public List<Enrollment> getAllEnrollments() {
            return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
            Enrollment enrollment = enrollmentRepository.findById(id);
            if (enrollment == null) {
                throw new IllegalArgumentException("Enrollment not found with id: " + id);
            }
            return enrollment;
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
            return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
            return enrollmentRepository.findByCourseId(courseId);
    }

    public void createEnrollment(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }

        Course course = courseRepository.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }

        if (enrollmentRepository.existsByStudentAndCourse(studentId, courseId)) {
            throw new IllegalArgumentException(
                    "Student is already enrolled in this course"
            );
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());

        enrollmentRepository.save(enrollment);
    }


    public void updateEnrollment(Long id, Long studentId, Long courseId) {
            Enrollment existingEnrollment = enrollmentRepository.findById(id);
            if (existingEnrollment == null) {
                throw new IllegalArgumentException("Enrollment not found with id: " + id);
            }

            Student student = studentRepository.findById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found with id: " + studentId);
            }

            Course course = courseRepository.findById(courseId);
            if (course == null) {
                throw new IllegalArgumentException("Course not found with id: " + courseId);
            }

            if (!existingEnrollment.getStudent().getId().equals(studentId) ||
                    !existingEnrollment.getCourse().getId().equals(courseId)) {
                if (enrollmentRepository.existsByStudentAndCourse(studentId, courseId)) {
                    throw new IllegalArgumentException(
                            "Student is already enrolled in this course"
                    );
                }
            }

            existingEnrollment.setStudent(student);
            existingEnrollment.setCourse(course);

            enrollmentRepository.save(existingEnrollment);
    }

    public void deleteEnrollment(Long id) {
            Enrollment enrollment = enrollmentRepository.findById(id);
            if (enrollment == null) {
                throw new IllegalArgumentException("Enrollment not found with id: " + id);
            }

            enrollmentRepository.deleteById(id);
    }

    public List<Student> getStudentsByCourse(Long courseId) {
            List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
            return enrollments.stream()
                    .map(Enrollment::getStudent)
                    .collect(Collectors.toList());
    }


    public void deleteEnrollmentsByStudent(Long studentId) {
            Student student = studentRepository.findById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found with id: " + studentId);
            }

            enrollmentRepository.deleteByStudentId(studentId);
    }

    public void deleteEnrollmentsByCourse(Long courseId) {
            // Check if course exists
            Course course = courseRepository.findById(courseId);
            if (course == null) {
                throw new IllegalArgumentException("Course not found with id: " + courseId);
            }

            enrollmentRepository.deleteByCourseId(courseId);
    }

    public void updateEnrollment(Long id, Enrollment enrollment) {
            Enrollment existingEnrollment = enrollmentRepository.findById(id);
            if (existingEnrollment == null) {
                throw new IllegalArgumentException("Enrollment not found with id: " + id);
            }

            Long studentId = enrollment.getStudent().getId();
            Long courseId = enrollment.getCourse().getId();

            Student student = studentRepository.findById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Student not found with id: " + studentId);
            }

            Course course = courseRepository.findById(courseId);
            if (course == null) {
                throw new IllegalArgumentException("Course not found with id: " + courseId);
            }

            if (!existingEnrollment.getStudent().getId().equals(studentId) ||
                    !existingEnrollment.getCourse().getId().equals(courseId)) {
                if (enrollmentRepository.existsByStudentAndCourse(studentId, courseId)) {
                    throw new IllegalArgumentException(
                            "Student is already enrolled in this course"
                    );
                }
            }

            existingEnrollment.setStudent(student);
            existingEnrollment.setCourse(course);

            enrollmentRepository.save(existingEnrollment);
    }
    public boolean checkEnrollment(Long studentId, Long courseId) {
            return enrollmentRepository.existsByStudentAndCourse(studentId, courseId);
    }

    public boolean isStudentEnrolledInCourse(Long studentId, Long courseId) {
            return enrollmentRepository.existsByStudentAndCourse(studentId, courseId);
        }
    }
