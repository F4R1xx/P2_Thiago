package br.edu.ibmec.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    private int codigo;

    @Column(nullable = false)
    private String nome;

    // NOVO: Valor base para o cálculo da mensalidade
    @Column(nullable = false, columnDefinition = "double default 1000.0")
    private double valorBaseMensalidade = 1000.0;

    // NOVO: Capacidade máxima de alunos no curso
    @Column(nullable = false, columnDefinition = "integer default 50")
    private int capacidadeAlunos = 50;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Aluno> alunos = new ArrayList<Aluno>();

    public Curso(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    // NOVO: Método helper para verificar vagas
    public boolean temVagasDisponiveis() {
        return this.alunos.size() < this.capacidadeAlunos;
    }
}