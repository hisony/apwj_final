package com.example.UniversityManagement.api;



import com.example.UniversityManagement.entity.Attendance;
import com.example.UniversityManagement.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceApi {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceApi(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(attendanceService.getAttendanceById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        try {
            return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Attendance>> getAttendanceByCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(attendanceService.getAttendanceByCourse(courseId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(attendanceService.getAttendanceByStudentAndCourse(studentId, courseId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@Valid @RequestBody Attendance attendance) {
        try {
            attendanceService.markAttendance(
                    attendance.getStudent().getId(),
                    attendance.getCourse().getId(),
                    attendance.getDate(),
                    attendance.isPresent()
            );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/mark/bulk")
    public ResponseEntity<?> markBulkAttendance(@RequestBody Map<String, Object> request) {
        try {
            Long courseId = Long.valueOf(request.get("courseId").toString());
            LocalDate date = LocalDate.parse(request.get("date").toString());
            @SuppressWarnings("unchecked")
            List<Long> presentStudentIds = (List<Long>) request.get("presentStudentIds");

            attendanceService.markBulkAttendance(courseId, date, presentStudentIds);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException | ClassCastException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/percentage")
    public ResponseEntity<?> getAttendancePercentage(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        try {
            double percentage = attendanceService.getAttendancePercentage(studentId, courseId);
            return ResponseEntity.ok(Map.of("percentage", percentage));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long id) {
        try {
            attendanceService.deleteAttendance(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<?> deleteAttendanceByStudent(@PathVariable Long studentId) {
        try {
            attendanceService.deleteAttendanceByStudent(studentId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<?> deleteAttendanceByCourse(@PathVariable Long courseId) {
        try {
            attendanceService.deleteAttendanceByCourse(courseId);
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
