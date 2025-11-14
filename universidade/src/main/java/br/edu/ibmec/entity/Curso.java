package br.edu.ibmec.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    // REMOVIDO: O Curso não se liga mais direto ao Aluno
    // @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Aluno> alunos = new ArrayList<Aluno>();

    // NOVO: Relação "Todo" (Whole) do diagrama. Um Curso tem muitas Disciplinas
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Curso(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }
}