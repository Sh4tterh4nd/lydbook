package ch.fhnw.webec.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_seq_gen")
    @SequenceGenerator(name = "tag_seq_gen", sequenceName = "tag_sequence", initialValue = 10000)
    private Long id;

    @Column(unique = true)
    private String name;
    private boolean removable;


    @ManyToMany(mappedBy = "tags")
    private Set<Book> books;

    public Tag() {
    }

    public Tag(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public Tag(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }
}