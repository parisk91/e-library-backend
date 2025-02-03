package gr.aueb.cf.libraryproject.dto.request;

import gr.aueb.cf.libraryproject.model.business.Book;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthorInsertRequestDto {
    @NotNull
    @Length(min = 2, max = 50)
    private String firstname;
    @NotNull
    @Length(min = 2, max = 50)
    private String lastname;
    @Length(max = 1000)
    private String biography;
}
