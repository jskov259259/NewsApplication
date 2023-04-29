package ru.clevertec.kalustau.aop.cache;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.cache.Cache;
import ru.clevertec.kalustau.model.Comment;

import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class CommentCacheAspect {

    private final Cache<Comment> cache;

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

    @Around("execution(* ru.clevertec.kalustau.repository.CommentRepository.deleteById(..))")
    public void deleteAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        joinPoint.proceed();
        cache.delete(id);
    }

}
