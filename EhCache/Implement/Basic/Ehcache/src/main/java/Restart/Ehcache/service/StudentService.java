package Restart.Ehcache.service;


import Restart.Ehcache.model.Student;
import Restart.Ehcache.repo.StudentRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Cacheable(value = "studentsCache", key = "#id")
    public Optional<Student> getStudentById(Long id) {
        System.out.println("Fetching student from DB for ID: " + id);
        return studentRepository.findById(id);
    }
int counter;
    @Cacheable("myCache")
    public int getCachedData() {
        return ++counter;  // This should return the same number if caching works
    }
}
