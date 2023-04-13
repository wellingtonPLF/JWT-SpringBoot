package project.br.useAuthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.service.UserService;

@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private UserService userService;
		
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/usuarios")
	public StatusResult<?> findAll() {
		return this.userService.listAll();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/usuarios/getUser")
	public StatusResult<?> getAuthenticatedUser() {
		return this.userService.getAuthenticatedUser();
	}
	
	@PreAuthorize("permitAll()")
	@PostMapping("/usuarios")
	public StatusResult<?> insert(@Valid @RequestBody UserJPA user) {
		return this.userService.insert(user);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PutMapping("/usuarios")
	public StatusResult<?> update(@Valid @RequestBody UserJPA user) {
		return this.userService.update(user);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@DeleteMapping("/usuarios/{id}")
	public StatusResult<?> delete(@PathVariable("id") Long id) {	
		return this.userService.remove(id);
	}
}
