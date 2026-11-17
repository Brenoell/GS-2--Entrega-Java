
package com.fiap.minicity.service;

import com.fiap.minicity.dao.OcorrenciaDAO;
import com.fiap.minicity.dao.DistritoDAO;
import com.fiap.minicity.dao.CategoriaOcorrenciaDAO;
import com.fiap.minicity.dao.FonteDadoDAO;
import com.fiap.minicity.dao.PessoaDAO;

import com.fiap.minicity.dto.OcorrenciaDTO;
import com.fiap.minicity.dto.CriarOcorrenciaDTO;

import com.fiap.minicity.exception.RecursoNaoEncontradoException;
import com.fiap.minicity.exception.RegraNegocioException;

import com.fiap.minicity.mapper.OcorrenciaMapper;

import com.fiap.minicity.models.Ocorrencia;
import com.fiap.minicity.models.Distrito;
import com.fiap.minicity.models.CategoriaOcorrencia;
import com.fiap.minicity.models.FonteDado;
import com.fiap.minicity.models.Pessoa;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável pelas regras de negócio das ocorrências.
 * Realiza validações, consultas e operações de cadastro, atualização e exclusão.
 * Também gerencia os relacionamentos entre ocorrências, pessoas, distritos,
 * categorias e fontes de dados.
 */
@Service
public class OcorrenciaService {

    /** DAO responsável pelas operações de acesso às ocorrências. */
    private final OcorrenciaDAO dao;

    /** DAO responsável pelo acesso aos dados dos distritos. */
    private final DistritoDAO distritoDAO;

    /** DAO responsável pelo acesso às categorias de ocorrência. */
    private final CategoriaOcorrenciaDAO categoriaDAO;

    /** DAO responsável pelo acesso às fontes de dados. */
    private final FonteDadoDAO fonteDAO;

    /** DAO responsável pelo acesso aos dados das pessoas. */
    private final PessoaDAO pessoaDAO;

    /**
     * Constrói o serviço recebendo todos os DAOs necessários
     * para acessar os dados das ocorrências e seus relacionamentos.
     *
     * @param dao DAO responsável pelas ocorrências
     * @param distritoDAO DAO responsável pelos distritos
     * @param categoriaDAO DAO responsável pelas categorias de ocorrência
     * @param fonteDAO DAO responsável pelas fontes de dados
     * @param pessoaDAO DAO responsável pelas pessoas
     */
    public OcorrenciaService(
            OcorrenciaDAO dao,
            DistritoDAO distritoDAO,
            CategoriaOcorrenciaDAO categoriaDAO,
            FonteDadoDAO fonteDAO,
            PessoaDAO pessoaDAO
    ) {
        this.dao = dao;
        this.distritoDAO = distritoDAO;
        this.categoriaDAO = categoriaDAO;
        this.fonteDAO = fonteDAO;
        this.pessoaDAO = pessoaDAO;
    }

    /**
     * Lista todas as ocorrências cadastradas.
     * Carrega os dados dos relacionamentos antes de converter
     * cada ocorrência para DTO.
     *
     * @return lista de ocorrências em formato DTO
     */
    public List<OcorrenciaDTO> listar() {

        List<Ocorrencia> ocorrencias = dao.listar();

        return ocorrencias.stream()
                .map(ocorrencia -> {
                    carregarRelacionamentos(ocorrencia);
                    return OcorrenciaMapper.toDTO(ocorrencia);
                })
                .toList();
    }

    /**
     * Busca uma ocorrência pelo seu ID.
     *
     * @param id identificador da ocorrência que será buscada
     * @return ocorrência encontrada em formato DTO
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se a ocorrência não existir
     */
    public OcorrenciaDTO buscarPorId(Long id) {

        validarId(id);

        Ocorrencia ocorrencia = dao.buscarPorId(id);

        if (ocorrencia == null) {
            throw new RecursoNaoEncontradoException(
                    "Ocorrência não encontrada."
            );
        }

        carregarRelacionamentos(ocorrencia);

        return OcorrenciaMapper.toDTO(ocorrencia);
    }

