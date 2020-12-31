package com.lydbook.audiobook.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_seq_gen")
    @SequenceGenerator(name = "series_seq_gen", sequenceName = "series_sequence", initialValue = 10000)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "series", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Book> books;


    public Series() {
    }

    public Series(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Series{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}
