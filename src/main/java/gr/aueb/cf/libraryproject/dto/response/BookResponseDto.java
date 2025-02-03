package gr.aueb.cf.libraryproject.dto.response;


import gr.aueb.cf.libraryproject.model.business.Author;
import gr.aueb.cf.libraryproject.model.business.User;
import gr.aueb.cf.libraryproject.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private BigInteger id;
    private String title;
    private String description;
    private Integer quantity;
    private Author author;
}
