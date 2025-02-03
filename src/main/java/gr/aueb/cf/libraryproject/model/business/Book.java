package gr.aueb.cf.libraryproject.model.business;
import lombok.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private BigInteger id;
    private String title;
    private String description;
    private Integer quantity;
    private Author author;
    //private List<User> users = new ArrayList<>();

}
