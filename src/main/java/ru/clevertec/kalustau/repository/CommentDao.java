package ru.clevertec.kalustau.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.kalustau.model.Comment;

public interface CommentDao extends JpaRepository<Comment, Long> {
}
