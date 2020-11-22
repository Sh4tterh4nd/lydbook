package ch.fhnw.webec.entity;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq_gen")
    @SequenceGenerator(name = "author_seq_gen", sequenceName = "author_sequence", initialValue = 10000)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "author")
    private List<Book> bookList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Long getId() {
        return id;
    }
}
