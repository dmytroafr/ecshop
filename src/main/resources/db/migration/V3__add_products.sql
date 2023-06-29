INSERT INTO products (id, price, title)
VALUES  (1,205.0,'Skinman soft 1l'), (2,210.0,'Incidin liquid 1l'),
        (3,380.0,'Bacillol AF 1l'),
        (4,380.0,'Sterillium classic 1l'),
        (5,335.0,'Cutasept F 1l'),
        (6,375.0,'Cutasept G 1l'),
        (7,210.0,'Aniosime 1l'),
        (8,850.0,'Surfanios 1l'),
        (9,195.0,'Aniospray 1l'),
        (10,260.0,'Aniosgel 1l');

ALTER SEQUENCE product_seq RESTART WITH 6;