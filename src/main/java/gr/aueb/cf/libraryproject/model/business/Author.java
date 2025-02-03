package gr.aueb.cf.libraryproject.model.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    private BigInteger id;
    private String firstname;
    private String lastname;
    private String biography;
    private List<Book> books = new ArrayList<>();




}
