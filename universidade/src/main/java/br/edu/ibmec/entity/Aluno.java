package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    private int matricula;

    @Column(nullable = false)
    private String nome;

    @Embedded
    private Data dataNascimento;

    private int idade;

    private boolean matriculaAtiva;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;

    @ElementCollection
    @CollectionTable(name = "aluno_telefones",
            joinColumns = @JoinColumn(name = "aluno_matricula"))
    @Column(name = "telefone")
    private List<String> telefones = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_codigo")
    private Curso curso;

    public Aluno(int matricula, String nome, Data dataNascimento,
                 boolean matriculaAtiva, EstadoCivil estadoCivil, Curso curso,
                 List<String> telefones) {
        this.matricula = matricula;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.matriculaAtiva = matriculaAtiva;
        this.estadoCivil = estadoCivil;
        this.curso = curso;

        this.idade = 0;
        this.telefones = telefones;
    }
}