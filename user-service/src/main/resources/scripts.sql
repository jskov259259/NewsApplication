CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);

CREATE TABLE roles
(
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE users_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE (user_id, role_id)
);


INSERT INTO public.users(id, login, password, first_name, last_name)
VALUES (1, 'anton', '1234', 'anton', 'Ivanov');

INSERT INTO public.users(id, login, password, first_name, last_name)
VALUES (2, 'ivan', '5', 'ivan', 'Ivanov');

INSERT INTO public.users(id, login, password, first_name, last_name)
VALUES (3, 'grisha', '123456', 'grisha', 'Petrov');


INSERT INTO public.roles(id, role)
VALUES (1, 'ADMIN');

INSERT INTO public.roles(id, role)
VALUES (2, 'JOURNALIST');

INSERT INTO public.roles(id, role)
VALUES (3, 'SUBSCRIBER');


INSERT INTO public.users_roles(user_id, role_id)
VALUES (1, 1);

INSERT INTO public.users_roles(user_id, role_id)
VALUES (2, 2);

INSERT INTO public.users_roles(user_id, role_id)
VALUES (3, 3);