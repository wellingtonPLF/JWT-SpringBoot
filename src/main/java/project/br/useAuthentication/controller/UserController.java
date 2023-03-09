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
import project.br.useAuthentication.dtoModel.AuthDTO;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.service.AuthenticationService;
import project.br.useAuthentication.service.LogOutService;
import project.br.useAuthentication.service.UserService;

@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationService authService;
		
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/usuarios")
	public StatusResult<?> findAll() {
		return this.userService.listAll();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/usuarios/{id}")
	public StatusResult<?> findById(@PathVariable("id") Long id) {
		return this.userService.findById(id);
	}
	
	@PreAuthorize("permitAll()")
	@GetMapping("/usuarios/authentication")
	public StatusResult<?> authentication(@RequestBody AuthDTO auth) {
		return this.authService.authenticate(auth);
	}
	
	@PreAuthorize("permitAll()")
	@PostMapping("/usuarios")
	public StatusResult<?> insert(@Valid @RequestBody UserDTO user) {
		return this.authService.register(user);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PutMapping("/usuarios")
	public StatusResult<?> update(@Valid @RequestBody UserDTO user) {
		return this.userService.update(user);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/usuarios/{id}")
	public StatusResult<?> delete(@PathVariable("id") Long id) {	
		return this.userService.remove(id);
	}
}
