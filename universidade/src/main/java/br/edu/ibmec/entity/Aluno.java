package br.edu.ibmec.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    // REMOVIDO: O Aluno não se liga mais direto ao Curso
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "curso_codigo")
    // private Curso curso;
    
    // NOVO: Um aluno tem várias inscrições (em turmas)
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscricao> inscricoes = new ArrayList<>();


    // Construtor foi removido - Usaremos o AlunoBuilder para criar
}