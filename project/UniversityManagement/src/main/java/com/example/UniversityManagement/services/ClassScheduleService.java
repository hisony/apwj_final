package com.example.UniversityManagement.services;



import com.example.UniversityManagement.entity.ClassSchedule;
import com.example.UniversityManagement.entity.Course;
import com.example.UniversityManagement.repository.ClassScheduleRepository;
import com.example.UniversityManagement.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClassScheduleService {

    private final ClassScheduleRepository classScheduleRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ClassScheduleService(
            ClassScheduleRepository classScheduleRepository,
            CourseRepository courseRepository) {
        this.classScheduleRepository = classScheduleRepository;
        this.courseRepository = courseRepository;
    }

    public List<ClassSchedule> getAllSchedules() {
        return classScheduleRepository.findAll();
    }

    public ClassSchedule getScheduleById(Long id) {
        ClassSchedule schedule = classScheduleRepository.findById(id);
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + id);
        }
        return schedule;
    }

    public List<ClassSchedule> getSchedulesByCourse(Long courseId) {
        validateCourse(courseId);
        return classScheduleRepository.findByCourseId(courseId);
    }

    public List<ClassSchedule> getSchedulesByDayOfWeek(String dayOfWeek) {
        validateDayOfWeek(dayOfWeek);
        return classScheduleRepository.findByDayOfWeek(dayOfWeek.toUpperCase());
    }

    public List<ClassSchedule> getSchedulesByRoom(String room) {
        if (room == null || room.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be empty");
        }
        return classScheduleRepository.findByRoom(room);
    }

    public List<ClassSchedule> getSchedulesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        validateTimeRange(startTime, endTime);
        return classScheduleRepository.findByTimeRange(startTime, endTime);
    }

    public void createSchedule(ClassSchedule schedule) {
        validateSchedule(schedule);
        validateTimeConflicts(schedule);
        classScheduleRepository.save(schedule);
    }

    public void updateSchedule(Long id, ClassSchedule schedule) {
        if (!id.equals(schedule.getId())) {
            throw new IllegalArgumentException("Schedule ID mismatch");
        }

        ClassSchedule existingSchedule = getScheduleById(id);
        validateSchedule(schedule);
        validateTimeConflicts(schedule);

        classScheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        if (classScheduleRepository.findById(id) == null) {
            throw new IllegalArgumentException("Schedule not found with id: " + id);
        }
        classScheduleRepository.deleteById(id);
    }

    public void deleteSchedulesByCourse(Long courseId) {
        validateCourse(courseId);
        classScheduleRepository.deleteByCourseId(courseId);
    }

    private void validateSchedule(ClassSchedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null");
        }

        if (schedule.getCourse() == null || schedule.getCourse().getId() == null) {
            throw new IllegalArgumentException("Course is required");
        }

        validateCourse(schedule.getCourse().getId());
        validateDayOfWeek(schedule.getDayOfWeek());
        validateTimeRange(schedule.getStartTime(), schedule.getEndTime());

        if (schedule.getRoom() == null || schedule.getRoom().trim().isEmpty()) {
            throw new IllegalArgumentException("Room number is required");
        }
    }

    private void validateCourse(Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID cannot be null");
        }
        Course course = courseRepository.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with id: " + courseId);
        }
    }

    private void validateDayOfWeek(String dayOfWeek) {
        if (dayOfWeek == null || dayOfWeek.trim().isEmpty()) {
            throw new IllegalArgumentException("Day of week cannot be empty");
        }

        String upperDay = dayOfWeek.toUpperCase();
        List<String> validDays = List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY");
        if (!validDays.contains(upperDay)) {
            throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        }
    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }

        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    private void validateTimeConflicts(ClassSchedule schedule) {
        List<ClassSchedule> existingSchedules = classScheduleRepository.findByRoom(schedule.getRoom());

        for (ClassSchedule existing : existingSchedules) {
            if (schedule.getId() != null && schedule.getId().equals(existing.getId())) {
                continue;
            }

            if (!existing.getDayOfWeek().equals(schedule.getDayOfWeek())) {
                continue;
            }

            boolean overlaps = !(schedule.getEndTime().isBefore(existing.getStartTime()) ||
                    schedule.getStartTime().isAfter(existing.getEndTime()));

            if (overlaps) {
                throw new IllegalArgumentException(
                        String.format("Time conflict with existing schedule in room %s from %s to %s",
                                existing.getRoom(),
                                existing.getStartTime(),
                                existing.getEndTime())
                );
            }
        }
    }
}

