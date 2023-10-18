
INSERT INTO users (archive,id, email, extra_phone, name, password, phone, role )
values (false, 6000,'office@e-chem.com.ua','', 'admin', '$2a$10$YjI6Lz7V1yFt1L13kWjKA.jXTJMO5F1NK.sGQx4iwAlwJ4jqvQkpK', '+380957913429', 'ADMIN');
alter sequence user_seq restart with 6001;
