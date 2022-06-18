Stock Trading System

Adding an admin to the database:

A user already needs to exist.
update users set role = 'ROLE_ADMIN' where username = _username;