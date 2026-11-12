package com.fiap.minicity.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Distrito {
    private Long id;
    private String nomeDistrito;
    private String zona;
    private double latitude;
    private double longitude;
}
