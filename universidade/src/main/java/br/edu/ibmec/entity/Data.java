package br.edu.ibmec.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Data {
	private int dia;
	private int mes;
	private int ano;

    public Data(int dia, int mes, int ano) {
		this.dia = dia;
		this.mes = mes;
		this.ano = ano;
	}

	@Override
	public String toString() {
		return ""+dia+"/"+mes+"/"+ano;
	}
}
