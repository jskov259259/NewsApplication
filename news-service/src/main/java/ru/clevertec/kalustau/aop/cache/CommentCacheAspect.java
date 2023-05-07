package ru.clevertec.kalustau.aop.cache;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.cache.Cache;
import ru.clevertec.kalustau.model.Comment;

import java.util.Objects;
import java.util.Optional;

/**
 * Aspect that implements logic for working with the cache
 * @author Dzmitry Kalustau
 */
@Aspect
@Component
@RequiredArgsConstructor
@Profile("custom-cache")
public class CommentCacheAspect {

    private final Cache<Comment> cache;

    /**
     * Advice at the method level (find by id) intercepting control for interacting with the cache
     * @param joinPoint The join point that represents the method execution.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("execution(* ru.clevertec.kalustau.repository.CommentRepository.findById(..))")
    public Object findByIdAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        Optional<Comment> commentFromCacheData = cache.getById(id);
        if (commentFromCacheData.isEmpty()) {
            Optional<Comment> commentFromRepositoryData = (Optional<Comment>) joinPoint.proceed();
            commentFromRepositoryData.ifPresent(cache::save);
            return commentFromRepositoryData;
        }
        return commentFromCacheData;
    }

    /**
     * Advice at the method level (save or update) intercepting control for interacting with the cache
     * @param joinPoint The join point that represents the method execution.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("execution(* ru.clevertec.kalustau.repository.CommentRepository.save(..))")
    public Object saveOrUpdateAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Comment comment = (Comment) joinPoint.getArgs()[0];
        if (Objects.nonNull(comment.getId())) {
            Comment updatedComment = (Comment) joinPoint.proceed();
            cache.update(comment);
            return updatedComment;
        }
        else {
            Comment createdComment = (Comment) joinPoint.proceed();
            cache.save(createdComment);
            return createdComment;
        }
    }

    /**
     * Advice at the method level (delete) intercepting control for interacting with the cache
     * @param joinPoint The join point that represents the method execution.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("execution(* ru.clevertec.kalustau.repository.CommentRepository.deleteById(..))")
    public void deleteAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        joinPoint.proceed();
        cache.delete(id);
    }

}
