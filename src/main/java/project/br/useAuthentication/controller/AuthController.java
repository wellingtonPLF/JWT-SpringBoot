package project.br.useAuthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.AuthJPA;
import project.br.useAuthentication.service.AuthenticationService;

@RestController
@RequestMapping("/")
public class AuthController {
	@Autowired
	private AuthenticationService authService;
	
	@PreAuthorize("permitAll()")
	@PostMapping("/usuarios/authentication")
	public StatusResult<?> authentication(@Valid @RequestBody AuthJPA auth) {
		return this.authService.authenticate(auth);
	}
		
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping("/usuarios/acceptAuth")
	public StatusResult<?> acceptAuth(@Valid @RequestBody AuthJPA auth) {
		return this.authService.acceptAuth(auth);
	}
	
	@PreAuthorize("permitAll()")
	@PostMapping("/usuarios/authInsert")
	public StatusResult<?> authInsert(@Valid @RequestBody AuthJPA auth) {
		return this.authService.authInsert(auth);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@PostMapping("/usuarios/authUpdate")
	public StatusResult<?> authUpdate(@Valid @RequestBody AuthJPA auth) {
		return this.authService.authUpdate(auth);
	}
	
	@PreAuthorize("permitAll()")
	@GetMapping("/usuarios/refresh")
	public StatusResult<?> refresh() {
		return this.authService.refresh();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@GetMapping("/usuarios/getUserID")
	public StatusResult<?> getAuthenticatedUserID() {
		return this.authService.getAuthenticatedUserID();
	}
		
	@PreAuthorize("permitAll()")
	@GetMapping("/usuarios/logout")
	public StatusResult<?> logout() {
		return this.authService.logout();
	}
}
