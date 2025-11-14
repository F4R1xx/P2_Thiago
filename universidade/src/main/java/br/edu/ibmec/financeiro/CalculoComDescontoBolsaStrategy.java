package br.edu.ibmec.financeiro;

import org.springframework.stereotype.Component;

/**
 * Implementação Concreta da Strategy: Cálculo para Bolsistas.
 * Aplica um desconto fixo de 20% sobre o valor bruto.
 */
@Component("calculoBolsista") 
public class CalculoComDescontoBolsaStrategy implements CalculoMensalidadeStrategy {

    private static final double PERCENTUAL_DESCONTO = 0.20; // 20%

    @Override
    public double calcular(double valorBruto) {
        double desconto = valorBruto * PERCENTUAL_DESCONTO;
        double valorFinal = valorBruto - desconto;

        System.out.println("Cálculo Bolsista: Valor Bruto R$ " + valorBruto + " | Desconto R$ " + desconto + " | Final R$ " + valorFinal);
        return valorFinal;
    }
}