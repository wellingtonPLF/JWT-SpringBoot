package project.br.useAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import project.br.useAuthentication.dtoModel.TokenDTO;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.TokenRepository;

public class TokenService {
	@Autowired
	private TokenRepository TokenRepository;
		
	public StatusResult<?> findById(Long id) {
		TokenDTO Token = new TokenDTO(this.TokenRepository.findById(id).orElseThrow(() -> 
			new NotFoundExceptionResult("The requested Id was not found.")));
		return new StatusResult<TokenDTO>(HttpStatus.OK.value(), Token);
	}
	
	@Transactional
	public void insertUpdate(TokenJPA token) {
		this.TokenRepository.save(token);
	}
	
	public void remove(Long id) {
		try {
			this.TokenRepository.deleteById(id);
		}
		catch(Exception e) {
			throw new NotFoundExceptionResult("The requested TokenId was not found.");
		}
	}
}
