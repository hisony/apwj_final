package com.example.ums.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAll() {
        return courseRepository.getAll();
    }

    public Course getCourse(int id) {
        return courseRepository.getCourse(id);
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public void update(Course course) {
        courseRepository.update(course);
    }

    public void delete(int id) {
        courseRepository.delete(id);
    }
}
