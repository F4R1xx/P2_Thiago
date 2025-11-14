package br.edu.ibmec.financeiro;

import org.springframework.stereotype.Component;

/**
 * Implementação Concreta da Strategy: Cálculo Padrão.
 * Apenas retorna o valor bruto recebido.
 */
@Component("calculoPadrao") 
public class CalculoPadraoStrategy implements CalculoMensalidadeStrategy {

    @Override
    public double calcular(double valorBruto) {
        System.out.println("Cálculo Padrão: Valor Bruto R$ " + valorBruto);
        return valorBruto;
    }
}