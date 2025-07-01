package com.example.UniversityManagement.services;


import com.example.UniversityManagement.entity.Course;
import com.example.UniversityManagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        Course course = courseRepository.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with id: " + id);
        }
        return course;
    }

    public Course getCourseByCode(String code) {
        Course course = courseRepository.findByCode(code);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with code: " + code);
        }
        return course;
    }

    public List<Course> getCoursesByDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
        return courseRepository.findByDepartment(department);
    }

    public void createCourse(Course course) {
        validateCourse(course);

        Course existingCourse = courseRepository.findByCode(course.getCode());
        if (existingCourse != null) {
            throw new IllegalArgumentException("Course with code " + course.getCode() + " already exists");
        }

        courseRepository.save(course);
    }

    public void updateCourse(Long id, Course course) {
        Course existingCourse = courseRepository.findById(id);
        if (existingCourse == null) {
            throw new IllegalArgumentException("Course not found with id: " + id);
        }

        validateCourse(course);

        Course courseWithCode = courseRepository.findByCode(course.getCode());
        if (courseWithCode != null && !courseWithCode.getId().equals(id)) {
            throw new IllegalArgumentException("Course code " + course.getCode() + " is already in use");
        }

        course.setId(id);
        courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course existingCourse = courseRepository.findById(id);
        if (existingCourse == null) {
            throw new IllegalArgumentException("Course not found with id: " + id);
        }

        courseRepository.deleteById(id);
    }

    private void validateCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        if (course.getName() == null || course.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be empty");
        }

        if (course.getCode() == null || course.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }

        if (course.getDepartment() == null || course.getDepartment().trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        if (!isValidCourseCode(course.getCode())) {
            throw new IllegalArgumentException("Invalid course code format");
        }
    }

    private boolean isValidCourseCode(String code) {
        String courseCodeRegex = "^[A-Z]{2,4}\\d{3,4}$";
        return code.matches(courseCodeRegex);
    }
}


