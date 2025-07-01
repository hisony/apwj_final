package com.example.UniversityManagement.repository;



import com.example.UniversityManagement.entity.ClassSchedule;
import com.example.UniversityManagement.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ClassScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClassScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ClassSchedule> findAll() {
        String sql = "SELECT cs.*, c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM class_schedule cs " +
                "JOIN course c ON cs.course_id = c.id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<ClassSchedule> schedules = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            schedules.add(mapRowToClassSchedule(row));
        }
        return schedules;
    }

    public ClassSchedule findById(Long id) {
        String sql = "SELECT cs.*, c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM class_schedule cs " +
                "JOIN course c ON cs.course_id = c.id " +
                "WHERE cs.id = ?";

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, id);
            return mapRowToClassSchedule(row);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ClassSchedule> findByCourseId(Long courseId) {
        String sql = "SELECT cs.*, c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM class_schedule cs " +
                "JOIN course c ON cs.course_id = c.id " +
                "WHERE cs.course_id = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, courseId);
        List<ClassSchedule> schedules = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            schedules.add(mapRowToClassSchedule(row));
        }
        return schedules;
    }

    public List<ClassSchedule> findByDayOfWeek(String dayOfWeek) {
        String sql = "SELECT cs.*, c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM class_schedule cs " +
                "JOIN course c ON cs.course_id = c.id " +
                "WHERE cs.day_of_week = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, dayOfWeek);
        List<ClassSchedule> schedules = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            schedules.add(mapRowToClassSchedule(row));
        }
        return schedules;
    }

    public List<ClassSchedule> findByRoom(String room) {
        String sql = "SELECT cs.*, c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM class_schedule cs " +
                "JOIN course c ON cs.course_id = c.id " +
                "WHERE cs.room = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, room);
        List<ClassSchedule> schedules = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            schedules.add(mapRowToClassSchedule(row));
        }
        return schedules;
    }

    public List<ClassSchedule> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT cs.*, c.name as course_name, c.code as course_code, " +
                "c.department as course_department " +
                "FROM class_schedule cs " +
                "JOIN course c ON cs.course_id = c.id " +
                "WHERE (cs.start_time BETWEEN ? AND ?) OR (cs.end_time BETWEEN ? AND ?)";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,
                Timestamp.valueOf(startTime),
                Timestamp.valueOf(endTime),
                Timestamp.valueOf(startTime),
                Timestamp.valueOf(endTime)
        );

        List<ClassSchedule> schedules = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            schedules.add(mapRowToClassSchedule(row));
        }
        return schedules;
    }

    public void save(ClassSchedule schedule) {
        if (schedule.getId() == null) {
            String sql = "INSERT INTO class_schedule (course_id, day_of_week, start_time, end_time, room) " +
                    "VALUES (?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql,
                    schedule.getCourse().getId(),
                    schedule.getDayOfWeek(),
                    Timestamp.valueOf(schedule.getStartTime()),
                    Timestamp.valueOf(schedule.getEndTime()),
                    schedule.getRoom()
            );
        } else {
            String sql = "UPDATE class_schedule SET course_id = ?, day_of_week = ?, " +
                    "start_time = ?, end_time = ?, room = ? WHERE id = ?";

            jdbcTemplate.update(sql,
                    schedule.getCourse().getId(),
                    schedule.getDayOfWeek(),
                    Timestamp.valueOf(schedule.getStartTime()),
                    Timestamp.valueOf(schedule.getEndTime()),
                    schedule.getRoom(),
                    schedule.getId()
            );
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM class_schedule WHERE id = ?", id);
    }

    public void deleteByCourseId(Long courseId) {
        jdbcTemplate.update("DELETE FROM class_schedule WHERE course_id = ?", courseId);
    }



    private ClassSchedule mapRowToClassSchedule(Map<String, Object> row) {
        ClassSchedule schedule = new ClassSchedule();
        schedule.setId(((Number) row.get("id")).longValue());
        Course course = new Course();
        course.setId(((Number) row.get("course_id")).longValue());
        course.setName((String) row.get("course_name"));
        course.setCode((String) row.get("course_code"));
        course.setDepartment((String) row.get("course_department"));
        schedule.setCourse(course);
        schedule.setDayOfWeek((String) row.get("day_of_week"));
        Object startTimeObj = row.get("start_time");
        if (startTimeObj instanceof Timestamp) {
            schedule.setStartTime(((Timestamp) startTimeObj).toLocalDateTime());
        } else if (startTimeObj instanceof LocalDateTime) {
            schedule.setStartTime((LocalDateTime) startTimeObj);
        } else {
            schedule.setStartTime(null);
        }
        Object endTimeObj = row.get("end_time");
        if (endTimeObj instanceof Timestamp) {
            schedule.setEndTime(((Timestamp) endTimeObj).toLocalDateTime());
        } else if (endTimeObj instanceof LocalDateTime) {
            schedule.setEndTime((LocalDateTime) endTimeObj);
        } else {
            schedule.setEndTime(null);
        }
        schedule.setRoom((String) row.get("room"));
        return schedule;
    }
}

