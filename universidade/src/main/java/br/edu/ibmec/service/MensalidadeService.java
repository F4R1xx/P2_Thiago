package br.edu.ibmec.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // NOVO IMPORT
import org.springframework.stereotype.Service; // NOVO IMPORT

import br.edu.ibmec.dao.AlunoRepository;
import br.edu.ibmec.dao.InscricaoRepository;
import br.edu.ibmec.dto.MensalidadeDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.financeiro.CalculoMensalidadeStrategy; // NOVO IMPORT

/**
 * Serviço dedicado a calcular a mensalidade de um aluno.
 * Ele usa o Padrão Strategy para definir COMO o cálculo final será feito
 * (ex: com desconto, sem desconto, etc.).
 */
@Service
public class MensalidadeService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    /**
     * Calcula a mensalidade de um aluno com base em uma estratégia de cálculo.
     *
     * @param idAluno  O ID do aluno
     * @param strategy A estratégia de cálculo (Padrão ou Bolsista)
     * @param tipoCalculo O nome da estratégia (para o DTO)
     * @return O DTO com os detalhes da mensalidade
     * @throws DaoException Se o aluno não for encontrado
     */
    public MensalidadeDTO calcularMensalidade(int idAluno, CalculoMensalidadeStrategy strategy, String tipoCalculo) throws DaoException {
        // 1. Verifica se o aluno existe e busca seu nome
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new DaoException("Aluno com ID " + idAluno + " não encontrado."));

        // 2. Busca todas as inscrições (matrículas) ativas do aluno
        List<Inscricao> inscricoes = inscricaoRepository.findAllByAluno_Matricula(idAluno);

        // 3. Prepara a lista de nomes de disciplinas
        List<String> nomesDisciplinas = inscricoes.stream()
                .map(inscricao -> inscricao.getTurma().getDisciplina().getNome())
                .distinct()
                .collect(Collectors.toList());

        // 4. Calcula o valor bruto somando o valor de cada disciplina
        // Usamos .distinct() para o caso de o aluno estar em duas turmas da mesma disciplina (não deve acontecer)
        double valorBruto = inscricoes.stream()
                .map(inscricao -> inscricao.getTurma().getDisciplina())
                .distinct()
                .mapToDouble(Disciplina::getValorDisciplina) // Referência de método (Java 8+)
                .sum();

        // 5. Aplica o Padrão Strategy sobre o valor bruto
        double valorFinal = strategy.calcular(valorBruto);

        // 6. Monta o DTO de resposta
        MensalidadeDTO dto = new MensalidadeDTO();
        dto.setNomeAluno(aluno.getNome());
        dto.setTipoCalculo(tipoCalculo);
        dto.setDisciplinas(nomesDisciplinas);
        dto.setValorBruto(valorBruto);
        dto.setValorFinal(valorFinal);

        return dto;
    }
}