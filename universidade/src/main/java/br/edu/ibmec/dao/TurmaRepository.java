package br.edu.ibmec.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    
    /**
     * Encontra todas as turmas disponíveis para uma disciplina específica.
     * Usado pelo InscricaoService (Facade) para encontrar vagas.
     */
    List<Turma> findAllByDisciplina_Id(long disciplinaId);
}