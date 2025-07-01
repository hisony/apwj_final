package com.example.ums.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAll() {
        return studentRepository.getAll();
    }

    public Student getStudent(int id) {
        return studentRepository.getStudent(id);
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public void update(Student student) {
        studentRepository.update(student);
    }

    public void delete(int id) {
        studentRepository.delete(id);
    }

    public Student getStudentByCourse(int id) {
        return studentRepository.getStudentWithCourse(id);
    }
}
