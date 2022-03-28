INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date)
VALUES ('Spa certificate', 'Program “Ultratransformation. All Inclusive: 75 minutes” includes full body peeling, SPA wrap (chocolate, exotic, fruit, ginger or honey), infrared heating up to 80 degrees and relaxing tea drinking. For more than an hour you will rest in the hands of an experienced master, and your body will become more beautiful.',
        8200, 10, '2022-01-01 12:00:00', '2022-01-02 13:30:25'),

       ('Skydiving certificate', 'How long has someone close to you been going to skydive? How much longer will he put off receiving emotions and justify it by not knowing the intricacies of the jump and the location of the airfield? If these are real excuses, buy him a gift certificate for skydiving in Minsk.',
        14340, 20, '2022-01-03 16:30:20', '2022-01-03 16:30:20'),

       ('Skiing certificate', 'Three hours or a whole day on professional skiing!',
        2586, 7, '2022-01-05 14:29:12', '2022-01-05 14:29:12'),

       ('Family vacation in the ski entertainment center', 'The whole day on professional skiing or snowboarding with the whole family!',
        11239,14,'2022-01-10 17:19:46', '2022-01-11 12:19:46'),

       ('Gift Certificate to Zalkind Kitchen Restaurant', 'Enjoy aristocratic interiors, haute cuisine and rare wines at the Zalkind Restaurant',
        20000,5,'2022-01-15 12:12:23', '2022-01-15 12:12:23'),

       ('Winter driving on the race track in a Subaru BRZ', 'Challenge yourself on a specially designed racing track and learn a lot from a professional instructor.',
        88530,30,'2022-01-17 15:11:44', '2022-01-17 16:12:13');

INSERT INTO tag (name)
VALUES ('rest'),    -- 1
       ('spa'),     -- 2
       ('health'),  -- 3
       ('sky'),     -- 4
       ('extreme'), -- 5
       ('ski'),     -- 6
       ('winter'),  -- 7
       ('family'),  -- 8
       ('nature'),  -- 9
       ('vine'),    -- 10
       ('restaurant');-- 11

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1,1), (1,2), (1,3),
       (2,1), (2,4), (2,5),
       (3,5), (3,6), (3,7),
       (4,6), (4,7), (4,8), (4,9),(4,1),
       (5,1), (5,10), (5,11);

INSERT INTO users(email,password,first_name,last_name,role)
VALUES ('alex','$2a$10$t7Yelc6KO8lOcOQUd1eYDOl/T6LxVPWwee4DlMTSdNOrSo.JZ9cNq','Alox','First','ADMIN'),
       ('john','$2a$10$t7Yelc6KO8lOcOQUd1eYDOl/T6LxVPWwee4DlMTSdNOrSo.JZ9cNq','John','Second','CLIENT'),
       ('nick','$2a$10$t7Yelc6KO8lOcOQUd1eYDOl/T6LxVPWwee4DlMTSdNOrSo.JZ9cNq','Nick','Third','CLIENT'),
       ('carl','$2a$10$t7Yelc6KO8lOcOQUd1eYDOl/T6LxVPWwee4DlMTSdNOrSo.JZ9cNq','Carl','Fourth','CLIENT'),
       ('spring','$2a$10$t7Yelc6KO8lOcOQUd1eYDOl/T6LxVPWwee4DlMTSdNOrSo.JZ9cNq','Spring','Fives','CLIENT');

INSERT INTO orders (user_id, create_date, last_update_date, total_price)
VALUES (1, '2022-02-01 13:05:43', '2022-02-01 13:05:43', 5460),
       (2, '2022-01-24 15:51:23', '2022-01-24 15:51:23', 1250),
       (1, '2022-01-11 17:55:21', '2022-01-11 17:55:21', 54500),
       (1, '2022-02-05 09:00:42', '2022-02-05 09:00:42', 4000);

INSERT INTO order_detail (gift_certificate_id, order_id, price, quantity)
VALUES (2, 1, 2730, 2),
       (1, 2, 8200, 3),
       (1, 3, 2500, 1),
       (5, 3, 6000, 2),
       (6, 3, 60000, 1),
       (2, 4, 14340, 1);