    /**
     * Cadastra uma nova ocorrência utilizando os dados informados no DTO.
     * Verifica a existência do distrito, da categoria e da pessoa ou fonte
     * de dados, conforme a origem da ocorrência.
     *
     * @param objeto dados da ocorrência que será cadastrada
     * @return ocorrência cadastrada em formato DTO
     * @throws RegraNegocioException se os dados informados forem inválidos
     * @throws RecursoNaoEncontradoException se algum relacionamento informado não existir
     */
    public OcorrenciaDTO cadastrar(OcorrenciaDTO objeto) {

        validarOcorrencia(objeto);

        Distrito distrito = distritoDAO.buscarPorId(
                objeto.distrito().id()
        );

        if (distrito == null) {
            throw new RecursoNaoEncontradoException(
                    "Distrito não encontrado."
            );
        }

        CategoriaOcorrencia categoria = categoriaDAO.buscarPorId(
                objeto.categoria().id()
        );

        if (categoria == null) {
            throw new RecursoNaoEncontradoException(
                    "Categoria não encontrada."
            );
        }

        FonteDado fonte = null;
        Pessoa pessoa = null;

        if ("API".equals(objeto.origem())) {

            fonte = fonteDAO.buscarPorId(
                    objeto.fonte().id()
            );

            if (fonte == null) {
                throw new RecursoNaoEncontradoException(
                        "Fonte de dados não encontrada."
                );
            }

        } else {

            pessoa = pessoaDAO.buscarPorId(
                    objeto.pessoa().id()
            );

            if (pessoa == null) {
                throw new RecursoNaoEncontradoException(
                        "Pessoa não encontrada."
                );
            }
        }

        Ocorrencia ocorrencia = OcorrenciaMapper.toModel(objeto);

        ocorrencia.setDistrito(distrito);
        ocorrencia.setCategoria(categoria);
        ocorrencia.setFonte(fonte);
        ocorrencia.setPessoa(pessoa);

        Ocorrencia cadastrada = dao.cadastrar(ocorrencia);

        carregarRelacionamentos(cadastrada);

        return OcorrenciaMapper.toDTO(cadastrada);
    }

    /**
     * Cadastra uma ocorrência criada por um cidadão autenticado.
     * Valida os dados, busca a pessoa, o distrito e a categoria,
     * e define automaticamente a origem, o status e as datas.
     *
     * @param objeto dados informados para criar a ocorrência
     * @param idPessoa identificador da pessoa autenticada que está cadastrando
     * @return ocorrência cadastrada em formato DTO
     * @throws RegraNegocioException se os dados ou o ID da pessoa forem inválidos
     * @throws RecursoNaoEncontradoException se a pessoa, o distrito ou a categoria não existirem
     */
    public OcorrenciaDTO cadastrarComoCidadao(
            CriarOcorrenciaDTO objeto,
            Long idPessoa
    ) {

        if (objeto == null) {
            throw new RegraNegocioException(
                    "Os dados da ocorrência são obrigatórios."
            );
        }

        validarId(idPessoa);

        if (objeto.nomeTitulo() == null ||
                objeto.nomeTitulo().isBlank() ||
                objeto.nomeTitulo().length() < 3 ||
                objeto.nomeTitulo().length() > 150) {
            throw new RegraNegocioException(
                    "O título deve ter entre 3 e 150 caracteres."
            );
        }

        if (objeto.descOcorrencia() == null ||
                objeto.descOcorrencia().isBlank() ||
                objeto.descOcorrencia().length() > 1000) {
            throw new RegraNegocioException(
                    "A descrição é obrigatória e deve ter no máximo 1000 caracteres."
            );
        }

        if (objeto.gravidade() == null ||
                objeto.gravidade() < 1 ||
                objeto.gravidade() > 5) {
            throw new RegraNegocioException(
                    "A gravidade deve estar entre 1 e 5."
            );
        }

        if (objeto.latitude() == null ||
                !Double.isFinite(objeto.latitude()) ||
                objeto.latitude() < -90 ||
                objeto.latitude() > 90) {
            throw new RegraNegocioException("Latitude inválida.");
        }

        if (objeto.longitude() == null ||
                !Double.isFinite(objeto.longitude()) ||
                objeto.longitude() < -180 ||
                objeto.longitude() > 180) {
            throw new RegraNegocioException("Longitude inválida.");
        }

        if (objeto.dataOcorrencia() == null ||
                objeto.dataOcorrencia().isAfter(LocalDateTime.now())) {
            throw new RegraNegocioException(
                    "A data da ocorrência é obrigatória e não pode ser futura."
            );
        }

        if (objeto.distrito() == null ||
                objeto.distrito().id() == null ||
                objeto.distrito().id() <= 0) {
            throw new RegraNegocioException(
                    "É necessário informar um distrito válido."
            );
        }

        if (objeto.categoria() == null ||
                objeto.categoria().id() == null ||
                objeto.categoria().id() <= 0) {
            throw new RegraNegocioException(
                    "É necessário informar uma categoria válida."
            );
        }

        // Busca a pessoa autenticada
        Pessoa pessoa = pessoaDAO.buscarPorId(idPessoa);

        if (pessoa == null) {
            throw new RecursoNaoEncontradoException(
                    "Pessoa não encontrada."
            );
        }

        // Busca o distrito
        Distrito distrito = distritoDAO.buscarPorId(
                objeto.distrito().id()
        );

        if (distrito == null) {
            throw new RecursoNaoEncontradoException(
                    "Distrito não encontrado."
            );
        }

        // Busca a categoria
        CategoriaOcorrencia categoria = categoriaDAO.buscarPorId(
                objeto.categoria().id()
        );

        if (categoria == null) {
            throw new RecursoNaoEncontradoException(
                    "Categoria não encontrada."
            );
        }

        // Cria a ocorrência
        Ocorrencia ocorrencia = new Ocorrencia();

        ocorrencia.setNomeTitulo(objeto.nomeTitulo());
        ocorrencia.setDescOcorrencia(objeto.descOcorrencia());
        ocorrencia.setGravidade(objeto.gravidade());
        ocorrencia.setLatitude(objeto.latitude());
        ocorrencia.setLongitude(objeto.longitude());
        ocorrencia.setDataOcorrencia(objeto.dataOcorrencia());

        // Dados definidos automaticamente
        ocorrencia.setOrigem("CIDADAO");
        ocorrencia.setStatus("ATIVA");
        ocorrencia.setDataPublicacao(LocalDateTime.now());
        ocorrencia.setDataAtualizacao(null);

        ocorrencia.setDistrito(distrito);
        ocorrencia.setCategoria(categoria);
        ocorrencia.setPessoa(pessoa);
        ocorrencia.setFonte(null);

        Ocorrencia cadastrada = dao.cadastrar(ocorrencia);

        carregarRelacionamentos(cadastrada);

        return OcorrenciaMapper.toDTO(cadastrada);
    }

