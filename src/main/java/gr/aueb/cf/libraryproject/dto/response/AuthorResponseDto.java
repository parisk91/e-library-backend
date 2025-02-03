package gr.aueb.cf.libraryproject.dto.response;

import gr.aueb.cf.libraryproject.model.business.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthorResponseDto {

    private BigInteger id;
    private String firstname;
    private String lastname;
    private String biography;
    private List<Book> books;
}
