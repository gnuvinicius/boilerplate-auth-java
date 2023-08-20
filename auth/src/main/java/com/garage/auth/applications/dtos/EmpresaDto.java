package com.garage.auth.applications.dtos;

import java.time.LocalDateTime;

import com.garage.auth.models.Endereco;
import com.garage.core.enums.Status;

public record EmpresaDto(Long id, String nome,
        Endereco endereco,
        String cnpj,
        Status status,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm) {
}