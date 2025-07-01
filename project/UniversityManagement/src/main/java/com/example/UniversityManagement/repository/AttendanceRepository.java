package com.example.UniversityManagement.repository;



import com.example.UniversityManagement.entity.Attendance;
import com.example.UniversityManagement.entity.Student;
import com.example.UniversityManagement.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AttendanceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AttendanceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Attendance> findAll() {
        String sql = "SELECT a.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM attendance a " +
                "JOIN student s ON a.student_id = s.id " +
                "JOIN course c ON a.course_id = c.id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<Attendance> attendances = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            attendances.add(mapRowToAttendance(row));
        }
        return attendances;
    }

    public Attendance findById(Long id) {
        String sql = "SELECT a.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM attendance a " +
                "JOIN student s ON a.student_id = s.id " +
                "JOIN course c ON a.course_id = c.id " +
                "WHERE a.id = ?";

        Map<String, Object> row = jdbcTemplate.queryForMap(sql, id);
        return mapRowToAttendance(row);
    }

    public List<Attendance> findByStudentId(Long studentId) {
        String sql = "SELECT a.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM attendance a " +
                "JOIN student s ON a.student_id = s.id " +
                "JOIN course c ON a.course_id = c.id " +
                "WHERE a.student_id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId);
        List<Attendance> attendances = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            attendances.add(mapRowToAttendance(row));
        }
        return attendances;
    }

    public List<Attendance> findByCourseId(Long courseId) {
        String sql = "SELECT a.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM attendance a " +
                "JOIN student s ON a.student_id = s.id " +
                "JOIN course c ON a.course_id = c.id " +
                "WHERE a.course_id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, courseId);
        List<Attendance> attendances = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            attendances.add(mapRowToAttendance(row));
        }
        return attendances;
    }

    public List<Attendance> findByDate(LocalDate date) {
        String sql = "SELECT a.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM attendance a " +
                "JOIN student s ON a.student_id = s.id " +
                "JOIN course c ON a.course_id = c.id " +
                "WHERE a.date = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, Date.valueOf(date));
        List<Attendance> attendances = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            attendances.add(mapRowToAttendance(row));
        }
        return attendances;
    }

    public List<Attendance> findByStudentIdAndCourseId(Long studentId, Long courseId) {
        String sql = "SELECT a.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM attendance a " +
                "JOIN student s ON a.student_id = s.id " +
                "JOIN course c ON a.course_id = c.id " +
                "WHERE a.student_id = ? AND a.course_id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentId, courseId);
        List<Attendance> attendances = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            attendances.add(mapRowToAttendance(row));
        }
        return attendances;
    }

    public Attendance findByStudentIdAndCourseIdAndDate(Long studentId, Long courseId, LocalDate date) {
        String sql = "SELECT a.*, s.name as student_name, s.email as student_email, " +
                "s.department as student_department, s.dob as student_dob, " +
                "c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM attendance a " +
                "JOIN student s ON a.student_id = s.id " +
                "JOIN course c ON a.course_id = c.id " +
                "WHERE a.student_id = ? AND a.course_id = ? AND a.date = ?";

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, studentId, courseId, Date.valueOf(date));
            return mapRowToAttendance(row);
        } catch (Exception e) {
            return null;
        }
    }

    public void save(Attendance attendance) {
        if (attendance.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO attendance (student_id, course_id, date, present) VALUES (?, ?, ?, ?)",
                    attendance.getStudent().getId(),
                    attendance.getCourse().getId(),
                    Date.valueOf(attendance.getDate()),
                    attendance.isPresent()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE attendance SET student_id = ?, course_id = ?, date = ?, present = ? WHERE id = ?",
                    attendance.getStudent().getId(),
                    attendance.getCourse().getId(),
                    Date.valueOf(attendance.getDate()),
                    attendance.isPresent(),
                    attendance.getId()
            );
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM attendance WHERE id = ?", id);
    }

    public void deleteByStudentId(Long studentId) {
        jdbcTemplate.update("DELETE FROM attendance WHERE student_id = ?", studentId);
    }

    public void deleteByCourseId(Long courseId) {
        jdbcTemplate.update("DELETE FROM attendance WHERE course_id = ?", courseId);
    }

    private Attendance mapRowToAttendance(Map<String, Object> row) {
        Attendance attendance = new Attendance();
        attendance.setId(((Number) row.get("id")).longValue());

        Student student = new Student();
        student.setId(((Number) row.get("student_id")).longValue());
        student.setName((String) row.get("student_name"));
        student.setEmail((String) row.get("student_email"));
        student.setDepartment((String) row.get("student_department"));
        student.setDob(((Date) row.get("student_dob")).toLocalDate());
        attendance.setStudent(student);

        Course course = new Course();
        course.setId(((Number) row.get("course_id")).longValue());
        course.setName((String) row.get("course_name"));
        course.setCode((String) row.get("course_code"));
        course.setDepartment((String) row.get("course_department"));
        attendance.setCourse(course);

        attendance.setDate(((Date) row.get("date")).toLocalDate());
        attendance.setPresent((Boolean) row.get("present"));

        return attendance;
    }
}
