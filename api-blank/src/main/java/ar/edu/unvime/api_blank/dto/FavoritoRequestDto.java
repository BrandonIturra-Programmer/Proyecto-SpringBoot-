package ar.edu.unvime.api_blank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FavoritoRequestDto {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "El ID de la lista es obligatorio") 
    private Long listaId;

    @NotBlank(message = "La nota personal no puede estar vacía")
    private String notaPersonal;

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getNotaPersonal() { return notaPersonal; }
    public void setNotaPersonal(String notaPersonal) { this.notaPersonal = notaPersonal; }

    public Long getListaId() { return listaId; }
    public void setListaId(Long listaId) { this.listaId = listaId; }
}