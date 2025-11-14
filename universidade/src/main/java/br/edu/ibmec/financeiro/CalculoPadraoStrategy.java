package br.edu.ibmec.financeiro;

import org.springframework.stereotype.Component;

import br.edu.ibmec.entity.Curso;

/**
 * Implementação Concreta da Strategy: Cálculo Padrão.
 * Apenas retorna o valor base da mensalidade do curso.
 */
@Component("calculoPadrao") // Torna esta classe um Bean gerenciado pelo Spring
public class CalculoPadraoStrategy implements CalculoMensalidadeStrategy {

    @Override
    public double calcular(Curso curso) {
        System.out.println("Cálculo Padrão: Valor R$ " + curso.getValorBaseMensalidade());
        return curso.getValorBaseMensalidade();
    }
}