INSERT INTO schedule (day, open_time, close_time)
VALUES ('mon', 9, 16), ('tue', 9, 16), ('wed', 9, 16), ('thu', 9, 16), ('fri', 9, 16);

INSERT INTO stocks (ticker, co_name, init_price, volume)
VALUES ('disney', 'DIS', 94.34, 0), ('amc', 'AMC', 12.53, 0);

INSERT INTO users (username, email, first_name, last_name, password, role)
VALUES ('admin', 'admin@email.com', 'admin', 'admin', '$2a$10$t4yEICuRGn3f6UhBoMfTVOaA1dgyje.1WPqeuaAd0dCdWy6r56OrS', 'ROLE_ADMIN'),
       ('user', 'user@email.com', 'user', 'userr', '$2a$10$t4yEICuRGn3f6UhBoMfTVOaA1dgyje.1WPqeuaAd0dCdWy6r56OrS', 'ROLE_USER');