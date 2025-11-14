package br.edu.ibmec.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade "Inscrição" do diagrama [cite: image_8a7d64.jpg].
 * Esta é a classe de associação que liga Aluno e Turma.
 */
@Entity
@Table(name = "inscricoes")
@Getter
@Setter
@NoArgsConstructor
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Relação 0..* com Aluno
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_matricula")
    private Aluno aluno;

    // Relação 0..* com Turma
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turma_id")
    private Turma turma;

    @Column(nullable = false)
    private LocalDateTime dataInscricao;
    
    private Double notaFinal; // Pode ser nulo até o fim do semestre

    public Inscricao(Aluno aluno, Turma turma) {
        this.aluno = aluno;
        this.turma = turma;
        this.dataInscricao = LocalDateTime.now();
    }
}