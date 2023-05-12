package ru.clevertec.kalustau.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing the news in the database
 * @author Dzmitry Kalustau
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "title")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "news")
public class News implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime time;

    private String title;

    private String text;

    @Column(name = "username")
    private String userName;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy="news")
    @JsonIgnore
    private List<Comment> comments;

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setNews(this);
    }

    public void removeComment(long commentId) {
        Comment comment = this.comments.stream().filter(c -> c.getId() == commentId).findFirst().orElse(null);
        if (Objects.nonNull(comment)) {
            this.comments.remove(comment);
            comment.setNews(null);
        }
    }

}
