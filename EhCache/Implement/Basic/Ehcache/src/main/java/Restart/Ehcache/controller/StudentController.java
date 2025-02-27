package Restart.Ehcache.controller;


import Restart.Ehcache.model.Student;
import Restart.Ehcache.repo.StudentRepository;
import Restart.Ehcache.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentRepository repo;
    @Autowired
    StudentService service;
    @GetMapping("all")
    public List<Student >  toadd()
    {
        return repo.findAll();
    }

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public Optional<Student> getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @GetMapping("/C")
    public String testCache() {
        return "Cache Value: " + service.getCachedData();
    }
}

