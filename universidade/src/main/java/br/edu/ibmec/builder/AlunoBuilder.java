package br.edu.ibmec.builder;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Data;
import br.edu.ibmec.entity.EstadoCivil;

/**
 * Implementação do Design Pattern "Builder".
 * Simplifica a criação de um objeto Aluno (Entidade) a partir de um AlunoDTO,
 * cuidando das conversões e lógica de idade.
 */
public class AlunoBuilder {

    private Aluno aluno;
    private AlunoDTO dto;

    public AlunoBuilder(AlunoDTO dto) {
        this.aluno = new Aluno();
        this.dto = dto;
    }

    public AlunoBuilder comDadosPessoais() {
        aluno.setMatricula(dto.getMatricula());
        aluno.setNome(dto.getNome());
        aluno.setMatriculaAtiva(dto.isMatriculaAtiva());
        aluno.setTelefones(dto.getTelefones());
        // Assume um default ou converte o DTO
        aluno.setEstadoCivil(EstadoCivil.solteiro); 
        return this;
    }

    public AlunoBuilder comDataNascimentoEIdade() {
        if (dto.getDtNascimento() != null && !dto.getDtNascimento().isEmpty()) {
            Data dataNascimento = converterData(dto.getDtNascimento());
            aluno.setDataNascimento(dataNascimento);
            aluno.setIdade(calcularIdade(dataNascimento));
        }
        return this;
    }

    public Aluno build() {
        return this.aluno;
    }
    
    // Lógica de conversão movida do AlunoService/DTO para cá
    private Data converterData(String dataString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(dataString, formatter);
            
            Data dataRetorno = new Data();
            dataRetorno.setAno(localDate.getYear());
            dataRetorno.setMes(localDate.getMonthValue());
            dataRetorno.setDia(localDate.getDayOfMonth());
            return dataRetorno;
        } catch (Exception e) {
            // Logar o erro
            System.out.println("Erro ao converter data no Builder: " + e.getMessage());
            return null; // Ou lançar exceção
        }
    }

    private int calcularIdade(Data dataNascimento) {
        if (dataNascimento == null) return 0;
        
        try {
            LocalDate hoje = LocalDate.now();
            LocalDate nascimento = LocalDate.of(dataNascimento.getAno(), dataNascimento.getMes(), dataNascimento.getDia());
            return Period.between(nascimento, hoje).getYears();
        } catch (Exception e) {
            System.out.println("Erro ao calcular idade no Builder: " + e.getMessage());
            return 0;
        }
    }
}