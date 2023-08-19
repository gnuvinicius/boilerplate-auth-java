package com.garage.dashboard.auth.data;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.garage.dashboard.auth.models.Empresa;
import com.garage.dashboard.auth.models.Usuario;
import com.garage.dashboard.core.enums.Status;

@Transactional
public interface IUserRepository extends JpaRepository<Usuario, String> {

	@Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.status = :status")
	Optional<Usuario> buscaPorEmail(String email, Status status);
	
	@Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.status = :status AND u.empresa = :empresa")
	Optional<Usuario> buscaPorEmailAndStatus(String email, Status status, Empresa empresa);

	@Query("SELECT u FROM Usuario u WHERE u.status = :status AND u.empresa = :empresa")
	List<Usuario> buscaPorEmpresa(Status status, Empresa empresa);

}