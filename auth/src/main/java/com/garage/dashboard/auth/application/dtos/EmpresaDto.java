package com.garage.dashboard.auth.application.dtos;

import java.time.LocalDateTime;

import com.garage.dashboard.auth.models.Endereco;
import com.garage.dashboard.core.enums.Status;

public record EmpresaDto(Long id, String nome,
        Endereco endereco,
        String cnpj,
        Status status,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm) {
}