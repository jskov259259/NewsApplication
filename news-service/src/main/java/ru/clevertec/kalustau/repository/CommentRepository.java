package ru.clevertec.kalustau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.clevertec.kalustau.model.Comment;

/**
 * Repository interface for Comment entity.
 * Provides methods for interacting with the database
 * @author Dzmitry Kalustau
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    /**
     * Returns a list of comments for the news with the given id
     * @param newsId id of the news.
     * @param paging The Pageable object to use for pagination.
     * @return A list of comments for the specified news, as specified by the given Pageable object.
     */
    Page<Comment> findAllByNewsId(Long newsId, Pageable paging);
}
