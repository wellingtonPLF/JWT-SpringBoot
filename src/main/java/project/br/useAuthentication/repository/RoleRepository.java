package project.br.useAuthentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.br.useAuthentication.jpaModel.RoleJPA;

public interface RoleRepository extends JpaRepository<RoleJPA, Long>{

}
