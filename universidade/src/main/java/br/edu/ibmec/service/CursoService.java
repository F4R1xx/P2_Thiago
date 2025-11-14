package br.edu.ibmec.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.dao.CursoRepository;
import br.edu.ibmec.dto.CursoDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public CursoDTO buscarCurso(int codigo) throws DaoException {
        try{
            Curso curso = cursoRepository.findById(codigo)
                    .orElseThrow(() -> new DaoException("Curso não encontrado"));

            CursoDTO cursoDTO = new CursoDTO(curso.getCodigo(), curso.getNome());
            return cursoDTO;
        }
        catch(Exception e) // Alterado para Exception genérica para capturar orElseThrow
        {
            throw new DaoException("Erro ao buscar curso: " + e.getMessage());
        }
    }

    public Collection<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    @Transactional
    public void cadastrarCurso(CursoDTO cursoDTO) throws ServiceException,
            DaoException {
        if ((cursoDTO.getCodigo() < 1) || (cursoDTO.getCodigo() > 9999)) { // Aumentado limite do código
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        // CORREÇÃO AQUI: Aumentado o limite de 20 para 100 caracteres
        if ((cursoDTO.getNome() == null) || (cursoDTO.getNome().length() < 3)
                || (cursoDTO.getNome().length() > 100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getNome());

        try {
            if (cursoRepository.existsById(curso.getCodigo())) {
                throw new DaoException("Curso com código " + curso.getCodigo() + " já existe");
            }
            cursoRepository.save(curso);
        } catch (Exception e) {
            throw new DaoException("Erro ao salvar curso: " + e.getMessage());
        }
    }

    @Transactional
    public void alterarCurso(CursoDTO cursoDTO) throws ServiceException,
            DaoException {
        if ((cursoDTO.getCodigo() < 1) || (cursoDTO.getCodigo() > 9999)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        // CORREÇÃO AQUI: Aumentado o limite de 20 para 100 caracteres
        if ((cursoDTO.getNome() == null) || (cursoDTO.getNome().length() < 3)
                || (cursoDTO.getNome().length() > 100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getNome());

        try {
            if (!cursoRepository.existsById(curso.getCodigo())) {
                throw new DaoException("Curso com código " + curso.getCodigo() + " não encontrado");
            }
            cursoRepository.save(curso);
        } catch (Exception e) {
            throw new DaoException("Erro ao alterar curso: " + e.getMessage());
        }
    }

    @Transactional
    public void removerCurso(int codigo) throws DaoException {
        try {
            Curso curso = cursoRepository.findById(codigo)
                    .orElseThrow(() -> new DaoException("Curso com código " + codigo + " não encontrado"));

            // Validação de segurança: Não podemos remover curso se ele tiver disciplinas
            if (curso.getDisciplinas() != null && !curso.getDisciplinas().isEmpty()) {
                throw new DaoException("Não é possível remover curso. Existem " + curso.getDisciplinas().size() + " disciplinas associadas.");
            }

            cursoRepository.deleteById(codigo);
        }
        catch(Exception e)
        {
            throw new DaoException("Erro ao remover curso: " + e.getMessage());
        }
    }
}