package project.br.useAuthentication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.br.useAuthentication.jpaModel.TokenJPA;

public interface TokenRepository extends JpaRepository<TokenJPA, Long>{
		
	@Query("SELECT t FROM TokenJPA t INNER JOIN UserJPA u "
			+ "ON t.user._id = u._id "
			+ "WHERE u._id = ?1 "
			+ "AND (t.expired = FALSE OR t.revoked = FALSE)")
	List<TokenJPA> findAllValidTokenByUser(Long id);

	Optional<TokenJPA> findByToken(String token);
}
