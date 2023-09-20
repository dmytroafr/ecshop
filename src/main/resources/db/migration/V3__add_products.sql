INSERT INTO products (id,on_stock,price, opt_price, title, country_producer,photo_url,producer, product_description)
VALUES  (1,'AVAILABLE',205.0,1.0,'Skinman soft 1l','Germany','http','Vilan','For disinfection'),
         (2,'AVAILABLE',210.0,1.0,'Incidin liquid 1l','Germany','http','Vilan','For disinfection'),
        (3,'AVAILABLE',380.0,1.0,'Bacillol AF 1l','Germany','http','Vilan','For disinfection'),
        (4,'AVAILABLE',380.0,1.0,'Sterillium classic 1l','Germany','http','Vilan','For disinfection'),
        (5,'AVAILABLE',335.0,1.0,'Cutasept F 1l','Germany','http','Vilan','For disinfection'),
        (6,'AVAILABLE',375.0,1.0,'Cutasept G 1l','Germany','http','Vilan','For disinfection'),
        (7,'AVAILABLE',210.0,1.0,'Aniosime 1l','Germany','http','Vilan','For disinfection'),
        (8,'AVAILABLE',850.0,1.0,'Surfanios 1l','Germany','http','Vilan','For disinfection'),
        (9,'AVAILABLE',195.0,1.0,'Aniospray 1l','Germany','http','Vilan','For disinfection'),
        (10,'AVAILABLE',260.0,1.0,'Aniosgel 1l','Germany','http','Vilan','For disinfection');

ALTER SEQUENCE product_seq RESTART WITH 11;