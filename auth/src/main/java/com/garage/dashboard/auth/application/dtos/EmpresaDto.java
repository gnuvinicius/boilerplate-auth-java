package com.garage.dashboard.auth.application.dtos;

import java.time.LocalDateTime;

import com.garage.dashboard.core.enums.Status;
import com.garage.dashboard.core.models.Endereco;

public record EmpresaDto(Long id, String nome,
        Endereco endereco,
        String cnpj,
        Status status,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm) {
}