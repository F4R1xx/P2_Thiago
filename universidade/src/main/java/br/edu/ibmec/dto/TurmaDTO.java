package br.edu.ibmec.dto;

/**
 * DTO simples para ajudar na criação da Turma.
 * Exemplo de JSON:
 * {
 * "disciplinaId": 1,
 * "professorId": 1,
 * "capacidade": 50
 * }
 */
public class TurmaDTO {
    private long disciplinaId;
    private long professorId;
    private int capacidade;

    // Getters e Setters
    public long getDisciplinaId() { return disciplinaId; }
    public void setDisciplinaId(long id) { this.disciplinaId = id; }
    public long getProfessorId() { return professorId; }
    public void setProfessorId(long id) { this.professorId = id; }
    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int cap) { this.capacidade = cap; }
}