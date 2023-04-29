package ru.clevertec.kalustau.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.kalustau.cache.Cache;
import ru.clevertec.kalustau.model.News;

import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class NewsCacheAspect {

    private final Cache<News> cache;

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

    @Around("execution(* ru.clevertec.kalustau.repository.NewsRepository.deleteById(..))")
    public void deleteAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        joinPoint.proceed();
        cache.delete(id);
    }

}
