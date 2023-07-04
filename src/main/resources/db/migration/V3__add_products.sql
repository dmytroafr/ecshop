INSERT INTO products (id, price, title, country_producer,photo_url,producer, product_description)
VALUES  (1,205.0,'Skinman soft 1l','Germany','http','Vilan','For disinfection'),
         (2,210.0,'Incidin liquid 1l','Germany','http','Vilan','For disinfection'),
        (3,380.0,'Bacillol AF 1l','Germany','http','Vilan','For disinfection'),
        (4,380.0,'Sterillium classic 1l','Germany','http','Vilan','For disinfection'),
        (5,335.0,'Cutasept F 1l','Germany','http','Vilan','For disinfection'),
        (6,375.0,'Cutasept G 1l','Germany','http','Vilan','For disinfection'),
        (7,210.0,'Aniosime 1l','Germany','http','Vilan','For disinfection'),
        (8,850.0,'Surfanios 1l','Germany','http','Vilan','For disinfection'),
        (9,195.0,'Aniospray 1l','Germany','http','Vilan','For disinfection'),
        (10,260.0,'Aniosgel 1l','Germany','http','Vilan','For disinfection');

ALTER SEQUENCE product_seq RESTART WITH 6;