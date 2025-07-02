package com.example.UniversityManagement.api;



import com.example.UniversityManagement.entity.Grade;
import com.example.UniversityManagement.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeApi {

    private final GradeService gradeService;

    @Autowired
    public GradeApi(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    // Get all grades
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return new ResponseEntity<>(grades, HttpStatus.OK);
    }

    // Get a grade by ID
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable("id") Long id) {
        try {
            Grade grade = gradeService.getGradeById(id);
            return new ResponseEntity<>(grade, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get grades by student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudent(@PathVariable("studentId") Long studentId) {
        try {
            List<Grade> grades = gradeService.getGradesByStudent(studentId);
            return new ResponseEntity<>(grades, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Get grades by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourse(@PathVariable("courseId") Long courseId) {
        try {
            List<Grade> grades = gradeService.getGradesByCourse(courseId);
            return new ResponseEntity<>(grades, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Create a new grade

    // Create a new grade
    //http://localhost:8080/api/grades/addGrade/4/2?grade=A
    @PostMapping("/addGrade/{studentId}/{courseId}")
    public ResponseEntity<String> createGrade(@PathVariable("studentId") Long studentId,
                                              @PathVariable("courseId") Long courseId,
                                              @RequestParam String grade) {
        try {
            gradeService.createGrade(studentId, courseId, grade);
            return new ResponseEntity<>("Grade created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    // Update an existing grade
//    @PutMapping("/updateGrade/{studentId}/{courseId}")
//    public ResponseEntity<String> updateGrade(@PathVariable("studentId") Long studentId,
//                                              @PathVariable("courseId") Long courseId,
//                                              @RequestParam String grade) {
//        try {
//            gradeService.createGrade(studentId, courseId, grade);
//            return new ResponseEntity<>("Grade updated successfully", HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }


    // Delete a grade by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGrade(@PathVariable("id") Long id) {
        try {
            gradeService.deleteGrade(id);
            return new ResponseEntity<>("Grade deleted successfully", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete grades by student ID
    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<String> deleteGradesByStudent(@PathVariable("studentId") Long studentId) {
        try {
            gradeService.deleteGradesByStudent(studentId);
            return new ResponseEntity<>("Grades for student deleted successfully", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete grades by course ID
    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<String> deleteGradesByCourse(@PathVariable("courseId") Long courseId) {
        try {
            gradeService.deleteGradesByCourse(courseId);
            return new ResponseEntity<>("Grades for course deleted successfully", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get average grade for a student
//    @GetMapping("/student/{studentId}/average")
//    public ResponseEntity<Double> getStudentAverageGrade(@PathVariable("studentId") Long studentId) {
//        try {
//            Double average = gradeService.getStudentAverageGrade(studentId);
//            return new ResponseEntity<>(average, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }

    // Get average grade for a course
//    @GetMapping("/course/{courseId}/average")
//    public ResponseEntity<Double> getCourseAverageGrade(@PathVariable("courseId") Long courseId) {
//        try {
//            Double average = gradeService.getCourseAverageGrade(courseId);
//            return new ResponseEntity<>(average, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }
}
