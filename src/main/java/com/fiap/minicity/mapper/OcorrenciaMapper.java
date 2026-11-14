package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.OcorrenciaDTO;
import com.fiap.minicity.models.Ocorrencia;

public class OcorrenciaMapper {

        // Converte Model para DTO
        public static OcorrenciaDTO toDTO(Ocorrencia o) {
                if (o == null) {
                        return null;
                }

                return new OcorrenciaDTO(
                        o.getId(),
                        o.getNomeTitulo(),
                        o.getDescOcorrencia(),
                        o.getGravidade(),
                        o.getOrigem(),
                        o.getStatus(),
                        o.getLatitude(),
                        o.getLongitude(),
                        o.getDataOcorrencia(),
                        o.getDataPublicacao(),
                        o.getDataAtualizacao(),

                        DistritoMapper.toDTO(o.getDistrito()),
                        CategoriaOcorrenciaMapper.toDTO(o.getCategoria()),
                        FonteDadoMapper.toDTO(o.getFonte()),
                        PessoaMapper.toDTO(o.getPessoa()),

                        o.getIdExterno()
                );
        }

        // Converte DTO para Model
        public static Ocorrencia toModel(OcorrenciaDTO dto) {
                if (dto == null) {
                        return null;
                }

                Ocorrencia o = new Ocorrencia();
                o.setId(dto.id());
                o.setNomeTitulo(dto.nomeTitulo());
                o.setDescOcorrencia(dto.descOcorrencia());
                o.setGravidade(dto.gravidade());
                o.setOrigem(dto.origem());
                o.setStatus(dto.status());
                o.setLatitude(dto.latitude());
                o.setLongitude(dto.longitude());
                o.setDataOcorrencia(dto.dataOcorrencia());
                o.setDataPublicacao(dto.dataPublicacao());
                o.setDataAtualizacao(dto.dataAtualizacao());
                o.setIdExterno(dto.idExterno());

                // Os relacionamentos serão preenchidos pelo Service
                o.setDistrito(null);
                o.setCategoria(null);
                o.setFonte(null);
                o.setPessoa(null);

                return o;
        }
}