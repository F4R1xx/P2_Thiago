/**
* Aplicação com serviços REST para gestão de cursos.
*
* @author  Thiago Silva de Souza
* @version 1.0
* @since   2012-02-29 
*/

package br.edu.ibmec.dto;

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
	private boolean matriculaAtiva;
	private List<String> telefones;

	// Campos removidos:
	// - private int idade; (Será calculada e salva no banco pelo AlunoBuilder)
	// - private EstadoCivilDTO estadoCivilDTO; (Será definido como "solteiro" pelo AlunoBuilder)
	// - private int curso; (Campo obsoleto, aluno não tem mais curso direto)
	
	// Construtor complexo e método getIdadeConvertida() foram removidos.
}