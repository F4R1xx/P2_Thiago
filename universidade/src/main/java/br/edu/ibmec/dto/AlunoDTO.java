/**
* Aplicação com serviços REST para gestão de cursos.
*
* @author  Thiago Silva de Souza
* @version 1.0
* @since   2012-02-29 
*/

package br.edu.ibmec.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@XmlRootElement(name="aluno")
public class AlunoDTO {
	private int matricula;
	private String nome;
	private String dtNascimento;
	private int idade;
	private boolean matriculaAtiva;
	private EstadoCivilDTO estadoCivilDTO;
	private List<String> telefones;

	private int curso;

    public AlunoDTO(int matricula,
			String nome, 
			String dtNascimento,
			boolean matriculaAtiva, 
			EstadoCivilDTO estadoCivilDTO, 
			int curso,
			List<String> telefones) {
		this.matricula = matricula;
		this.nome = nome;
		this.dtNascimento = dtNascimento;
		this.matriculaAtiva = matriculaAtiva;
		this.estadoCivilDTO = estadoCivilDTO;
		this.curso = curso;
		
		this.idade = getIdadeConvertida(dtNascimento);
		this.telefones = telefones;
	}
	
	private int getIdadeConvertida(String data)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dataConvertida = null;
		try {
			dataConvertida = sdf.parse(data);
			Date hoje = new Date();
			return hoje.getYear() - dataConvertida.getYear();
		} catch (Exception e) {
			System.out.println("Erro Convers�o da idade: " + e.getMessage());
			return 0;
		}
	}

}
