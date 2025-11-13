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

import br.edu.ibmec.service.CursoService;
import br.edu.ibmec.dto.CursoDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

@RestController
@RequestMapping("/curso")
public class CursoResource {

	private CursoService cursoService;

    @Autowired
    public CursoResource(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping(value = "/{codigo}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CursoDTO> buscarCurso(@PathVariable("codigo") String codigo) {
        try {
            CursoDTO cursoDTO = cursoService.buscarCurso(Integer.parseInt(codigo));
            return ResponseEntity.ok(cursoDTO);
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarCurso(@RequestBody CursoDTO cursoDTO)
            throws ServiceException, DaoException {
        try {
            cursoService.cadastrarCurso(cursoDTO);
            return ResponseEntity.created(new URI("" + cursoDTO.getCodigo())).build();
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO) {
                return ResponseEntity.status(400)
                        .header("Motivo", e.getTipo().toString())
                        .build();
            }
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO) {
                return ResponseEntity.status(400)
                        .header("Motivo", "Nome inválido")
                        .build();
            } else {
                return ResponseEntity.status(400)
                        .header("Motivo", e.getMessage())
                        .build();
            }
        } catch (DaoException e) {
            return ResponseEntity.status(400)
                    .header("Motivo", "Erro no banco de dados")
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }

    @PutMapping
    public ResponseEntity<Void> alterarCurso(@RequestBody CursoDTO cursoDTO) {
        try {
            cursoService.alterarCurso(cursoDTO);
            return ResponseEntity.created(new URI("" + cursoDTO.getCodigo())).build();
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO) {
                return ResponseEntity.status(400)
                        .header("Motivo", "Código inválido")
                        .build();
            }
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO) {
                return ResponseEntity.status(400)
                        .header("Motivo", "Nome inválido")
                        .build();
            } else {
                return ResponseEntity.status(400)
                        .header("Motivo", e.getMessage())
                        .build();
            }
        } catch (DaoException e) {
            return ResponseEntity.status(400)
                    .header("Motivo", "Erro no banco de dados")
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> removerCurso(@PathVariable("codigo") String codigo) {
        try {
            cursoService.removerCurso(Integer.parseInt(codigo));
            return ResponseEntity.ok().build();
        } catch (DaoException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String listarCursos() {
        List<String> nomes = new ArrayList<String>();
        for (Iterator<Curso> it = cursoService.listarCursos().iterator(); it
                .hasNext();) {
            Curso curso = (Curso) it.next();
            nomes.add(curso.getNome());
        }
        return nomes.toString();
    }
}