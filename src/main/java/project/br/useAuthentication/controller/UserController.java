package project.br.useAuthentication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.service.UserService;

@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/usuarios")
	public List<UserDTO> findAll() {
		return this.userService.listAll();
	}
	
	@GetMapping("/usuarios/{id}")
	public UserDTO findById(@PathVariable("id") Long id) {
		return this.userService.pesquisarPorID(id);
	}
	
	@PostMapping("/usuarios")
	public UserDTO insert(@RequestBody UserDTO user) {
		return this.userService.inserirOuAtualizar(user);
	}
	
	@PutMapping("/usuarios")
	public UserDTO update(@RequestBody UserDTO user) {
		return this.userService.inserirOuAtualizar(user);
	}
	
	@DeleteMapping("/usuarios/{id}")
	public void delete(@PathVariable("id") Long id) {	
		this.userService.apagar(id);
	}
}
