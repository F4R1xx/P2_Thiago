package br.edu.ibmec.builder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Data;
import br.edu.ibmec.entity.EstadoCivil;

/**
 * Implementação do Design Pattern "Builder".
 * Esta classe facilita a construção de um objeto Aluno,
 * especialmente ao converter de um AlunoDTO.
 * Ela centraliza a lógica de conversão e validação da construção.
 */
public class AlunoBuilder {

    private int matricula;
    private String nome;
    private Data dataNascimento;
    private boolean matriculaAtiva;
    private EstadoCivil estadoCivil;
    private List<String> telefones;

    public AlunoBuilder comMatricula(int matricula) {
        this.matricula = matricula;
        return this;
    }

    public AlunoBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    /**
     * Converte a String de data do DTO para o objeto Data.
     */
    public AlunoBuilder comDataNascimento(String dataString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(dataString, formatter);
            this.dataNascimento = new Data(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
        } catch (Exception e) {
            System.out.println("Erro na conversão da data (Builder): " + e.getMessage());
            this.dataNascimento = null; // Ou lançar exceção
        }
        return this;
    }

    public AlunoBuilder comMatriculaAtiva(boolean ativa) {
        this.matriculaAtiva = ativa;
        return this;
    }

    public AlunoBuilder comEstadoCivil(EstadoCivil estadoCivil) {
        // No DTO, o EstadoCivilDTO é um enum, mas na entidade é outro.
        // Aqui poderia haver uma conversão, mas vamos simplificar
        // e assumir que o service vai definir um padrão.
        this.estadoCivil = estadoCivil;
        return this;
    }
    
    public AlunoBuilder comTelefones(List<String> telefones) {
        this.telefones = telefones;
        return this;
    }

    /**
     * Constrói o objeto Aluno final.
     * O curso (Curso) não é definido aqui, pois ele é uma associação
     * gerenciada pelo serviço de matrícula.
     */
    public Aluno build() {
        Aluno aluno = new Aluno();
        aluno.setMatricula(this.matricula);
        aluno.setNome(this.nome);
        aluno.setDataNascimento(this.dataNascimento);
        aluno.setMatriculaAtiva(this.matriculaAtiva);
        aluno.setTelefones(this.telefones);
        
        // Define um padrão se não for fornecido
        if (this.estadoCivil != null) {
            aluno.setEstadoCivil(this.estadoCivil);
        } else {
            aluno.setEstadoCivil(EstadoCivil.solteiro);
        }

        // O aluno é criado sem curso. A matrícula é um passo separado.
        aluno.setCurso(null); 
        
        // O construtor da entidade Aluno calcula a idade, mas vamos setar 0 aqui
        // pois a lógica de idade já estava no DTO
        aluno.setIdade(0); 

        return aluno;
    }

    /**
     * Método estático para facilitar a criação a partir de um DTO.
     */
    public static Aluno fromDTO(AlunoDTO dto) {
        return new AlunoBuilder()
                .comMatricula(dto.getMatricula())
                .comNome(dto.getNome())
                .comDataNascimento(dto.getDtNascimento())
                .comMatriculaAtiva(dto.isMatriculaAtiva())
                .comTelefones(dto.getTelefones())
                .build();
    }
}