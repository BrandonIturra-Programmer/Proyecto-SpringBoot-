package ar.edu.unvime.api_blank.dto;

import jakarta.validation.constraints.NotNull;

public class MoverFavoritosRequestDto {

    @NotNull(message = "El ID de la lista destino es obligatorio")
    private Long destinoId;

    public Long getDestinoId() { return destinoId; }
    public void setDestinoId(Long destinoId) { this.destinoId = destinoId; }
}