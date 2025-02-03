package gr.aueb.cf.libraryproject.repository;

import gr.aueb.cf.libraryproject.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, BigInteger> {
    UserEntity findUserEntityByUsername(String username);
    UserEntity findUserEntityByEmail(String email);
    UserEntity findUserEntityById(BigInteger id);
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    UserEntity getUserByEmail(@Param("email") String email); // Returns User or null


//    @Query(value = "SELECT id from users", nativeQuery = true)
//    UserEntity findUserEntityById1(BigInteger id);
}
