package project.br.useAuthentication.jpaModel;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import project.br.useAuthentication.enumState.RoleName;

@Entity
@Table(name = "roles")
public class RoleJPA implements GrantedAuthority{
	private final static long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="role_ID")
	private Long _id;
	
	@Enumerated(EnumType.STRING)
	@NotNull(message="RoleName: Campo obrigatório")
	@Column(name="roleName", unique=true)
	private RoleName _roleName;
	
	public String getAuthority() {
		return this._roleName.toString();
	}

	public Long getId() {
		return this._id;
	}

	public void setId(Long id) {
		this._id = id;
	}

	public RoleName getRoleName() {
		return this._roleName;
	}

	public void setRoleName(RoleName roleName) {
		this._roleName = roleName;
	}
}
