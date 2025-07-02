package com.example.UniversityManagement.api;


import com.example.UniversityManagement.entity.ClassSchedule;
import com.example.UniversityManagement.services.ClassScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ClassScheduleApi {

    private final ClassScheduleService classScheduleService;

    @Autowired
    public ClassScheduleApi(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    @GetMapping
    public ResponseEntity<List<ClassSchedule>> getAllSchedules() {
        return ResponseEntity.ok(classScheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassSchedule> getScheduleById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(classScheduleService.getScheduleById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ClassSchedule>> getSchedulesByCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(classScheduleService.getSchedulesByCourse(courseId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/day/{dayOfWeek}")
    public ResponseEntity<?> getSchedulesByDayOfWeek(@PathVariable String dayOfWeek) {
        try {
            return ResponseEntity.ok(classScheduleService.getSchedulesByDayOfWeek(dayOfWeek));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/room/{room}")
    public ResponseEntity<?> getSchedulesByRoom(@PathVariable String room) {
        try {
            return ResponseEntity.ok(classScheduleService.getSchedulesByRoom(room));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

//    @GetMapping("/timerange")
//    public ResponseEntity<?> getSchedulesByTimeRange(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
//        try {
//            return ResponseEntity.ok(classScheduleService.getSchedulesByTimeRange(startTime, endTime));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new ErrorResponse(e.getMessage()));
//        }
//    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@Valid @RequestBody ClassSchedule schedule) {
        try {
            classScheduleService.createSchedule(schedule);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ClassSchedule schedule) {
        try {
            schedule.setId(id);
            classScheduleService.updateSchedule(id, schedule);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        try {
            classScheduleService.deleteSchedule(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<?> deleteSchedulesByCourse(@PathVariable Long courseId) {
        try {
            classScheduleService.deleteSchedulesByCourse(courseId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}


