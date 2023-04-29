package ru.clevertec.kalustau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.kalustau.model.Comment;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByNewsId(Long newsId, Pageable paging);
}
