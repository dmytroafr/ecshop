INSERT INTO users (id, archive, email, name, password, role, bucket_id)
VALUES (1, false, 'office@e-chem.com.ua', admin, 'password', 'ADMIN', null);

ALTER SEQUENCE user_seq RESTART WITH 2;