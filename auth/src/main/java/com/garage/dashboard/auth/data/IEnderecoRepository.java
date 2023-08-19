package com.garage.dashboard.auth.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.dashboard.core.models.Endereco;

public interface IEnderecoRepository extends JpaRepository<Endereco, String> {

}
