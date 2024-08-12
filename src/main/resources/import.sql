INSERT INTO users (name , password) VALUES ('admin', '$2a$12$mTo9Js0M/CB8hzXgfwljr.oR9q0Rnx88iH2ZvIlrFAMP.IctKX0YG');
INSERT INTO users (name , password) VALUES ('user', '$2a$12$EzPE91.ANj5NUftxeYhdhOVsIDhy.rVxMwoGkgXhwbrNIEC1XGflu');
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

INSERT INTO user_role (user_id, role_id) VALUES ((SELECT id FROM users WHERE name = 'admin'),(SELECT id FROM roles WHERE name = 'ADMIN'));
INSERT INTO user_role (user_id, role_id) VALUES ((SELECT id FROM users WHERE name = 'user'),(SELECT id FROM roles WHERE name = 'USER'));