DROP TABLE IF EXISTS news;
CREATE TABLE news
(
    id BIGSERIAL PRIMARY KEY,
    time TIME NOT NULL,
    title VARCHAR(50) NOT NULL UNIQUE,
    text TEXT NOT NULL
);

DROP TABLE IF EXISTS comments;
CREATE TABLE comments
(
    id BIGSERIAL PRIMARY KEY,
    time TIME NOT NULL,
    text TEXT NOT NULL,
    username VARCHAR(50),
    news_id INT NOT NULL,
    FOREIGN KEY (news_id) REFERENCES news(id)
);

INSERT INTO news VALUES (1, '08:00:00', 'political news', 'Text');

INSERT INTO comments VALUES (1, '08:00:00', 'Text1', 'user1', 1);