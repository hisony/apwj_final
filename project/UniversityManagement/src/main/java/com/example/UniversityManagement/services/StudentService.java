package com.example.UniversityManagement.services;



import com.example.UniversityManagement.entity.Student;
import com.example.UniversityManagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public void createStudent(Student student) {
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Student email cannot be empty");
        }
        if (student.getDob() == null) {
            throw new IllegalArgumentException("Date of birth cannot be empty");
        }
        if (student.getDepartment() == null || student.getDepartment().trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        studentRepository.save(student);
    }

    public void updateStudent(Long id, Student student) {
        Student existingStudent = studentRepository.findById(id);
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }

        student.setId(id);
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Student existingStudent = studentRepository.findById(id);
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
    }
}

