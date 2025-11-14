package br.edu.ibmec.financeiro;

/**
 * Interface para o Design Pattern "Strategy".
 * Define o contrato para qualquer algoritmo (estratégia)
 * que saiba como calcular uma mensalidade a partir de um valor bruto.
 */
public interface CalculoMensalidadeStrategy {
    
    /**
     * Aplica uma regra de negócio sobre o valor bruto da mensalidade.
     * @param valorBruto O valor total (soma das disciplinas)
     * @return O valor final da mensalidade.
     */
    double calcular(double valorBruto);
}