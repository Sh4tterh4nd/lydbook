package com.lydbook.audiobook.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

/**
 * Author.
 */
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq_gen")
    @SequenceGenerator(name = "author_seq_gen", sequenceName = "author_sequence", initialValue = 10000)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Book> bookList;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets book list.
     *
     * @return the book list
     */
    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * Sets book list.
     *
     * @param bookList the book list
     */
    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    /**
     * Add book.
     *
     * @param book the book
     */
    public void addBook(Book book){
        this.bookList.add(book);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return name.equals(author.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
