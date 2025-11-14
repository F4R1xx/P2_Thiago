package br.edu.ibmec.financeiro;

import org.springframework.stereotype.Component;

import br.edu.ibmec.entity.Curso;

/**
 * Implementação Concreta da Strategy: Cálculo para Bolsistas.
 * Aplica um desconto fixo de 20% sobre o valor base.
 */
@Component("calculoBolsista") // Torna esta classe um Bean gerenciado pelo Spring
public class CalculoComDescontoBolsaStrategy implements CalculoMensalidadeStrategy {

    private static final double PERCENTUAL_DESCONTO = 0.20; // 20%

    @Override
    public double calcular(Curso curso) {
        double valorBase = curso.getValorBaseMensalidade();
        double desconto = valorBase * PERCENTUAL_DESCONTO;
        double valorFinal = valorBase - desconto;

        System.out.println("Cálculo Bolsista: Valor Base R$ " + valorBase + " | Desconto R$ " + desconto + " | Final R$ " + valorFinal);
        return valorFinal;
    }
}