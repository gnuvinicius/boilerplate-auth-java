package com.garage.auth.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.garage.auth.models.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${jwt.expiration}")
	private String expiration;

	@Value("${jwt.secret}")
	private String secret;

	public String buildToken(Authentication authenticate) {

		Usuario user = (Usuario) authenticate.getPrincipal();
		var today = new Date();
		Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));

		Claims claims = Jwts.claims().setSubject(user.getId().toString());
		claims.setIssuedAt(today);
		claims.setExpiration(expirationDate);
		claims.setIssuer("dashboard api");
		claims.put("name", user.getNome());
		claims.put("email", user.getEmail());
		claims.put("roles", user.getRoles());
		if (user.getEmpresa() != null) {
			claims.put("empresa", user.getEmpresa().getId());
		}

		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();

		return token;
	}

	public void isTokenExpired(String token) throws ExpiredJwtException {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
		} catch (ExpiredJwtException e) {
			throw new ExpiredJwtException(null, null, token, e);
		}

	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getUserIdByToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

}
