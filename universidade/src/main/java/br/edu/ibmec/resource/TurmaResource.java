package br.edu.ibmec.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.dao.DisciplinaRepository;
import br.edu.ibmec.dao.ProfessorRepository;
import br.edu.ibmec.dao.TurmaRepository;
import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.entity.Professor;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.exception.DaoException;

/**
 * CRUD básico para a entidade Turma.
 */
@RestController
@RequestMapping("/turma")
public class TurmaResource {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @PostMapping
    public ResponseEntity<?> createTurma(@RequestBody TurmaDTO dto) {
        try {
            Disciplina disciplina = disciplinaRepository.findById(dto.getDisciplinaId())
                    .orElseThrow(() -> new DaoException("Disciplina não encontrada"));
            
            Professor professor = professorRepository.findById(dto.getProfessorId())
                    .orElseThrow(() -> new DaoException("Professor não encontrado"));

            Turma turma = new Turma();
            turma.setDisciplina(disciplina);
            turma.setProfessor(professor);
            turma.setCapacidade(dto.getCapacidade());

            Turma savedTurma = turmaRepository.save(turma);
            return ResponseEntity.ok(savedTurma);

        } catch (DaoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Turma>> getAllTurmas() {
        return ResponseEntity.ok(turmaRepository.findAll());
    }
}
