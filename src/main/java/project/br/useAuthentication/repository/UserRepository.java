package project.br.useAuthentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import project.br.useAuthentication.jpaModel.UserJPA;

public interface UserRepository extends JpaRepository<UserJPA, Long> {
	
	public UserJPA findBy_username(String username);
	public Optional<UserJPA> findBy_email(String email);
	
	//@Query("SELECT u FROM UserJPA u WHERE u._username = ?1")
	//UserJPA getUserByName(String name);
}
