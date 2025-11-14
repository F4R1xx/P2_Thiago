package br.edu.ibmec.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Turma;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    /**
     * Verifica se já existe uma inscrição para este aluno nesta turma.
     */
    boolean existsByAlunoAndTurma(Aluno aluno, Turma turma);

    /**
     * Encontra todas as inscrições de um aluno específico.
     * Usado pelo MensalidadeService para calcular o total.
     */
    List<Inscricao> findAllByAluno_Matricula(int alunoMatricula);
}