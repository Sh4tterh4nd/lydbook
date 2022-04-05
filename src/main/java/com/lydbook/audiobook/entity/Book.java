package com.lydbook.audiobook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Book.
 */
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq_gen")
    @SequenceGenerator(name = "book_seq_gen", sequenceName = "book_sequence", initialValue = 10000)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @JsonIgnore
    @Column(name = "data_name")
    private String dataName;

    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @CreationTimestamp
    @Column(name = "upload_date")
    private Date uploadDate;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @Column(name = "book_number")
    private Double bookNumber;

    /**
     * Gets author.
     *
     * @return the author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * Gets data name.
     *
     * @return the data name
     */
    public String getDataName() {
        return dataName;
    }

    /**
     * Sets data name.
     *
     * @param filename the filename
     */
    public void setDataName(String filename) {
        this.dataName = filename;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets tags.
     *
     * @return the tags
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Add tag.
     *
     * @param tag the tag
     */
    public void addTag(Tag tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }


    public Double getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(Double bookNumber) {
        this.bookNumber = bookNumber;
    }

    /**
     * Gets upload date.
     *
     * @return the upload date
     */
    public Date getUploadDate() {
        return uploadDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author=" + author +
                ", filename='" + dataName + '\'' +
                ", title='" + title + '\'' +
                ", tags=" + tags +
                '}';
    }
}
