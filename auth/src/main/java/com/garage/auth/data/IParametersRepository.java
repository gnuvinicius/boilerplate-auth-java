package com.garage.auth.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.garage.auth.models.Parameters;

public interface IParametersRepository extends JpaRepository<Parameters, Long> {

	@Query(value = "SELECT p.email_not_response FROM TB_PARAMETERS p WHERE p.status = 1 ORDER BY p.updated_at DESC LIMIT 1", nativeQuery = true)
	Optional<String> findLastEmailNotResponseActive();

}
