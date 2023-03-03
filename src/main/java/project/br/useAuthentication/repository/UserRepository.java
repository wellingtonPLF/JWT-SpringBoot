package project.br.useAuthentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.br.useAuthentication.jpaModel.UserJPA;

public interface UserRepository extends JpaRepository<UserJPA, Long> {
	
	public UserJPA findBy_name(String name);
	public UserJPA findBy_email(String email);
	
	//@Query("SELECT u FROM UserJPA u WHERE u._name = ?1")
	//UserJPA getUserByName(String name);
}
