package ar.edu.unvime.api_blank.dto;

import jakarta.validation.constraints.NotBlank;

public class ListaRequestDto {

    @NotBlank(message = "El nombre de la lista es obligatorio")
    private String nombre;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}