package com.example.UniversityManagement.services;


import com.example.UniversityManagement.entity.Grade;
import com.example.UniversityManagement.entity.Student;
import com.example.UniversityManagement.entity.Course;
import com.example.UniversityManagement.repository.GradeRepository;
import com.example.UniversityManagement.repository.StudentRepository;
import com.example.UniversityManagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public GradeService(
            GradeRepository gradeRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Grade getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id);
        if (grade == null) {
            throw new IllegalArgumentException("Grade not found with id: " + id);
        }
        return grade;
    }

    public List<Grade> getGradesByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }
        return gradeRepository.findByStudentId(studentId);
    }

    public List<Grade> getGradesByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }
        return gradeRepository.findByCourseId(courseId);
    }

    public void createGrade(Long studentId, Long courseId, String grade) {
        // Find the student by id, throw an exception if not found
        Student student = studentRepository.findById(studentId);

        // Find the course by id, throw an exception if not found
        Course course = courseRepository.findById(courseId);


        // Validate the grade
        validateGrade(grade);

        // Create and save the new grade entity
        Grade newGrade = new Grade();
        newGrade.setStudent(student);
        newGrade.setCourse(course);
        newGrade.setGrade(grade);

        gradeRepository.save(newGrade);
    }





    public void deleteGrade(Long id) {
        Grade grade = gradeRepository.findById(id);
        if (grade == null) {
            throw new IllegalArgumentException("Grade not found with id: " + id);
        }

        gradeRepository.deleteById(id);
    }

    public void deleteGradesByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }

        gradeRepository.deleteByStudentId(studentId);
    }

    public void deleteGradesByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }

        gradeRepository.deleteByCourseId(courseId);
    }

    public Double getStudentAverageGrade(Long studentId) {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with id: " + studentId);
        }

        return gradeRepository.getAverageGradeForStudent(studentId);
    }

    public Double getCourseAverageGrade(Long courseId) {
        Course course = courseRepository.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }

        return gradeRepository.getAverageGradeForCourse(courseId);
    }

    private void validateGrade(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            throw new IllegalArgumentException("Grade cannot be empty");
        }
        String validGrades = "ABCDF";
        if (!validGrades.contains(grade.toUpperCase())) {
            throw new IllegalArgumentException("Invalid grade value. Must be one of: A, B, C, D, F");
        }
    }
}
