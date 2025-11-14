package br.edu.ibmec.financeiro;

import br.edu.ibmec.entity.Curso;

/**
 * Interface para o Design Pattern "Strategy".
 * Define o contrato para qualquer algoritmo (estratégia)
 * que saiba como calcular uma mensalidade.
 */
public interface CalculoMensalidadeStrategy {
    
    /**
     * Calcula a mensalidade com base no curso.
     * @param curso O curso no qual o aluno está se matriculando.
     * @return O valor final da mensalidade.
     */
    double calcular(Curso curso);
}