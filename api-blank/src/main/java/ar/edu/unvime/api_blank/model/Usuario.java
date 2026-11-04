package ar.edu.unvime.api_blank.model;

public record Usuario(Long id, String email, String passwordHash, Rol rol) {}