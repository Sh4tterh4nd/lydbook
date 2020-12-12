package ch.fhnw.webec.entity;


import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "progress",uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","book_id"}))
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "progress_seq_gen")
    @SequenceGenerator(name = "progress_seq_gen", sequenceName = "progress_sequence", initialValue = 10000)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer progress;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedtime;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Date getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(Date updatedtime) {
        this.updatedtime = updatedtime;
    }
}
