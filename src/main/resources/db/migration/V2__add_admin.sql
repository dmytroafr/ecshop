insert into users (archive, id, email, name, password, phone, role)
values (false, 1, 'office@e-chem.com.ua', 'admin', 'pass','+38', 'ADMIN');

alter sequence user_seq restart with 2;