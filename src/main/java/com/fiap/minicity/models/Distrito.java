package com.fiap.minicity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um distrito da cidade de São Paulo,
 * armazenando seu nome, zona e coordenadas geográficas.
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Distrito {

    /** Identificador único do distrito no sistema. */
    private Long id;

    /** Nome do distrito. */
    private String nomeDistrito;

    /** Zona da cidade onde o distrito está localizado. */
    private String zona;

    /** Latitude que representa a localização geográfica do distrito. */
    private double latitude;

    /** Longitude que representa a localização geográfica do distrito. */
    private double longitude;
}
