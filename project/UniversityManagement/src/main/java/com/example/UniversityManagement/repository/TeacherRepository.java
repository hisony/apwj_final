package com.example.UniversityManagement.repository;



import com.example.UniversityManagement.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Repository
public class TeacherRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TeacherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Teacher> findAll() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM teacher");
        List<Teacher> teachers = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Teacher teacher = new Teacher();
            teacher.setId(((Number) row.get("id")).longValue());
            teacher.setName((String) row.get("name"));
            teacher.setEmail((String) row.get("email"));
            teacher.setDepartment((String) row.get("department"));
            teachers.add(teacher);
        }
        return teachers;
    }

    public Teacher findById(Long id) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM teacher WHERE id = ?", id);
        Teacher teacher = new Teacher();
        teacher.setId(((Number) row.get("id")).longValue());
        teacher.setName((String) row.get("name"));
        teacher.setEmail((String) row.get("email"));
        teacher.setDepartment((String) row.get("department"));
        return teacher;
    }

    public void save(Teacher teacher) {
        if (teacher.getId() == null) {
            jdbcTemplate.update(
                    "INSERT INTO teacher (name, email, department) VALUES (?, ?, ?)",
                    teacher.getName(),
                    teacher.getEmail(),
                    teacher.getDepartment()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE teacher SET name = ?, email = ?, department = ? WHERE id = ?",
                    teacher.getName(),
                    teacher.getEmail(),
                    teacher.getDepartment(),
                    teacher.getId()
            );
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM teacher WHERE id = ?", id);
    }
}

