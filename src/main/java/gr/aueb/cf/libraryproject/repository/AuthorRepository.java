package gr.aueb.cf.libraryproject.repository;

import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.model.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, BigInteger> {
    List<AuthorEntity> findAuthorByLastname(String lastname);
    AuthorEntity findAuthorById(BigInteger id);
    Optional<AuthorEntity> findByFirstnameAndLastname(String firstname, String lastname);


}
