package gr.aueb.cf.libraryproject.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigInteger;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateRequestDto {
    @Length(min = 2, max = 50)
    private String title;
    private String description;
    private Integer quantity;
    private BigInteger authorId;
}
