package project.br.useAuthentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.br.useAuthentication.jpaModel.UserJPA;

public interface UserRepository extends JpaRepository<UserJPA, Long> {
	
	public Optional<UserJPA> findBy_auth_id(Long id);
}
