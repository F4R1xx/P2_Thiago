package br.edu.ibmec.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.service.AlunoService;

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

    /**
     * Endpoint de "Inscrição" (Cadastro) do aluno no sistema.
     * Não matricula em curso, apenas cria o Aluno.
     */
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> cadastrarAluno(@RequestBody AlunoDTO alunoDTO)
            throws ServiceException, DaoException {
        try {
            // O AlunoService agora retorna o Aluno salvo, que podemos usar
            Aluno alunoSalvo = alunoService.cadastrarAluno(alunoDTO);
            URI location = URI.create("" + alunoSalvo.getMatricula());
            return ResponseEntity.created(location).build();
        } catch (ServiceException e) {
            // Ajustado para os Enums corretos de Aluno
            if (e.getTipo() == ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA) {
                return ResponseEntity.badRequest()
                        .header("Motivo", "Matrícula inválida")
                        .build();
            }
            if (e.getTipo() == ServiceExceptionEnum.ALUNO_NOME_INVALIDO) {
                return ResponseEntity.badRequest()
                        .header("Motivo", "Nome inválido")
                        .build();
            } else {
                return ResponseEntity.badRequest()
                        .header("Motivo", e.getMessage())
                        .build();
            }
        } catch (DaoException e) {
            // Pode retornar 409 (Conflict) se a matrícula já existir
            if (e.getMessage().contains("Matrícula já existe")) {
                return ResponseEntity.status(409).header("Motivo", e.getMessage()).build();
            }
            return ResponseEntity.badRequest()
                    .header("Motivo", "Erro no banco de dados: " + e.getMessage())
                    .build();
        }
    }

    @PutMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> alterarAluno(@RequestBody AlunoDTO alunoDTO)
            throws ServiceException, DaoException {
        try {
            alunoService.alterarAluno(alunoDTO);
            // Alteração não cria um novo recurso, então 200 OK
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
             if (e.getTipo() == ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA) {
                return ResponseEntity.badRequest()
                        .header("Motivo", "Matrícula inválida")
                        .build();
            }
            if (e.getTipo() == ServiceExceptionEnum.ALUNO_NOME_INVALIDO) {
                return ResponseEntity.badRequest()
                        .header("Motivo", "Nome inválido")
                        .build();
            } else {
                return ResponseEntity.badRequest()
                        .header("Motivo", e.getMessage())
                        .build();
            }
        } catch (DaoException e) {
            // Se o aluno não for encontrado para alteração
            if (e.getMessage().contains("Aluno não encontrado")) {
                 return ResponseEntity.status(404).header("Motivo", e.getMessage()).build();
            }
            return ResponseEntity.badRequest()
                    .header("Motivo", "Erro no banco de dados: " + e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> removerAluno(@PathVariable String matricula) {
        try {
            alunoService.removerAluno(Integer.parseInt(matricula));
            return ResponseEntity.ok().build();
        } catch (DaoException e) {
            // CORREÇÃO:
            // A linha anterior era: .body(e.getMessage()).build() -> Incorreto
            // A linha correta usa .header() (que retorna um builder) ou .build() direto
            return ResponseEntity.status(404).header("Motivo", e.getMessage()).build();
        }
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> listarAlunos() {
        List<String> nomes = new ArrayList<>();
        // O método listarAlunos() foi mantido no AlunoService
        for (Aluno aluno : alunoService.listarAlunos()) {
            nomes.add(aluno.getNome());
        }
        return ResponseEntity.ok(nomes.toString());
    }
}