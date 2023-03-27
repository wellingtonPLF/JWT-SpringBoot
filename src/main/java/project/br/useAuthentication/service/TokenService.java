package project.br.useAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;
import project.br.useAuthentication.exception.BadRequestExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.TokenRepository;

@Service
public class TokenService {
	@Autowired
	private TokenRepository tokenRepository;
		
	public TokenJPA findById(Long id) {
		TokenJPA token = this.tokenRepository.findById(id).orElseThrow(
			() -> new NotFoundExceptionResult("The requested TokenId was not found."));
		return token;
	}
	
	public TokenJPA findByToken(String token) {
		TokenJPA jwtDB = this.tokenRepository.findByToken(token).orElseThrow(
			() -> new AuthenticationExceptionResponse(JwtType.INVALID_AT.toString())
		);
		return jwtDB;
	}
	
	@Transactional
	public void insertUpdate(TokenJPA token) {
		try {
			this.tokenRepository.save(token);
		}
		catch(Exception e) {
			throw new BadRequestExceptionResult("Token is Null");
		}
	}
	
	public void removeByUserID(Long id) {
		try {
			TokenJPA tokenID = this.tokenRepository.findByUserID(id).orElse(null);
			if (tokenID != null) {
				this.tokenRepository.deleteById(tokenID.getId());
			}
		}
		catch(Exception e) {
			throw new BadRequestExceptionResult("Token is Null");
		}
	}
	
	public void remove(Long id) {
		try {
			this.tokenRepository.deleteById(id);
		}
		catch(Exception e) {
			throw new NotFoundExceptionResult("The requested TokenId was not found.");
		}
	}
}
