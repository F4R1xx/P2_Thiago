package br.edu.ibmec.resource;

import br.edu.ibmec.dao.ProfessorRepository;
import br.edu.ibmec.entity.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")
public class ProfessorResource {

    @Autowired
    private ProfessorRepository professorRepository;

}
