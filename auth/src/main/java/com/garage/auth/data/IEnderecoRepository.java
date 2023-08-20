package com.garage.auth.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage.auth.models.Endereco;

public interface IEnderecoRepository extends JpaRepository<Endereco, String> {

}
