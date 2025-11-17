package br.edu.ibmec.resource;

import java.util.Map; // ALTERADO: Novo import

import org.springframework.beans.factory.annotation.Autowired; // ALTERADO: Novo import
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ibmec.dto.InscricaoTurmaDTO; // NOVO IMPORT
import br.edu.ibmec.dto.MensalidadeDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.financeiro.CalculoComDescontoBolsaStrategy;
import br.edu.ibmec.financeiro.CalculoPadraoStrategy;
import br.edu.ibmec.service.InscricaoService;
import br.edu.ibmec.service.MensalidadeService; // NOVO IMPORT

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
     * ALTERADO: Recebe os dados via @RequestBody
     */
    @PostMapping("/turma") // ALTERADO: URL simplificada
    public ResponseEntity<String> matricularAluno(
            @RequestBody InscricaoTurmaDTO dto) { // ALTERADO: Usa o DTO
        try {
            // ALTERADO: Busca os dados do DTO
            inscricaoService.realizarInscricao(dto.getIdAluno(), dto.getIdTurma());
            return ResponseEntity.ok("Inscrição na turma " + dto.getIdTurma() + " realizada com sucesso!");
        } catch (DaoException e) {
            // Erro de "Não Encontrado" (Aluno/Turma)
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (ServiceException e) {
            // Erro de Regra de Negócio (Sem vagas, Já inscrito)
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    /**
     * REMOVIDO: Endpoint de inscrição por disciplina foi removido.
     */
    // @PostMapping("/disciplina") 
    // public ResponseEntity<String> matricularAlunoPorDisciplina(
    //         @RequestBody InscricaoDisciplinaDTO dto) { 
    //     try {
    //         Inscricao inscricaoFeita = inscricaoService.realizarInscricaoPorDisciplina(dto.getIdAluno(), dto.getIdDisciplina());
    //         return ResponseEntity.ok("Inscrição na disciplina " + dto.getIdDisciplina() + " realizada (turma " + inscricaoFeita.getTurma().getId() + " encontrada)!");
    //     } catch (DaoException e) {
    //         return ResponseEntity.status(404).body(e.getMessage());
    //     } catch (ServiceException e) {
    //         return ResponseEntity.status(400).body(e.getMessage());
    //     }
    // }


    /**
     * Calcula a mensalidade de um aluno com base nas suas inscrições.
     * (Usa o MensalidadeService + Padrão Strategy)
     * @param tipoCalculo "padrao" ou "bolsista"
     */
    @GetMapping("/mensalidade/{idAluno}")
    public ResponseEntity<?> getMensalidade( // ALTERADO para ResponseEntity<?>
            @PathVariable int idAluno,
            @RequestParam(defaultValue = "padrao") String tipoCalculo) {
        try {
            MensalidadeDTO dto; // ALTERADO
            if ("bolsista".equalsIgnoreCase(tipoCalculo)) {
                dto = mensalidadeService.calcularMensalidade(idAluno, calculoComDescontoBolsaStrategy, "bolsista");
            } else {
                dto = mensalidadeService.calcularMensalidade(idAluno, calculoPadrao, "padrao");
            }
            return ResponseEntity.ok(dto); // ALTERADO: Retorna o DTO
        } catch (DaoException e) {
            // ALTERADO: Retorna um JSON de erro
            Map<String, String> erro = Map.of("erro", e.getMessage());
            return ResponseEntity.status(404).body(erro);
        }
    }
}