INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date)
VALUES ('Free coca-cola certificate', 'Simple use it and get coca-cola', 1000, 10, '2022-01-01 12:00:00', '2022-01-02 13:30:25'),
       ('KFC certificate', '10 dollars certificate', 1000, 10, '2022-01-03 16:30:20', '2022-01-03 16:30:20'),
       ('100 dollars certificate', 'Buy anything for 100 dollars', 10000, 20, '2022-01-05 14:29:12', '2022-01-05 14:29:12'),
       ('Awesome certificate without tags', 'No tags provided', 100,3,'2022-01-10 17:19:46', '2022-01-11 12:19:46');

INSERT INTO tag (name)
VALUES ('coca-cola'),
       ('kfc'),
       ('money certificate'),
       ('unused tag');

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1,1),
       (2,2), (2,3),
       (3,3);
