package ch.fhnw.webec.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

/**
 * Tag.
 */
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_seq_gen")
    @SequenceGenerator(name = "tag_seq_gen", sequenceName = "tag_sequence", initialValue = 10001)
    private Long id;

    @Column(unique = true)
    private String name;

    private boolean removable = true;

    @JsonBackReference
    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    private Set<Book> books;

    /**
     * Instantiates a new Tag.
     */
    public Tag() {
    }


    /**
     * Instantiates a new Tag with name and id.
     *
     * @param name the name
     * @param id   the id
     */
    public Tag(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Instantiates a new Tag with name id and removable.
     *
     * @param name      the name
     * @param id        the id
     * @param removable the removable
     */
    public Tag(String name, Long id, boolean removable) {
        this.name = name;
        this.id = id;
        this.removable =removable;
    }

    /**
     * Instantiates a new Tag with name.
     *
     * @param name the name
     */
    public Tag(String name) {
        this.name = name;
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
     * Is removable boolean.
     *
     * @return the boolean
     */
    public boolean isRemovable() {
        return removable;
    }

    /**
     * Sets removable.
     *
     * @param removable the removable
     */
    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    /**
     * Gets books.
     *
     * @return the books
     */
    public Set<Book> getBooks() {
        return books;
    }

    /**
     * Sets books.
     *
     * @param books the books
     */
    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
