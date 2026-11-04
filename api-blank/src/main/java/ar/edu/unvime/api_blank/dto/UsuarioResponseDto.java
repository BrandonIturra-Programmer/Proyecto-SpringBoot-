package ar.edu.unvime.api_blank.dto;

import ar.edu.unvime.api_blank.model.Rol;

public record UsuarioResponseDto(Long id, String email, Rol rol) {}