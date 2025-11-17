package br.edu.ibmec.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para retornar a resposta detalhada do cálculo da mensalidade.
 */
@Getter
@Setter
@NoArgsConstructor
public class MensalidadeDTO {
    
    private String nomeAluno;
    private String tipoCalculo; // "padrao" ou "bolsista"
    private List<String> disciplinas;
    private double valorBruto;
    private double valorFinal; // Valor com desconto aplicado (se houver)
}