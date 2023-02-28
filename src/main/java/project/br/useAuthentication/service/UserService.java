package project.br.useAuthentication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public List<UserDTO> listAll() {
		List<UserJPA> result = this.userRepository.findAll();
		return result.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
	}
	
	public UserDTO pesquisarPorID(Long id) {
		return new UserDTO(this.userRepository.findById(id).orElse(null));
	}
	
	@Transactional
	public UserDTO inserirOuAtualizar(UserDTO user) {
		System.out.println(user);
		if(user.getName() != null) {
			this.userRepository.save(new UserJPA(user));
		}
		return user;
	}
	
	public void apagar(Long id) {	
		this.userRepository.deleteById(id);
	}
}
