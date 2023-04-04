package project.br.useAuthentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.br.useAuthentication.jpaModel.AuthJPA;

public interface AuthRepository extends JpaRepository<AuthJPA, Long>{
	
	@Query("select a from AuthJPA a where a._user._id = ?1")
	Optional<AuthJPA> findByUserID(Long userID);
	
	public Optional<AuthJPA> findBy_email(String email);
	public Optional<AuthJPA> findBy_username(String email);
}
