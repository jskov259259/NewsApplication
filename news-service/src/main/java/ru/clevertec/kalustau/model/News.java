package ru.clevertec.kalustau.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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

    private LocalDateTime time;

    private String title;

    private String text;

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
