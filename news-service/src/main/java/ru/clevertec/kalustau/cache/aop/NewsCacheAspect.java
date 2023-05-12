package ru.clevertec.kalustau.cache.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.cache.Cache;
import ru.clevertec.kalustau.model.News;

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
public class NewsCacheAspect {

    private final Cache<News> cache;

    /**
     * Advice at the method level (find by id) intercepting control for interacting with the cache
     * @param joinPoint The join point that represents the method execution.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("execution(* ru.clevertec.kalustau.repository.NewsRepository.findById(..))")
    public Object findByIdAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        Optional<News> newsFromCacheData = cache.getById(id);
        if (newsFromCacheData.isEmpty()) {
            Optional<News> newsFromRepositoryData = (Optional<News>) joinPoint.proceed();
            newsFromRepositoryData.ifPresent(cache::save);
            return newsFromRepositoryData;
        }
        return newsFromCacheData;
    }

    /**
     * Advice at the method level (save or update) intercepting control for interacting with the cache
     * @param joinPoint The join point that represents the method execution.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("execution(* ru.clevertec.kalustau.repository.NewsRepository.save(..))")
    public Object saveOrUpdateAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        News news = (News) joinPoint.getArgs()[0];
        if (Objects.nonNull(news.getId())) {
            News updatedNews = (News) joinPoint.proceed();
            cache.update(news);
            return updatedNews;
        }
        else {
            News createdNews = (News) joinPoint.proceed();
            cache.save(createdNews);
            return createdNews;
        }
    }

    /**
     * Advice at the method level (delete) intercepting control for interacting with the cache
     * @param joinPoint The join point that represents the method execution.
     * @return The cached value or the result of the method execution.
     * @throws Throwable If an exception occurs during method execution.
     */
    @Around("execution(* ru.clevertec.kalustau.repository.NewsRepository.deleteById(..))")
    public void deleteAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        joinPoint.proceed();
        cache.delete(id);
    }

}
