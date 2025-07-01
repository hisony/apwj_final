package com.example.UniversityManagement.repository;


import com.example.UniversityManagement.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.sql.Date;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> findAll() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM student");
        List<Student> students = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Student student = new Student();
            student.setId(((Number) row.get("id")).longValue());
            student.setName((String) row.get("name"));
            student.setEmail((String) row.get("email"));
            // Convert java.sql.Date to java.time.LocalDate
            Date sqlDate = (Date) row.get("dob");
            student.setDob(sqlDate != null ? sqlDate.toLocalDate() : null);
            student.setDepartment((String) row.get("department"));
            students.add(student);
        }
        return students;
    }

    public Student findById(Long id) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM student WHERE id = ?", id);
        Student student = new Student();
        student.setId(((Number) row.get("id")).longValue());
        student.setName((String) row.get("name"));
        student.setEmail((String) row.get("email"));
        // Convert java.sql.Date to java.time.LocalDate
        Date sqlDate = (Date) row.get("dob");
        student.setDob(sqlDate != null ? sqlDate.toLocalDate() : null);
        student.setDepartment((String) row.get("department"));
        return student;
    }

    public void save(Student student) {
        if (student.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO student (name, email, dob, department) VALUES (?, ?, ?, ?)",
                    student.getName(),
                    student.getEmail(),
                    student.getDob() != null ? Date.valueOf(student.getDob()) : null,
                    student.getDepartment()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE student SET name = ?, email = ?, dob = ?, department = ? WHERE id = ?",
                    student.getName(),
                    student.getEmail(),
                    student.getDob() != null ? Date.valueOf(student.getDob()) : null,
                    student.getDepartment(),
                    student.getId()
            );
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM student WHERE id = ?", id);
    }
}

