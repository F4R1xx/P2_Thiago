package br.edu.ibmec.service;

// imports...
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.builder.AlunoBuilder;
import br.edu.ibmec.dao.AlunoRepository;
import br.edu.ibmec.dao.CursoRepository;
import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository; // Mantido por enquanto, pode ser removido

    // Removido: A lógica de conversão de data foi para o AlunoBuilder
    // public static final Data getData(String dataString) { ... }

    public AlunoDTO buscarAluno(int matricula) throws DaoException {
        try {
            Aluno aluno = alunoRepository.findById(matricula)
                    .orElseThrow(() -> new DaoException("Aluno não encontrado"));

            AlunoDTO alunoDTO = new AlunoDTO(
                    aluno.getMatricula(),
                    aluno.getNome(),
                    aluno.getDataNascimento().toString(),
                    aluno.isMatriculaAtiva(),
                    null, // DTO de EstadoCivil (não implementado)
                    0, // Código do curso (REMOVIDO - Aluno não tem mais curso direto)
                    aluno.getTelefones());
            return alunoDTO;
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar aluno: " + e.getMessage());
        }
    }

    public Collection<Aluno> listarAlunos() {
        return alunoRepository.findAll();
    }

    @Transactional
    public Aluno cadastrarAluno(AlunoDTO alunoDTO) throws ServiceException,
            DaoException {
        
        // Validação (movida para cá, antes era de curso)
        if ((alunoDTO.getMatricula() < 1)) {
            throw new ServiceException(
                    ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA);
        }
        if ((alunoDTO.getNome() == null || alunoDTO.getNome().length() < 1)
                || (alunoDTO.getNome().length() > 50)) { // Aumentei o limite
            throw new ServiceException(ServiceExceptionEnum.ALUNO_NOME_INVALIDO);
        }

        if (alunoRepository.existsById(alunoDTO.getMatricula())) {
            throw new DaoException("Matrícula já existe");
        }

        try {
            // Usando o Padrão Builder
            Aluno aluno = new AlunoBuilder(alunoDTO)
                .comDadosPessoais()
                .comDataNascimentoEIdade()
                .build();

            return alunoRepository.save(aluno);

        } catch (Exception e) {
            throw new DaoException("Erro ao salvar aluno no banco: " + e.getMessage());
        }
    }

    @Transactional
    public void alterarAluno(AlunoDTO alunoDTO) throws ServiceException,
            DaoException {
        // Validações
        if ((alunoDTO.getMatricula() < 1)) {
            throw new ServiceException(
                    ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA);
        }
        if ((alunoDTO.getNome() == null || alunoDTO.getNome().length() < 1)
                || (alunoDTO.getNome().length() > 50)) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_NOME_INVALIDO);
        }

        // Verifica se existe
        if (!alunoRepository.existsById(alunoDTO.getMatricula())) {
            throw new DaoException("Aluno não encontrado para alteração");
        }

        try {
            // Usando o Padrão Builder para criar o objeto atualizado
            Aluno aluno = new AlunoBuilder(alunoDTO)
                .comDadosPessoais()
                .comDataNascimentoEIdade()
                .build();
            
            alunoRepository.save(aluno);

        } catch (Exception e) {
            throw new DaoException("Erro ao alterar aluno: " + e.getMessage());
        }
    }

    @Transactional
    public void removerAluno(int matricula) throws DaoException {
        try {
            if (!alunoRepository.existsById(matricula)) {
                throw new DaoException("Aluno não encontrado");
            }
            // A remoção das inscrições é automática (orphanRemoval=true)
            alunoRepository.deleteById(matricula);
        } catch (Exception e) {
            throw new DaoException("Erro ao remover aluno: " + e.getMessage());
        }
    }
}