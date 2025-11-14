package br.edu.ibmec.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.financeiro.CalculoComDescontoBolsaStrategy;
import br.edu.ibmec.financeiro.CalculoPadraoStrategy;
import br.edu.ibmec.service.MatriculaService;

/**
 * Novo endpoint para expor o Padrão Facade (MatriculaService).
 */
@RestController
@RequestMapping("/matricular")
public class MatriculaResource {

    @Autowired
    private MatriculaService matriculaService; // A Facade

    // Injetamos as duas estratégias de cálculo que criamos
    @Autowired
    private CalculoPadraoStrategy calculoPadrao;

    @Autowired
    private CalculoComDescontoBolsaStrategy calculoBolsista;

    /**
     * Realiza a matrícula simples (Aluno em Curso).
     */
    @PostMapping("/{idAluno}/{idCurso}")
    public ResponseEntity<String> matricularAluno(@PathVariable int idAluno, @PathVariable int idCurso) {
        try {
            matriculaService.realizarMatricula(idAluno, idCurso);
            return ResponseEntity.ok("Matrícula realizada com sucesso!");
        } catch (DaoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Exemplo de endpoint que usa a Facade + Strategy.
     * Matricula um aluno e calcula a mensalidade usando uma estratégia específica.
     * @param tipoCalculo "padrao" ou "bolsista"
     */
    @PostMapping("/{idAluno}/{idCurso}/calcular")
    public ResponseEntity<String> matricularComCalculo(
            @PathVariable int idAluno, 
            @PathVariable int idCurso,
            @RequestParam(defaultValue = "padrao") String tipoCalculo) {
        try {
            double mensalidade;
            if ("bolsista".equalsIgnoreCase(tipoCalculo)) {
                mensalidade = matriculaService.matricularComCalculo(idAluno, idCurso, calculoBolsista);
            } else {
                mensalidade = matriculaService.matricularComCalculo(idAluno, idCurso, calculoPadrao);
            }
            return ResponseEntity.ok("Matrícula realizada! Valor da mensalidade: R$ " + String.format("%.2f", mensalidade));
        } catch (DaoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ServiceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}