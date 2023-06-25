insert into users (archive, bucket_id, id, email, name, password, phone, role)
values (false, null, 1, 'office@e-chem.com.ua', 'admin', 'pass','+38', 'ADMIN');

alter sequence user_seq restart with 2;