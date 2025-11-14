package br.edu.ibmec.service;

import br.edu.ibmec.dao.AlunoRepository;
import br.edu.ibmec.dao.InscricaoRepository;
import br.edu.ibmec.dao.TurmaRepository;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Padrão Facade: Simplifica o processo complexo de inscrição.
 * Este serviço cuida da lógica de negócio de inscrever um aluno em uma turma,
 * escondendo a complexidade de verificar vagas, checar pré-requisitos, etc.
 *
 * Este arquivo SUBSTITUI a lógica de matrícula que estava no obsoleto MatriculaService.
 */
@Service
public class InscricaoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    /**
     * Método principal da Facade: Inscreve um aluno em uma turma específica.
     */
    @Transactional
    public Inscricao realizarInscricao(int idAluno, long idTurma) throws DaoException, ServiceException {
        // 1. Buscar as entidades
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new DaoException("Aluno com ID " + idAluno + " não encontrado."));

        Turma turma = turmaRepository.findById(idTurma)
                .orElseThrow(() -> new DaoException("Turma com ID " + idTurma + " não encontrada."));

        // 2. Aplicar Regras de Negócio
        // Regra 1: Verificar se o aluno já está inscrito nesta disciplina/turma
        boolean jaInscrito = inscricaoRepository.existsByAlunoAndTurma(aluno, turma);
        if (jaInscrito) {
            throw new ServiceException("Aluno já inscrito nesta turma.");
        }

        // Regra 2: Verificar vagas (Lógica movida para a entidade Turma)
        if (!turma.temVagasDisponiveis()) {
            throw new ServiceException("Turma lotada! Capacidade: " + turma.getCapacidade());
        }

        // 3. Criar a inscrição (se todas as regras passaram)
        Inscricao inscricao = new Inscricao();
        inscricao.setAluno(aluno);
        inscricao.setTurma(turma);
        inscricao.setDataInscricao(LocalDateTime.now());

        // 4. Salvar
        Inscricao inscricaoSalva = inscricaoRepository.save(inscricao);

        // 5. Atualizar a lista de inscrições na entidade Turma (para o lado "Um")
        turma.getInscricoes().add(inscricaoSalva);
        turmaRepository.save(turma);

        return inscricaoSalva;
    }

    /**
     * Método auxiliar (conveniente): Inscreve um aluno em uma DISCIPLINA.
     * O sistema automaticamente busca a primeira turma disponível para aquela disciplina.
     */
    @Transactional
    public Inscricao realizarInscricaoPorDisciplina(int idAluno, long idDisciplina) throws DaoException, ServiceException {
        // 1. Busca turmas disponíveis para a disciplina
        List<Turma> turmas = turmaRepository.findAllByDisciplina_Id(idDisciplina);
        if (turmas.isEmpty()) {
            throw new DaoException("Nenhuma turma encontrada para a disciplina ID " + idDisciplina);
        }

        // 2. CORREÇÃO: Buscar o aluno ANTES do loop (melhor performance)
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new DaoException("Aluno com ID " + idAluno + " não encontrado."));

        // 3. Lógica para encontrar uma turma com vaga
        Turma turmaComVaga = null;
        for (Turma turma : turmas) {
            if (turma.temVagasDisponiveis()) {
                // 4. CORREÇÃO: Usar o método 'existsByAlunoAndTurma' que existe no repositório
                boolean jaInscrito = inscricaoRepository.existsByAlunoAndTurma(aluno, turma);
                if (!jaInscrito) {
                    turmaComVaga = turma;
                    break;
                }
            }
        }

        // 5. Se não encontrou nenhuma turma com vaga
        if (turmaComVaga == null) {
            throw new ServiceException("Não há vagas em nenhuma turma para a disciplina ID " + idDisciplina + " (ou aluno já inscrito em todas as turmas disponíveis).");
        }

        // 6. Reutiliza o método principal da Facade
        return realizarInscricao(idAluno, turmaComVaga.getId());
    }
}