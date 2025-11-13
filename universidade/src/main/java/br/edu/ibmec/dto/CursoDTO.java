package br.edu.ibmec.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@XmlRootElement(name="curso")
public class CursoDTO {
	private int codigo;
	private String nome;

    public CursoDTO(int codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "CursoDTO [codigo=" + codigo + ", nome=" + nome + "]";
	}

}
