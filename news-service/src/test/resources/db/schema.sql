DROP TABLE IF EXISTS news;
CREATE TABLE news
(
    id BIGSERIAL PRIMARY KEY,
    time TIME NOT NULL,
    title VARCHAR(255) NOT NULL UNIQUE,
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
