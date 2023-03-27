package project.br.useAuthentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.br.useAuthentication.jpaModel.TokenJPA;

public interface TokenRepository extends JpaRepository<TokenJPA, Long>{
		
	@Query("select t from TokenJPA t where t._user._id = ?1")
	Optional<TokenJPA> findByUserID(Long userID);
	
	Optional<TokenJPA> findBy_token(String token);
}
