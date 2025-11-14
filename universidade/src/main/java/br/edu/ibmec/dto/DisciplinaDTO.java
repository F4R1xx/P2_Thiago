package br.edu.ibmec.dto;

/**
 * DTO simples para ajudar na criação da Disciplina.
 * Exemplo de JSON:
 * {
 * "nome": "Engenharia de Software",
 * "valorDisciplina": 650.0,
 * "cursoId": 1
 * }
 */
public class DisciplinaDTO {
    private String nome;
    private double valorDisciplina;
    private int cursoId;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getValorDisciplina() { return valorDisciplina; }
    public void setValorDisciplina(double valor) { this.valorDisciplina = valor; }
    public int getCursoId() { return cursoId; }
    public void setCursoId(int cursoId) { this.cursoId = cursoId; }
}