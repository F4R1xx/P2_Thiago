package br.edu.ibmec.service;

import br.edu.ibmec.dao.AlunoRepository;
import br.edu.ibmec.dao.InscricaoRepository;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.financeiro.CalculoMensalidadeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @return O valor final da mensalidade
     * @throws DaoException Se o aluno não for encontrado
     */
    public double calcularMensalidade(int idAluno, CalculoMensalidadeStrategy strategy) throws DaoException {
        // 1. Verifica se o aluno existe
        if (!alunoRepository.existsById(idAluno)) {
            throw new DaoException("Aluno com ID " + idAluno + " não encontrado.");
        }

        // 2. Busca todas as inscrições (matrículas) ativas do aluno
        List<Inscricao> inscricoes = inscricaoRepository.findAllByAluno_Matricula(idAluno);

        if (inscricoes.isEmpty()) {
            // Não cobramos se não estiver inscrito em nada
            return 0.0;
        }

        // 3. Calcula o valor bruto somando o valor de cada disciplina
        // Usamos .distinct() para o caso de o aluno estar em duas turmas da mesma disciplina (não deve acontecer)
        double valorBruto = inscricoes.stream()
                .map(inscricao -> inscricao.getTurma().getDisciplina())
                .distinct()
                .mapToDouble(Disciplina::getValorDisciplina) // Referência de método (Java 8+)
                .sum();

        // 4. Aplica o Padrão Strategy sobre o valor bruto
        // Esta é a linha que corrige o erro que você viu em MatriculaService!
        return strategy.calcular(valorBruto);
    }
}