package br.edu.ibmec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.dao.AlunoRepository;
import br.edu.ibmec.dao.CursoRepository;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.financeiro.CalculoMensalidadeStrategy;

/**
 * Implementação do Design Pattern "Facade".
 * Esta classe atua como uma fachada, simplificando o processo complexo
 * de matrícula, que envolve múltiplos repositórios e regras de negócio.
 */
@Service
public class MatriculaService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    /**
     * Método principal da Facade.
     * Orquestra todo o processo de matrícula.
     * @param idAluno ID do aluno (deve ter sido cadastrado via AlunoService)
     * @param idCurso ID do curso
     * @throws DaoException Se o aluno ou curso não forem encontrados
     * @throws ServiceException Se regras de negócio (como vagas) não forem atendidas
     */
    @Transactional
    public void realizarMatricula(int idAluno, int idCurso) throws DaoException, ServiceException {
        
        // 1. Buscar as entidades
        Aluno aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new DaoException("Aluno não encontrado para matrícula."));

        Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new DaoException("Curso não encontrado para matrícula."));

        // 2. Verificar regras de negócio
        if (aluno.getCurso() != null) {
            throw new ServiceException("Aluno já está matriculado em um curso.");
        }

        if (!curso.temVagasDisponiveis()) {
            throw new ServiceException("O curso '" + curso.getNome() + "' não possui vagas disponíveis.");
        }

        // 3. Efetivar a associação (matrícula)
        aluno.setCurso(curso);
        // O relacionamento é bidirecional, então adicionamos o aluno ao curso também
        curso.getAlunos().add(aluno); 

        // 4. Salvar as entidades (o @Transactional gerencia isso, mas o save() é explícito)
        alunoRepository.save(aluno);
        cursoRepository.save(curso);
    }

    /**
     * Exemplo de extensão da Facade: Matricular E já calcular a mensalidade.
     * @param strategy A estratégia de cálculo (Padrão Strategy) a ser usada.
     */
    @Transactional
    public double matricularComCalculo(int idAluno, int idCurso, CalculoMensalidadeStrategy strategy) 
            throws DaoException, ServiceException {
        
        // 1. Reutiliza o método principal da fachada
        realizarMatricula(idAluno, idCurso);

        // 2. Busca o curso novamente (necessário se não estivesse no escopo)
        Curso curso = cursoRepository.findById(idCurso).get(); // Sabemos que existe

        // 3. Chama o Padrão Strategy para calcular
        double mensalidade = strategy.calcular(curso);

        // Aqui você poderia, por exemplo, salvar essa cobrança em um
        // repositório financeiro (FinanceiroRepository.save(new Cobranca(...)))
        
        System.out.println("Matrícula realizada. Mensalidade calculada: R$ " + mensalidade);
        return mensalidade;
    }
}