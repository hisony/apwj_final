package com.example.UniversityManagement.repository;



import com.example.UniversityManagement.entity.Grade;
import com.example.UniversityManagement.entity.Student;
import com.example.UniversityManagement.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Repository
public class GradeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Grade> findAll() {
        String sql = "SELECT g.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM grade g " +
                "JOIN student s ON g.student_id = s.id " +
                "JOIN course c ON g.course_id = c.id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<Grade> grades = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            grades.add(mapRowToGrade(row));
        }
        return grades;
    }

    public Grade findById(Long id) {
        String sql = "SELECT g.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM grade g " +
                "JOIN student s ON g.student_id = s.id " +
                "JOIN course c ON g.course_id = c.id " +
                "WHERE g.id = ?";

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, id);
            return mapRowToGrade(row);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Grade> findByStudentId(Long studentId) {
        String sql = "SELECT g.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM grade g " +
                "JOIN student s ON g.student_id = s.id " +
                "JOIN course c ON g.course_id = c.id " +
                "WHERE g.student_id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId);
        List<Grade> grades = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            grades.add(mapRowToGrade(row));
        }
        return grades;
    }

    public List<Grade> findByCourseId(Long courseId) {
        String sql = "SELECT g.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM grade g " +
                "JOIN student s ON g.student_id = s.id " +
                "JOIN course c ON g.course_id = c.id " +
                "WHERE g.course_id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, courseId);
        List<Grade> grades = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            grades.add(mapRowToGrade(row));
        }
        return grades;
    }

    public void save(Grade grade) {
        if (grade.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO grade (student_id, course_id, grade) VALUES (?, ?, ?)",
                    grade.getStudent().getId(),
                    grade.getCourse().getId(),
                    grade.getGrade()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE grade SET student_id = ?, course_id = ?, grade = ? WHERE id = ?",
                    grade.getStudent().getId(),
                    grade.getCourse().getId(),
                    grade.getGrade(),
                    grade.getId()
            );
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM grade WHERE id = ?", id);
    }

    public void deleteByStudentId(Long studentId) {
        jdbcTemplate.update("DELETE FROM grade WHERE student_id = ?", studentId);
    }

    public void deleteByCourseId(Long courseId) {
        jdbcTemplate.update("DELETE FROM grade WHERE course_id = ?", courseId);
    }

    public Double getAverageGradeForStudent(Long studentId) {
        String sql = "SELECT AVG(CAST(grade AS DECIMAL(4,2))) FROM grade WHERE student_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, studentId);
    }

    public Double getAverageGradeForCourse(Long courseId) {
        String sql = "SELECT AVG(CAST(grade AS DECIMAL(4,2))) FROM grade WHERE course_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, courseId);
    }

    private Grade mapRowToGrade(Map<String, Object> row) {
        Grade grade = new Grade();
        grade.setId(((Number) row.get("id")).longValue());

        Student student = new Student();
        student.setId(((Number) row.get("student_id")).longValue());
        student.setName((String) row.get("student_name"));
        student.setEmail((String) row.get("student_email"));
        student.setDepartment((String) row.get("student_department"));
        student.setDob(((Date) row.get("student_dob")).toLocalDate());
        grade.setStudent(student);

        Course course = new Course();
        course.setId(((Number) row.get("course_id")).longValue());
        course.setName((String) row.get("course_name"));
        course.setCode((String) row.get("course_code"));
        course.setDepartment((String) row.get("course_department"));
        grade.setCourse(course);

        grade.setGrade((String) row.get("grade"));

        return grade;
    }
}
