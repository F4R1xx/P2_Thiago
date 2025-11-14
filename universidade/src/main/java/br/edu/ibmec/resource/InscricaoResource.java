package br.edu.ibmec.resource;

import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.financeiro.CalculoComDescontoBolsaStrategy;
import br.edu.ibmec.financeiro.CalculoPadraoStrategy;
import br.edu.ibmec.entity.Inscricao; // <-- CORREÇÃO AQUI: Importar a entidade
import br.edu.ibmec.service.InscricaoService;
import br.edu.ibmec.service.MensalidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Novo endpoint para expor o Padrão Facade (InscricaoService)
 * e o cálculo de mensalidade.
 */
@RestController
@RequestMapping("/inscricao")
public class InscricaoResource {

    @Autowired
    private InscricaoService inscricaoService; // A Facade

    @Autowired
    private MensalidadeService mensalidadeService;

    // Injetamos as duas estratégias de cálculo que criamos
    @Autowired
    private CalculoPadraoStrategy calculoPadrao;

    @Autowired
    private CalculoComDescontoBolsaStrategy calculoComDescontoBolsaStrategy;

    /**
     * Opção 1: Realiza a inscrição de um Aluno em uma Turma específica.
     * (Padrão Facade)
     */
    @PostMapping("/turma/{idAluno}/{idTurma}")
    public ResponseEntity<String> matricularAluno(
            @PathVariable int idAluno, 
            @PathVariable long idTurma) {
        try {
            inscricaoService.realizarInscricao(idAluno, idTurma);
            return ResponseEntity.ok("Inscrição na turma " + idTurma + " realizada com sucesso!");
        } catch (DaoException e) {
            // Erro de "Não Encontrado" (Aluno/Turma)
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ServiceException e) {
            // Erro de Regra de Negócio (Sem vagas, Já inscrito)
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * Opção 2: Realiza a inscrição de um Aluno em uma DISCIPLINA.
     * O sistema busca uma turma com vaga automaticamente.
     */
    @PostMapping("/disciplina/{idAluno}/{idDisciplina}")
    public ResponseEntity<String> matricularAlunoPorDisciplina(
            @PathVariable int idAluno, 
            @PathVariable long idDisciplina) {
        try {
            // Agora o 'import' existe, o Java vai encontrar a classe 'Inscricao'
            Inscricao inscricaoFeita = inscricaoService.realizarInscricaoPorDisciplina(idAluno, idDisciplina);
            return ResponseEntity.ok("Inscrição na disciplina " + idDisciplina + " realizada (turma " + inscricaoFeita.getTurma().getId() + " encontrada)!");
        } catch (DaoException e) {
            // Erro de "Não Encontrado" (Aluno/Disciplina/Turma)
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ServiceException e) {
            // Erro de Regra de Negócio (Sem vagas, Já inscrito)
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    /**
     * Calcula a mensalidade de um aluno com base nas suas inscrições.
     * (Usa o MensalidadeService + Padrão Strategy)
     * @param tipoCalculo "padrao" ou "bolsista"
     */
    @GetMapping("/mensalidade/{idAluno}")
    public ResponseEntity<String> getMensalidade(
            @PathVariable int idAluno,
            @RequestParam(defaultValue = "padrao") String tipoCalculo) {
        try {
            double mensalidade;
            if ("bolsista".equalsIgnoreCase(tipoCalculo)) {
                mensalidade = mensalidadeService.calcularMensalidade(idAluno, calculoComDescontoBolsaStrategy);
            } else {
                mensalidade = mensalidadeService.calcularMensalidade(idAluno, calculoPadrao);
            }
            return ResponseEntity.ok("Valor da mensalidade: R$ " + String.format("%.2f", mensalidade));
        } catch (DaoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}