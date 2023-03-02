package project.br.useAuthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.service.UserService;

@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/usuarios")
	public StatusResult<?> findAll() {
		return this.userService.listAll();
	}
	
	@GetMapping("/usuarios/{id}")
	public StatusResult<?> findById(@PathVariable("id") Long id) {
		return this.userService.pesquisarPorID(id);
	}
	
	@PostMapping("/usuarios")
	public StatusResult<?> insert(@Valid @RequestBody UserDTO user) {
		return this.userService.inserirOuAtualizar(user);
	}
	
	@PutMapping("/usuarios")
	public StatusResult<?> update(@Valid @RequestBody UserDTO user) {
		return this.userService.inserirOuAtualizar(user);
	}
	
	@DeleteMapping("/usuarios/{id}")
	public StatusResult<?> delete(@PathVariable("id") Long id) {	
		return this.userService.apagar(id);
	}
}
