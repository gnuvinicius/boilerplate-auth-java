package com.garage.dashboard.auth.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.garage.dashboard.auth.data.IUserRepository;
import com.garage.dashboard.core.enums.Status;

@Service
public class UserService implements UserDetailsService {

  private static final String EMAIL_NAO_ENCONTRADO = "e-mail não encontrado";

  @Autowired
  private IUserRepository userRepository;

  public UserService(IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository
        .buscaPorEmail(email, Status.ATIVO)
        .orElseThrow(() -> new UsernameNotFoundException(EMAIL_NAO_ENCONTRADO));
  }

}
