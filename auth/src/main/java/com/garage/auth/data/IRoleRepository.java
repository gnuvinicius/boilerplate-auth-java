package com.garage.auth.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.garage.auth.models.Role;

public interface IRoleRepository extends JpaRepository<Role, Long> {

	@Query("SELECT r FROM Role r WHERE r.roleName IN :roleNameList")
	List<Role> findByRoleNameList(List<String> roleNameList);

	Optional<Role> findByRoleName(String roleName);

}
