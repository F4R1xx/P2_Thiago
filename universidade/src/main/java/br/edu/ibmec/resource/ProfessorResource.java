package br.edu.ibmec.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.dao.ProfessorRepository;
import br.edu.ibmec.entity.Professor;

/**
 * CRUD básico para a entidade Professor, necessário para
 * popular o banco antes de criar turmas.
 */
@RestController
@RequestMapping("/professor")
public class ProfessorResource {

    @Autowired
    private ProfessorRepository professorRepository;

    @PostMapping
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor) {
        Professor savedProfessor = professorRepository.save(professor);
        return ResponseEntity.ok(savedProfessor);
    }

    @GetMapping
    public ResponseEntity<List<Professor>> getAllProfessores() {
        return ResponseEntity.ok(professorRepository.findAll());
    }
}