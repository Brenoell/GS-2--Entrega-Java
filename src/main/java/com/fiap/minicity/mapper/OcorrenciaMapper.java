
package com.fiap.minicity.mapper;

import com.fiap.minicity.dto.OcorrenciaDTO;
import com.fiap.minicity.models.Ocorrencia;
import org.modelmapper.ModelMapper;

public class OcorrenciaMapper {

        private static final ModelMapper mapper = new ModelMapper();

        // Converte Model para DTO
        public static OcorrenciaDTO toDTO(Ocorrencia ocorrencia) {

                if (ocorrencia == null) {
                        return null;
                }

                OcorrenciaDTO dto = mapper.map(ocorrencia, OcorrenciaDTO.class);

                return new OcorrenciaDTO(
                        dto.id(),
                        dto.nomeTitulo(),
                        dto.descOcorrencia(),
                        dto.gravidade(),
                        dto.origem(),
                        dto.status(),
                        dto.latitude(),
                        dto.longitude(),
                        dto.dataOcorrencia(),
                        dto.dataPublicacao(),
                        dto.dataAtualizacao(),

                        DistritoMapper.toDTO(ocorrencia.getDistrito()),
                        CategoriaOcorrenciaMapper.toDTO(ocorrencia.getCategoria()),
                        FonteDadoMapper.toDTO(ocorrencia.getFonte()),
                        PessoaMapper.toDTO(ocorrencia.getPessoa()),

                        dto.idExterno()
                );
        }

        // Converte DTO para Model
        public static Ocorrencia toModel(OcorrenciaDTO dto) {

                if (dto == null) {
                        return null;
                }

                Ocorrencia ocorrencia = mapper.map(dto, Ocorrencia.class);

                // Os relacionamentos serão preenchidos pelo Service
                ocorrencia.setDistrito(null);
                ocorrencia.setCategoria(null);
                ocorrencia.setFonte(null);
                ocorrencia.setPessoa(null);

                return ocorrencia;
        }
}
