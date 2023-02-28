package project.br.useAuthentication.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import project.br.useAuthentication.jpaModel.UserJPA;

public interface UserRepository extends JpaRepository<UserJPA, Long> {
	
	public UserJPA findByName(String name);
}
