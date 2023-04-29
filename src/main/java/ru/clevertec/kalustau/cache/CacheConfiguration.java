package ru.clevertec.kalustau.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.kalustau.cache.comment.CommentCacheLFU;
import ru.clevertec.kalustau.cache.comment.CommentCacheLRU;
import ru.clevertec.kalustau.cache.news.NewsCacheLFU;
import ru.clevertec.kalustau.cache.news.NewsCacheLRU;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.model.News;

@Configuration
public class CacheConfiguration {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.size}")
    private int maxSize;

    @Bean
    public Cache<News> getNewsCache() {
        if ("LFU".equals(algorithm)) return new NewsCacheLFU(maxSize);
        return new NewsCacheLRU(maxSize);
    }

    @Bean
    public Cache<Comment> getCommentCache() {
        if ("LFU".equals(algorithm)) return new CommentCacheLFU(maxSize);
        return new CommentCacheLRU(maxSize);
    }

}
