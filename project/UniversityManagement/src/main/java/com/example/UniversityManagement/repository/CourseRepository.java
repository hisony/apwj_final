package com.example.UniversityManagement.repository;


import com.example.UniversityManagement.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Repository
public class CourseRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Course> findAll() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM course");
        List<Course> courses = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Course course = new Course();
            course.setId(((Number) row.get("id")).longValue());
            course.setName((String) row.get("name"));
            course.setCode((String) row.get("code"));
            course.setDepartment((String) row.get("department"));
            courses.add(course);
        }
        return courses;
    }

    public Course findById(Long id) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM course WHERE id = ?", id);
        Course course = new Course();
        course.setId(((Number) row.get("id")).longValue());
        course.setName((String) row.get("name"));
        course.setCode((String) row.get("code"));
        course.setDepartment((String) row.get("department"));
        return course;
    }

    public void save(Course course) {
        if (course.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO course (name, code, department) VALUES (?, ?, ?)",
                    course.getName(),
                    course.getCode(),
                    course.getDepartment()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE course SET name = ?, code = ?, department = ? WHERE id = ?",
                    course.getName(),
                    course.getCode(),
                    course.getDepartment(),
                    course.getId()
            );
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM course WHERE id = ?", id);
    }

    public Course findByCode(String code) {
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM course WHERE code = ?", code);
            Course course = new Course();
            course.setId(((Number) row.get("id")).longValue());
            course.setName((String) row.get("name"));
            course.setCode((String) row.get("code"));
            course.setDepartment((String) row.get("department"));
            return course;
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Course> findByDepartment(String department) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                "SELECT * FROM course WHERE department = ?",
                department
        );
        List<Course> courses = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Course course = new Course();
            course.setId(((Number) row.get("id")).longValue());
            course.setName((String) row.get("name"));
            course.setCode((String) row.get("code"));
            course.setDepartment((String) row.get("department"));
            courses.add(course);
        }
        return courses;
    }
}

