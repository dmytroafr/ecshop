INSERT INTO users (archive, id, email, extra_phone, name, password, phone, role)
values (false, 1, 'office@e-chem.com.ua', '', 'admin', '$2a$12$LhT3WKtzAsV2P1aYWZcEeO8usOl4wAEuaC69vnKklc14jNajGBQyu', '+380957913429', 'ADMIN');
alter sequence user_seq restart with 2;
