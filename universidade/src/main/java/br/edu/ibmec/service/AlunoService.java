package br.edu.ibmec.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired; // NOVO: Importa o Builder
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
    private CursoRepository cursoRepository; // Este Service não deveria mais precisar disto

    // MÉTODO ANTIGO DE DATA (getData) FOI MOVIDO PARA DENTRO DO AlunoBuilder

    public AlunoDTO buscarAluno(int matricula) throws DaoException {
        try {
            Aluno aluno = alunoRepository.findById(matricula)
                    .orElseThrow(() -> new DaoException("Aluno não encontrado"));

            AlunoDTO alunoDTO = new AlunoDTO(
                    aluno.getMatricula(),
                    aluno.getNome(),
                    aluno.getDataNascimento() != null ? aluno.getDataNascimento().toString() : null,
                    aluno.isMatriculaAtiva(),
                    null, // DTO de EstadoCivil
                    aluno.getCurso() != null ? aluno.getCurso().getCodigo() : 0, // Retorna 0 se não tiver curso
                    aluno.getTelefones());
            return alunoDTO;
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar aluno");
        }
    }

    public Collection<Aluno> listarAlunos() {
        return alunoRepository.findAll();
    }

    /**
     * Este método agora representa a "Inscrição" (Cadastro) do aluno.
     * Ele apenas CRIA o aluno no sistema, mas não o matricula em um curso.
     * A matrícula será feita pelo MatriculaService (Padrão Facade).
     */
    @Transactional
    public Aluno cadastrarAluno(AlunoDTO alunoDTO) throws ServiceException,
            DaoException {
        if ((alunoDTO.getMatricula() < 1) || (alunoDTO.getMatricula() > 9999)) { // Aumentei o limite
            throw new ServiceException(
                    ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA); // Enum correto
        }
        if ((alunoDTO.getNome().length() < 1)
                || (alunoDTO.getNome().length() > 100)) { // Aumentei o limite
            throw new ServiceException(ServiceExceptionEnum.ALUNO_NOME_INVALIDO); // Enum correto
        }

        try {
            if (alunoRepository.existsById(alunoDTO.getMatricula())) {
                 throw new DaoException("Matrícula já existe");
            }
            
            // NOVO: Usa o Builder para criar o Aluno
            Aluno aluno = AlunoBuilder.fromDTO(alunoDTO);

            // O aluno é salvo SEM curso.
            return alunoRepository.save(aluno);

        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    @Transactional
    public void alterarAluno(AlunoDTO alunoDTO) throws ServiceException,
            DaoException {
        // Validações (similares ao cadastro)
        if ((alunoDTO.getMatricula() < 1) || (alunoDTO.getMatricula() > 9999)) {
            throw new ServiceException(
                    ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA);
        }
        if ((alunoDTO.getNome().length() < 1)
                || (alunoDTO.getNome().length() > 100)) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_NOME_INVALIDO);
        }

        try {
            // Busca o aluno existente
            Aluno aluno = alunoRepository.findById(alunoDTO.getMatricula())
                    .orElseThrow(() -> new DaoException("Aluno não encontrado"));

            // Atualiza os dados usando o Builder (ou setters diretos)
            // Vamos usar o Builder para manter a lógica de conversão da data
            Aluno dadosNovos = AlunoBuilder.fromDTO(alunoDTO);
            
            aluno.setNome(dadosNovos.getNome());
            aluno.setDataNascimento(dadosNovos.getDataNascimento());
            aluno.setMatriculaAtiva(dadosNovos.isMatriculaAtiva());
            aluno.setTelefones(dadosNovos.getTelefones());
            // Nota: Este método não altera o curso do aluno.
            // Isso deve ser feito por um serviço de "transferência".

            alunoRepository.save(aluno);
        } catch (DaoException e) {
            throw e;
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
            alunoRepository.deleteById(matricula);
        } catch (Exception e) {
            throw new DaoException("Erro ao remover aluno");
        }
    }
}