    /**
     * Atualiza os dados de uma ocorrência existente.
     * Verifica se a ocorrência, o distrito, a categoria e a pessoa ou fonte
     * de dados existem, conforme a origem informada.
     *
     * @param id identificador da ocorrência que será atualizada
     * @param objeto novos dados da ocorrência
     * @return ocorrência atualizada em formato DTO
     * @throws RegraNegocioException se o ID ou os dados forem inválidos
     * @throws RecursoNaoEncontradoException se a ocorrência ou algum relacionamento não existir
     */
    public OcorrenciaDTO atualizar(Long id, OcorrenciaDTO objeto) {

        validarId(id);
        validarOcorrencia(objeto);

        if (dao.buscarPorId(id) == null) {
            throw new RecursoNaoEncontradoException(
                    "Ocorrência não encontrada."
            );
        }

        Distrito distrito = distritoDAO.buscarPorId(
                objeto.distrito().id()
        );

        if (distrito == null) {
            throw new RecursoNaoEncontradoException(
                    "Distrito não encontrado."
            );
        }

        CategoriaOcorrencia categoria = categoriaDAO.buscarPorId(
                objeto.categoria().id()
        );

        if (categoria == null) {
            throw new RecursoNaoEncontradoException(
                    "Categoria não encontrada."
            );
        }

        FonteDado fonte = null;
        Pessoa pessoa = null;

        if ("API".equals(objeto.origem())) {

            fonte = fonteDAO.buscarPorId(
                    objeto.fonte().id()
            );

            if (fonte == null) {
                throw new RecursoNaoEncontradoException(
                        "Fonte de dados não encontrada."
                );
            }

        } else {

            pessoa = pessoaDAO.buscarPorId(
                    objeto.pessoa().id()
            );

            if (pessoa == null) {
                throw new RecursoNaoEncontradoException(
                        "Pessoa não encontrada."
                );
            }
        }

        Ocorrencia ocorrencia = OcorrenciaMapper.toModel(objeto);

        ocorrencia.setDistrito(distrito);
        ocorrencia.setCategoria(categoria);
        ocorrencia.setFonte(fonte);
        ocorrencia.setPessoa(pessoa);

        Ocorrencia atualizada = dao.atualizar(id, ocorrencia);

        if (atualizada == null) {
            throw new RecursoNaoEncontradoException(
                    "Ocorrência não encontrada."
            );
        }

        carregarRelacionamentos(atualizada);

        return OcorrenciaMapper.toDTO(atualizada);
    }

    /**
     * Exclui uma ocorrência pelo seu ID.
     *
     * @param id identificador da ocorrência que será excluída
     * @return true se a ocorrência for excluída com sucesso
     * @throws RegraNegocioException se o ID for inválido
     * @throws RecursoNaoEncontradoException se a ocorrência não existir
     */
    public boolean deletar(Long id) {

        validarId(id);

        boolean deletada = dao.deletar(id);

        if (!deletada) {
            throw new RecursoNaoEncontradoException(
                    "Ocorrência não encontrada."
            );
        }

        return true;
    }

