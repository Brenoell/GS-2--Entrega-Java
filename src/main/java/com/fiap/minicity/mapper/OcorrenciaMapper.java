
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.OcorrenciaDTO;
import com.fiap.minicity.models.Ocorrencia;

public class OcorrenciaMapper {

    // Converte Model para DTO
    public static OcorrenciaDTO toDTO(Ocorrencia ocorrencia) {

        if (ocorrencia == null) {
            return null;
        }

        return new OcorrenciaDTO(
                ocorrencia.getId(),
                ocorrencia.getNomeTitulo(),
                ocorrencia.getDescOcorrencia(),
                ocorrencia.getGravidade(),
                ocorrencia.getOrigem(),
                ocorrencia.getStatus(),
                ocorrencia.getLatitude(),
                ocorrencia.getLongitude(),
                ocorrencia.getDataOcorrencia(),
                ocorrencia.getDataPublicacao(),
                ocorrencia.getDataAtualizacao(),

                DistritoMapper.toDTO(ocorrencia.getDistrito()),
                CategoriaOcorrenciaMapper.toDTO(ocorrencia.getCategoria()),
                FonteDadoMapper.toDTO(ocorrencia.getFonte()),
                PessoaMapper.toDTO(ocorrencia.getPessoa()),

                ocorrencia.getIdExterno()
        );
    }

    // Converte DTO para Model
    public static Ocorrencia toModel(OcorrenciaDTO dto) {

        if (dto == null) {
            return null;
        }

        Ocorrencia ocorrencia = new Ocorrencia();

        ocorrencia.setId(dto.id());
        ocorrencia.setNomeTitulo(dto.nomeTitulo());
        ocorrencia.setDescOcorrencia(dto.descOcorrencia());
        ocorrencia.setGravidade(dto.gravidade());
        ocorrencia.setOrigem(dto.origem());
        ocorrencia.setStatus(dto.status());

        ocorrencia.setLatitude(dto.latitude());
        ocorrencia.setLongitude(dto.longitude());

        ocorrencia.setDataOcorrencia(dto.dataOcorrencia());
        ocorrencia.setDataPublicacao(dto.dataPublicacao());
        ocorrencia.setDataAtualizacao(dto.dataAtualizacao());

        ocorrencia.setDistrito(DistritoMapper.toModel(dto.distrito()));
        ocorrencia.setCategoria(CategoriaOcorrenciaMapper.toModel(dto.categoria()));
        ocorrencia.setFonte(FonteDadoMapper.toModel(dto.fonte()));
        ocorrencia.setPessoa(PessoaMapper.toModel(dto.pessoa()));

        ocorrencia.setIdExterno(dto.idExterno());

        return ocorrencia;
    }
}
