package br.edu.ibmec.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}