package gr.aueb.cf.libraryproject.repository;

import gr.aueb.cf.libraryproject.model.business.Book;
import gr.aueb.cf.libraryproject.model.entity.AuthorEntity;
import gr.aueb.cf.libraryproject.model.entity.BookEntity;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, BigInteger> {
    List<BookEntity> findBookEntitiesByAuthorId(BigInteger authorId);

    void deleteBookEntitiesByAuthorId(BigInteger authorId);
    BookEntity findBookEntityById(BigInteger id);
    BookEntity findBookEntityByTitleAndAuthor(String title, AuthorEntity authorEntity);
}
