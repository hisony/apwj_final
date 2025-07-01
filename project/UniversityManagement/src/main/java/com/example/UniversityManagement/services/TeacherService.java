package com.example.UniversityManagement.services;



import com.example.UniversityManagement.entity.Teacher;
import com.example.UniversityManagement.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public void createTeacher(Teacher teacher) {
        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher name cannot be empty");
        }
        if (teacher.getEmail() == null || teacher.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher email cannot be empty");
        }
        if (teacher.getDepartment() == null || teacher.getDepartment().trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        if (!isValidEmail(teacher.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        teacherRepository.save(teacher);
    }

    public void updateTeacher(Long id, Teacher teacher) {
        Teacher existingTeacher = teacherRepository.findById(id);
        if (existingTeacher == null) {
            throw new IllegalArgumentException("Teacher not found with id: " + id);
        }

        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher name cannot be empty");
        }
        if (teacher.getEmail() == null || teacher.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher email cannot be empty");
        }
        if (teacher.getDepartment() == null || teacher.getDepartment().trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        if (!isValidEmail(teacher.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        teacher.setId(id);
        teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        Teacher existingTeacher = teacherRepository.findById(id);
        if (existingTeacher == null) {
            throw new IllegalArgumentException("Teacher not found with id: " + id);
        }

        teacherRepository.deleteById(id);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
}

