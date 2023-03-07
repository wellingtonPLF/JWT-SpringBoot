package project.br.useAuthentication.dtoModel;

import java.util.List;

import project.br.useAuthentication.enumState.RoleName;

public class RoleDTO {

	private Long id;
	private RoleName roleName;
	private List<UserDTO> usuarios;
	
	public List<UserDTO> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UserDTO> usuarios) {
		this.usuarios = usuarios;
	}

	public String getAuthority() {
		return this.roleName.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}
}
