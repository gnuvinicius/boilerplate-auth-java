package com.garage.auth.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.garage.auth.models.Empresa;

public interface IEmpresaRepository extends JpaRepository<Empresa, String> {

	@Query("SELECT e.email FROM Empresa e WHERE e.id = :id")
	String getEmailToSendEmail(String id);

}
