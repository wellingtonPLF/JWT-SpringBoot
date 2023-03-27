package project.br.useAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.TokenRepository;

@Service
public class TokenService {
	@Autowired
	private TokenRepository TokenRepository;
		
	public TokenJPA findById(Long id) {
		TokenJPA token = this.TokenRepository.findById(id).orElseThrow(() -> 
			new NotFoundExceptionResult("The requested Id was not found."));
		return token;
	}
	
	@Transactional
	public void insertUpdate(TokenJPA token) {
		this.TokenRepository.save(token);
	}
	
	public void removeByUserID(Long id) {
		TokenJPA tokenID = this.TokenRepository.findByUserID(id).orElse(null);
		this.TokenRepository.deleteById(tokenID.getId());
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
