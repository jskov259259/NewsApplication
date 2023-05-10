CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);

CREATE TABLE roles
(
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    UNIQUE (user_id, role_id)
);


INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (1, 'Admin', '1234', 'Dzmitry', 'Kalustau');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (2, 'Ignavia', '1234', 'Ignavia', 'Archil');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (3, 'Polli', '1234', 'Polli', 'Estela');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (4, 'Sycosis', '1234', 'Sycosis', 'Urho');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (5, 'Jentacular', '1234', 'Jentacular', 'Adelheid');
INSERT INTO public.users(id, username, password, first_name, last_name)
VALUES (6, 'LimanYawp', '1234', 'Liman', 'Yawp');


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
VALUES (3, 2);
INSERT INTO public.users_roles(user_id, role_id)
VALUES (4, 2);
INSERT INTO public.users_roles(user_id, role_id)
VALUES (5, 3);
INSERT INTO public.users_roles(user_id, role_id)
VALUES (6, 3);