package com.example.UniversityManagement.repository;


import com.example.UniversityManagement.entity.Enrollment;
import com.example.UniversityManagement.entity.Student;
import com.example.UniversityManagement.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Repository
public class EnrollmentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EnrollmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Enrollment> findAll() {
        String sql = "SELECT e.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<Enrollment> enrollments = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            enrollments.add(mapRowToEnrollment(row));
        }
        return enrollments;
    }

    public Enrollment findById(Long id) {
        String sql = "SELECT e.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.id = ?";

        Map<String, Object> row = jdbcTemplate.queryForMap(sql, id);
        return mapRowToEnrollment(row);
    }

    public List<Enrollment> findByStudentId(Long studentId) {
        String sql = "SELECT e.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.student_id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId);
        List<Enrollment> enrollments = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            enrollments.add(mapRowToEnrollment(row));
        }
        return enrollments;
    }

    public List<Enrollment> findByCourseId(Long courseId) {
        String sql = "SELECT e.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM enrollment e " +
                "JOIN student s ON e.student_id = s.id " +
                "JOIN course c ON e.course_id = c.id " +
                "WHERE e.course_id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, courseId);
        List<Enrollment> enrollments = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            enrollments.add(mapRowToEnrollment(row));
        }
        return enrollments;
    }

    public boolean existsByStudentAndCourse(Long studentId, Long courseId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND course_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, studentId, courseId);
        return count > 0;
    }

    public void save(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO enrollment (student_id, course_id, enrollment_date) VALUES (?, ?, ?)",
                    enrollment.getStudent().getId(),
                    enrollment.getCourse().getId(),
                    Date.valueOf(enrollment.getEnrollmentDate())
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE enrollment SET student_id = ?, course_id = ?, enrollment_date = ? WHERE id = ?",
                    enrollment.getStudent().getId(),
                    enrollment.getCourse().getId(),
                    Date.valueOf(enrollment.getEnrollmentDate()),
                    enrollment.getId()
            );
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM enrollment WHERE id = ?", id);
    }

    public void deleteByStudentId(Long studentId) {
        jdbcTemplate.update("DELETE FROM enrollment WHERE student_id = ?", studentId);
    }

    public void deleteByCourseId(Long courseId) {
        jdbcTemplate.update("DELETE FROM enrollment WHERE course_id = ?", courseId);
    }

    private Enrollment mapRowToEnrollment(Map<String, Object> row) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(((Number) row.get("id")).longValue());

        Student student = new Student();
        student.setId(((Number) row.get("student_id")).longValue());
        student.setName((String) row.get("student_name"));
        student.setEmail((String) row.get("student_email"));
        student.setDepartment((String) row.get("student_department"));
        student.setDob(((Date) row.get("student_dob")).toLocalDate());
        enrollment.setStudent(student);

        Course course = new Course();
        course.setId(((Number) row.get("course_id")).longValue());
        course.setName((String) row.get("course_name"));
        course.setCode((String) row.get("course_code"));
        course.setDepartment((String) row.get("course_department"));
        enrollment.setCourse(course);

        enrollment.setEnrollmentDate(((Date) row.get("enrollment_date")).toLocalDate());

        return enrollment;
    }
}


