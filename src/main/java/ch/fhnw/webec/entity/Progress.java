package ch.fhnw.webec.entity;


import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Progress.
 */
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

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets book.
     *
     * @return the book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets book.
     *
     * @param book the book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * Sets progress.
     *
     * @param progress the progress
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     * Gets updatedtime.
     *
     * @return the updatedtime
     */
    public Date getUpdatedtime() {
        return updatedtime;
    }

    /**
     * Sets updatedtime.
     *
     * @param updatedtime the updatedtime
     */
    public void setUpdatedtime(Date updatedtime) {
        this.updatedtime = updatedtime;
    }
}