    /**
     * Carrega os dados completos dos relacionamentos de uma ocorrência.
     * Busca novamente os dados do distrito, da categoria, da fonte e da pessoa,
     * quando esses relacionamentos estiverem informados.
     *
     * @param ocorrencia ocorrência cujos relacionamentos serão carregados
     */
    private void carregarRelacionamentos(Ocorrencia ocorrencia) {

        if (ocorrencia.getDistrito() != null) {
            ocorrencia.setDistrito(
                    distritoDAO.buscarPorId(
                            ocorrencia.getDistrito().getId()
                    )
            );
        }

        if (ocorrencia.getCategoria() != null) {
            ocorrencia.setCategoria(
                    categoriaDAO.buscarPorId(
                            ocorrencia.getCategoria().getId()
                    )
            );
        }

        if (ocorrencia.getFonte() != null) {
            ocorrencia.setFonte(
                    fonteDAO.buscarPorId(
                            ocorrencia.getFonte().getId()
                    )
            );
        }

        if (ocorrencia.getPessoa() != null) {
            ocorrencia.setPessoa(
                    pessoaDAO.buscarPorId(
                            ocorrencia.getPessoa().getId()
                    )
            );
        }
    }

    /**
     * Verifica se o ID informado é válido.
     *
     * @param id identificador que será validado
     * @throws RegraNegocioException se o ID for nulo ou menor ou igual a zero
     */
    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new RegraNegocioException("ID inválido.");
        }
    }

    /**
     * Valida os dados de uma ocorrência antes do cadastro ou atualização.
     * Verifica título, gravidade, origem, status, coordenadas, data,
     * distrito, categoria e os relacionamentos obrigatórios conforme a origem.
     *
     * @param objeto dados da ocorrência que serão validados
     * @throws RegraNegocioException se os dados informados forem inválidos
     */
    private void validarOcorrencia(OcorrenciaDTO objeto) {

        if (objeto == null) {
            throw new RegraNegocioException(
                    "Ocorrência não informada."
            );
        }

        if (objeto.nomeTitulo() == null ||
                objeto.nomeTitulo().isBlank()) {
            throw new RegraNegocioException(
                    "O título da ocorrência é obrigatório."
            );
        }

        if (objeto.nomeTitulo().length() > 120) {
            throw new RegraNegocioException(
                    "O título deve ter no máximo 120 caracteres."
            );
        }

        if (objeto.gravidade() == null ||
                objeto.gravidade() < 1 ||
                objeto.gravidade() > 5) {
            throw new RegraNegocioException(
                    "A gravidade deve estar entre 1 e 5."
            );
        }

        if (objeto.origem() == null ||
                (!objeto.origem().equals("API") &&
                        !objeto.origem().equals("CIDADAO"))) {
            throw new RegraNegocioException(
                    "A origem deve ser API ou CIDADAO."
            );
        }

        if (objeto.status() != null &&
                !List.of("ATIVA", "EM_ANALISE", "RESOLVIDA", "EXPIRADA")
                        .contains(objeto.status())) {
            throw new RegraNegocioException("Status inválido.");
        }

        if (objeto.latitude() == null ||
                !Double.isFinite(objeto.latitude()) ||
                objeto.latitude() < -90 ||
                objeto.latitude() > 90) {
            throw new RegraNegocioException("Latitude inválida.");
        }

        if (objeto.longitude() == null ||
                !Double.isFinite(objeto.longitude()) ||
                objeto.longitude() < -180 ||
                objeto.longitude() > 180) {
            throw new RegraNegocioException("Longitude inválida.");
        }

        if (objeto.dataOcorrencia() == null) {
            throw new RegraNegocioException(
                    "A data da ocorrência é obrigatória."
            );
        }

        if (objeto.distrito() == null ||
                objeto.distrito().id() == null ||
                objeto.distrito().id() <= 0) {
            throw new RegraNegocioException(
                    "É necessário informar um distrito válido."
            );
        }

        if (objeto.categoria() == null ||
                objeto.categoria().id() == null ||
                objeto.categoria().id() <= 0) {
            throw new RegraNegocioException(
                    "É necessário informar uma categoria válida."
            );
        }

        if ("API".equals(objeto.origem())) {

            if (objeto.fonte() == null ||
                    objeto.fonte().id() == null ||
                    objeto.fonte().id() <= 0) {
                throw new RegraNegocioException(
                        "Ocorrências de API precisam de uma fonte válida."
                );
            }

            if (objeto.pessoa() != null) {
                throw new RegraNegocioException(
                        "Ocorrências de API não podem possuir uma pessoa."
                );
            }

        } else {

            if (objeto.pessoa() == null ||
                    objeto.pessoa().id() == null ||
                    objeto.pessoa().id() <= 0) {
                throw new RegraNegocioException(
                        "Ocorrências de cidadãos precisam de uma pessoa válida."
                );
            }

            if (objeto.fonte() != null) {
                throw new RegraNegocioException(
                        "Ocorrências de cidadãos não podem possuir uma fonte."
                );
            }
        }
    }
}
