package br.edu.ibmec.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ibmec.service.AlunoService;
import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

@RestController
@RequestMapping("/aluno")
public class AlunoResource {

    private AlunoService alunoService;

    @Autowired
    public AlunoResource(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping(value = "/{matricula}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AlunoDTO> buscarAluno(@PathVariable String matricula) {
        try {
            AlunoDTO alunoDTO = alunoService.buscarAluno(Integer.parseInt(matricula));
            return ResponseEntity.ok(alunoDTO);
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> cadastrarAluno(@RequestBody AlunoDTO alunoDTO)
            throws ServiceException, DaoException {
        try {
            alunoService.cadastrarAluno(alunoDTO);
            URI location = URI.create("" + alunoDTO.getMatricula());
            return ResponseEntity.created(location).build();
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO) {
                return ResponseEntity.badRequest()
                        .header("Motivo", "Código inválido")
                        .build();
            }
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO) {
                return ResponseEntity.badRequest()
                        .header("Motivo", "Nome inválido")
                        .build();
            } else {
                return ResponseEntity.badRequest()
                        .header("Motivo", e.getMessage())
                        .build();
            }
        } catch (DaoException e) {
            return ResponseEntity.badRequest()
                    .header("Motivo", "Erro no banco de dados")
                    .build();
        }
    }

    @PutMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> alterarAluno(@RequestBody AlunoDTO alunoDTO)
            throws ServiceException, DaoException {
        try {
            alunoService.alterarAluno(alunoDTO);
            URI location = URI.create("" + alunoDTO.getMatricula());
            return ResponseEntity.created(location).build();
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO) {
                return ResponseEntity.badRequest()
                        .header("Motivo", "Código inválido")
                        .build();
            }
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO) {
                return ResponseEntity.badRequest()
                        .header("Motivo", "Nome inválido")
                        .build();
            } else {
                return ResponseEntity.badRequest()
                        .header("Motivo", e.getMessage())
                        .build();
            }
        } catch (DaoException e) {
            return ResponseEntity.badRequest()
                    .header("Motivo", "Erro no banco de dados")
                    .build();
        }
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> removerAluno(@PathVariable String matricula) {
        try {
            alunoService.removerAluno(Integer.parseInt(matricula));
            return ResponseEntity.ok().build();
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> listarAlunos() {
        List<String> nomes = new ArrayList<>();
        for (Aluno aluno : alunoService.listarAlunos()) {
            nomes.add(aluno.getNome());
        }
        return ResponseEntity.ok(nomes.toString());
    }
}