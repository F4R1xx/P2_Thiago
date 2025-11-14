package br.edu.ibmec.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.dao.CursoRepository;
import br.edu.ibmec.dao.DisciplinaRepository;
import br.edu.ibmec.dto.DisciplinaDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.exception.DaoException;

/**
 * CRUD básico para a entidade Disciplina.
 * Use este endpoint para criar a disciplina de R$ 650.
 */
@RestController
@RequestMapping("/disciplina")
public class DisciplinaResource {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    public ResponseEntity<?> createDisciplina(@RequestBody DisciplinaDTO dto) {
        try {
            Curso curso = cursoRepository.findById(dto.getCursoId())
                    .orElseThrow(() -> new DaoException("Curso não encontrado"));
            
            Disciplina disciplina = new Disciplina();
            disciplina.setNome(dto.getNome());
            disciplina.setValorDisciplina(dto.getValorDisciplina());
            disciplina.setCurso(curso);
            
            Disciplina savedDisciplina = disciplinaRepository.save(disciplina);
            return ResponseEntity.ok(savedDisciplina);
        } catch (DaoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Disciplina>> getAllDisciplinas() {
        return ResponseEntity.ok(disciplinaRepository.findAll());
    }
}
