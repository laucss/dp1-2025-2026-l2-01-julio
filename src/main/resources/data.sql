-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (1,'admin1','admin1@example.com','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1,25,'/Avatar_default.png');


-- Ten player users, named player1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (4,'player1','player1@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar1.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (5,'player2','player2@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar2.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (6,'player3','player3@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (7,'player4','player4@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar1.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (8,'player5','player5@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar2.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (9,'player6','player6@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (10,'player7','player7@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar1.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (11,'player8','player8@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar2.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (12,'player9','player9@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (13,'player10','player10@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar1.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (2, 'CYB6650','cyb6650@example.com','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',2,25,'/Avatar2.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (14,'QSS7721','qss7721@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (15,'QFL3393','qfl3393@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar1.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (16, 'SBJ4592', 'sbj4592@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2,25,'/Avatar2.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (3, 'FSS8078', 'fss8078@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (17, 'XNT3290', 'xnt3290@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (18,'player11','player11@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar2.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (19,'player12','player12@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (20,'player13','player13@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar1.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (21,'player14','player14@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar2.jpg');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (22,'player15','player15@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');

-- Match

-- Partidas no iniciada, no empezada aún
INSERT INTO match(id,name,code,creator_id,status,start_time,end_time,max_players,min_players,num_npcs,is_private,winner_id)
VALUES (1,'Fiesta para todos!!! ÚNETE!',NULL,4,'WAITING',NULL,NULL,3,3,3,false,null);

INSERT INTO match(id,name,code,creator_id,status,start_time,end_time,max_players,min_players,num_npcs,is_private,winner_id)
VALUES (5,'Fiesta ',NULL, 7, 'WAITING',NULL,NULL,5,3,3,false,null);

INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,num_npcs,is_private,winner_id)
VALUES (4,'nerea!','DEF345', 'WAITING',NULL,NULL,5,3,4,true,null);

-- Partida en progreso 
INSERT INTO match(id,name,code,creator_id,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES (2,'Partida en curso','ABC123',7,'PLAYING','2025-10-26 20:00:00',NULL,5,3,3,false);

-- Más partidas en progreso

INSERT INTO match(id,name,code,creator_id,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES
        (8,'Escape Masters','JKL111',8,'PLAYING','2025-10-27 16:15:00',NULL,5,3,4,false),

        (9,'Operation Elba','MNO222',16,'PLAYING','2025-10-28 18:30:00',NULL,6,3,3,true),

        (10,'Night Escape',NULL,20,'PLAYING','2025-10-29 20:00:00',NULL,4,3,2,false),

        (11,'The Last Prisoners','PQR333',22,'PLAYING','2025-10-30 19:10:00',NULL,5,3,5,true);

-- Partidas finalizadas
INSERT INTO match(id,name,code,creator_id,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES (3,'Tinkissss','XYZ789',15,'FINISHED','2025-10-26 18:00:00','2025-10-26 19:00:00',4,3,3,true),
        (6,'Monster high',NULL,6,'FINISHED','2025-10-25 18:35:00','2025-10-25 19:00:00',6,3,3,false),
        (7,'Katseye','GHI456',11,'FINISHED','2025-11-24 16:00:00','2025-11-24 17:15:00',5,3,3,true);

INSERT INTO match(id,name,code,creator_id,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES (12,'Escape Legends','STU444',4,'FINISHED','2025-10-15 17:00:00','2025-10-15 18:20:00',5,3,4,false),

        (13,'Prison Break','VWX555',8,'FINISHED','2025-10-17 19:00:00','2025-10-17 20:05:00',6,3,3,true),

        (14,'Island Survivors',NULL,10,'FINISHED','2025-10-18 21:15:00','2025-10-18 22:00:00',4,3,2,false),

        (15,'Hidden Tunnels','YZA666',12,'FINISHED','2025-10-20 16:30:00','2025-10-20 17:50:00',5,3,4,true),

        (16,'Secret Mission',NULL,15,'FINISHED','2025-10-22 18:00:00','2025-10-22 19:25:00',5,3,3,false),

        (17,'Final Escape','BCD777',17,'FINISHED','2025-10-24 15:45:00','2025-10-24 17:10:00',6,3,5,true);


-- Cartas 
INSERT INTO cards(id,front_image, back_image,letter ) 
VALUES (1,'/images/cards/Carta 1.jpg','/images/cards/backCard.jpg','A'),
        (2,'/images/cards/Carta 2.jpg','/images/cards/backCard.jpg','A'),
        (3,'/images/cards/Carta 3.jpg','/images/cards/backCard.jpg','A'),
        (4,'/images/cards/Carta 4.jpg','/images/cards/backCard.jpg','A'),
        (5,'/images/cards/Carta 5.jpg','/images/cards/backCard.jpg','A'),
        (6,'/images/cards/Carta 6.jpg','/images/cards/backCard.jpg','A'),
        (7,'/images/cards/Carta 7.jpg','/images/cards/backCard.jpg','A'),
        (8,'/images/cards/Carta 8.jpg','/images/cards/backCard.jpg','A'),
        (9,'/images/cards/Carta 9.jpg','/images/cards/backCard.jpg','A'),
        (10,'/images/cards/Carta 10.jpg','/images/cards/backCard.jpg','A'),
        (11,'/images/cards/Carta 11.jpg','/images/cards/backCard.jpg','B'),
        (12,'/images/cards/Carta 12.jpg','/images/cards/backCard.jpg','B'),
        (13,'/images/cards/Carta 13.jpg','/images/cards/backCard.jpg','B'),
        (14,'/images/cards/Carta 14.jpg','/images/cards/backCard.jpg','B'),
        (15,'/images/cards/Carta 15.jpg','/images/cards/backCard.jpg','C'),
        (16,'/images/cards/Carta 16.jpg','/images/cards/backCard.jpg','C'),
        (17,'/images/cards/Carta 17.jpg','/images/cards/backCard.jpg','C'),
        (18,'/images/cards/Carta 18.jpg','/images/cards/backCard.jpg','C'),
        (19,'/images/cards/Carta 19.jpg','/images/cards/backCard.jpg','C'),
        (20,'/images/cards/Carta 20.jpg','/images/cards/backCard.jpg','C'),
        (21,'/images/cards/Carta 21.jpg','/images/cards/backCard.jpg','E'),
        (22,'/images/cards/Carta 22.jpg','/images/cards/backCard.jpg','E'),
        (23,'/images/cards/Carta 23.jpg','/images/cards/backCard.jpg','E'),
        (24,'/images/cards/Carta 24.jpg','/images/cards/backCard.jpg','E'),
        (25,'/images/cards/Carta 25.jpg','/images/cards/backCard.jpg','E'),
        (26,'/images/cards/Carta 26.jpg','/images/cards/backCard.jpg','E'),
        (27,'/images/cards/Carta 27.jpg','/images/cards/backCard.jpg','E'),
        (28,'/images/cards/Carta 28.jpg','/images/cards/backCard.jpg','E'),
        (29,'/images/cards/Carta 29.jpg','/images/cards/backCard.jpg','E'),
        (30,'/images/cards/Carta 30.jpg','/images/cards/backCard.jpg','E'),
        (31,'/images/cards/Carta 31.jpg','/images/cards/backCard.jpg','E'),
        (32,'/images/cards/Carta 32.jpg','/images/cards/backCard.jpg','E'),
        (33,'/images/cards/Carta 33.jpg','/images/cards/backCard.jpg','F'),
        (34,'/images/cards/Carta 34.jpg','/images/cards/backCard.jpg','F'),
        (35,'/images/cards/Carta 35.jpg','/images/cards/backCard.jpg','F'),
        (36,'/images/cards/Carta 36.jpg','/images/cards/backCard.jpg','F'),
        (37,'/images/cards/Carta 37.jpg','/images/cards/backCard.jpg','L'),
        (38,'/images/cards/Carta 38.jpg','/images/cards/backCard.jpg','L'),
        (39,'/images/cards/Carta 39.jpg','/images/cards/backCard.jpg','L'),
        (40,'/images/cards/Carta 40.jpg','/images/cards/backCard.jpg','L'),
        (41,'/images/cards/Carta 41.jpg','/images/cards/backCard.jpg','M'),
        (42,'/images/cards/Carta 42.jpg','/images/cards/backCard.jpg','M'),
        (43,'/images/cards/Carta 43.jpg','/images/cards/backCard.jpg','M'),
        (44,'/images/cards/Carta 44.jpg','/images/cards/backCard.jpg','M'),
        (45,'/images/cards/Carta 45.jpg','/images/cards/backCard.jpg','O'),
        (46,'/images/cards/Carta 46.jpg','/images/cards/backCard.jpg','O'),
        (47,'/images/cards/Carta 47.jpg','/images/cards/backCard.jpg','O'),
        (48,'/images/cards/Carta 48.jpg','/images/cards/backCard.jpg','O'),
        (49,'/images/cards/Carta 49.jpg','/images/cards/backCard.jpg','P'),
        (50,'/images/cards/Carta 50.jpg','/images/cards/backCard.jpg','P'),
        (51,'/images/cards/Carta 51.jpg','/images/cards/backCard.jpg','P'),
        (52,'/images/cards/Carta 52.jpg','/images/cards/backCard.jpg','P'),
        (53,'/images/cards/Carta 53.jpg','/images/cards/backCard.jpg','P'),
        (54,'/images/cards/Carta 54.jpg','/images/cards/backCard.jpg','P'),
        (55,'/images/cards/Carta 55.jpg','/images/cards/backCard.jpg','R'),
        (56,'/images/cards/Carta 56.jpg','/images/cards/backCard.jpg','R'),
        (57,'/images/cards/Carta 57.jpg','/images/cards/backCard.jpg','R'),
        (58,'/images/cards/Carta 58.jpg','/images/cards/backCard.jpg','R'),
        (59,'/images/cards/Carta 59.jpg','/images/cards/backCard.jpg','R'),
        (60,'/images/cards/Carta 60.jpg','/images/cards/backCard.jpg','R'),
        (61,'/images/cards/Carta 61.jpg','/images/cards/backCard.jpg','S'),
        (62,'/images/cards/Carta 62.jpg','/images/cards/backCard.jpg','S'),
        (63,'/images/cards/Carta 63.jpg','/images/cards/backCard.jpg','S'),
        (64,'/images/cards/Carta 64.jpg','/images/cards/backCard.jpg','S'); 



--Players
INSERT INTO player (id, action_points, match_id, strength, user_id) 
        VALUES (7,null,1,null,4), 
               (8,null,1,null,5), 
               (9,null,1,null,6),
               (10,null,2,null,7),
               (11,12,3,null,9),
               (12,5,6,null,9),
               (13,7,7,null,9),
               (14,10,3,null,15),
               (15,7,3,null,4),
               (16,8,3,null,5),
                 (17,6,6,null,6),
                 (18,9,6,null,8),
                 (19,11,6,null,10),
                 (20,4,7,null,11),
                 (21,8,7,null,12),
                 (22,7,7,null,13),
                 (23,10,7,null,14),
                 (24,null,5,null,7),
                 (25,null,5,null,8),
                 (26,null,5,null,9);

INSERT INTO player (id, action_points, match_id, strength, user_id)
                VALUES
                (27,9,8,NULL,8),
                (28,11,8,NULL,10),
                (29,8,8,NULL,12),

                (30,12,9,NULL,14),
                (31,7,9,NULL,15),
                (32,6,9,NULL,16),

                (33,10,10,NULL,17),
                (34,8,10,NULL,20),
                (35,5,10,NULL,21),

                (36,13,11,NULL,22),
                (37,7,11,NULL,11),
                (38,9,11,NULL,13);

INSERT INTO player (id, action_points, match_id, strength, user_id)
        VALUES
        (39,15,12,NULL,4),
        (40,11,12,NULL,5),
        (41,8,12,NULL,6),

        (42,14,13,NULL,8),
        (43,10,13,NULL,9),
        (44,7,13,NULL,10),

        (45,16,14,NULL,10),
        (46,12,14,NULL,11),
        (47,9,14,NULL,12),

        (48,13,15,NULL,12),
        (49,10,15,NULL,13),
        (50,8,15,NULL,14),

        (51,18,16,NULL,15),
        (52,11,16,NULL,16),
        (53,9,16,NULL,17),

        (54,17,17,NULL,17),
        (55,13,17,NULL,4),
        (56,10,17,NULL,7),
        (57,2,2,NULL,18),
        (58,3,2,NULL,19);

UPDATE match SET winner_id = 14 WHERE id = 3;
UPDATE match SET winner_id = 18 WHERE id = 6;
UPDATE match SET winner_id = 20 WHERE id = 7;
UPDATE match SET winner_id = 39 WHERE id = 12;
UPDATE match SET winner_id = 42 WHERE id = 13;
UPDATE match SET winner_id = 45 WHERE id = 14;
UPDATE match SET winner_id = 48 WHERE id = 15;
UPDATE match SET winner_id = 51 WHERE id = 16;
UPDATE match SET winner_id = 54 WHERE id = 17;

-- Habitaciones

INSERT INTO Room (id, name, black_dice, white_dice) 
        VALUES (1, 'North Tower', 1, 1), 
               (2, 'Caesar Room',1,2), 
               (3, 'Opal Room',1,3),
               (4, 'Coral Room', 1, 4),
               (5, 'Roof', 1, 5),
               (6, 'East Tower', 1, 6),
               (7, 'Corridor 1', 2, 1),
               (8, 'Cafe',2,2),
               (9, 'Corridor 2', 2, 3),
               (10, 'Corridor 2', 2, 4),
               (11, 'Parlor', 2, 5),
               (12, 'Corridor 3', 2, 6),
               (13, 'Ball Room', 3, 1),
               (14, 'Corridor 4', 3, 2),
               (15, 'SPA', 3, 3),
               (16, 'Pool', 3, 4),
               (17, 'Corridor 5', 3, 5),
               (18, 'Sleep Room', 3, 6),
               (19, 'Class Room', 4, 1),
               (20, 'Corridor 6', 4, 2),
               (21, 'Arbor', 4, 3),
               (22, 'Farm', 4, 4),
               (23, 'Corridor 7', 4, 5),
               (24, 'Meal Room', 4, 6),
               (25, 'Corridor 8', 5, 1),
               (26, 'Bar', 5, 2),
               (27, 'Corridor 9', 5, 3),
               (28, 'Corridor 9', 5, 4),
               (29, 'Lab', 5, 5),
               (30, 'Corridor 10', 5, 6),
               (31, 'West Tower', 6, 1),
               (32, 'Cellar', 6, 2),
               (33, 'Apple Room', 6, 3),
               (34, 'Map Room', 6, 4),
               (35, 'Parole Room', 6, 5),
               (36, 'South Tower', 6, 6),
               (37, 'Safe Area', null, null);

-- Adyacencias de las habitaciones
INSERT INTO room_adjacency_list (room_id, adjacency_list_id) 
        VALUES (1,2),
               (1,7),
               (2,1),
               (2,3),
               (3,2),
               (3,8),
               (4,5),
               (4,11),
               (5,4),
               (5,6),
               (6,5),
               (6,12),
               (7,1),
               (7,8),
               (7,14),
               (8,3),
               (8,7),
               (8,9),
               (8,10),
               (8,15),
               (9,8),
               (9,11),
               (9,37),
               (10,8),
               (10,11),
               (10,37),
               (11,4),
               (11,9),
               (11,10),
               (11,12),
               (11,16),
               (12,6),
               (12,11),
               (12,17),
               (13,14),
               (13,19),
               (14,13),
               (14,7),
               (14,15),
               (15,8),
               (15,14),
               (15,37),
               (15,21),
               (16,11),
               (16,17),
               (16,37),
               (16,22),
               (17,12),
               (17,16),
               (17,18),
               (18,17),
               (18,24),
               (19,13),
               (19,20),
               (20,19),
               (20,21),
               (20,25),
               (21,20),
               (21,15),
               (21,37),
               (21,26),
               (22,16),
               (22,23),
               (22,37),
               (22,29),
               (23,22),
               (23,24),
               (23,30),
               (24,18),
               (24,23),
               (25,20),
               (25,26),
               (25,31),
               (26,21),
               (26,25),
               (26,27),
               (26,28),
               (26,33),
               (27,26),
               (27,37),
               (27,29),
               (28,26),
               (28,37),
               (28,29),
               (29,22),
               (29,27),
               (29,28),
               (29,30),
               (29,34),
               (30,23),
               (30,29),
               (30,36),
               (31,25),
               (31,32),
               (32,31),
               (32,33),
               (33,32),
               (33,26),
               (34,29),
               (34,35),
               (35,34),
               (35,36),
               (36,30),
               (36,35),
               (37,9),
               (37,10),
               (37,15),
               (37,16),
               (37,21),
               (37,22),
               (37,27),
               (37,28);

/*
-- Friend request test data
-- User IDs: 101-110 for friend request tests
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (101,'test_user_101','test101@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (102,'test_user_102','test102@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (103,'test_user_103','test103@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (104,'test_user_104','test104@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (105,'test_user_105','test105@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (106,'test_user_106','test106@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (107,'test_user_107','test107@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (110,'test_user_110','test110@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');

-- Friend requests for testing
-- Request IDs: 201 (pending), 202 (accepted)
-- USER_ID_ONE_SENT_REQUEST = 101 (1 sent)
-- USER_ID_TWO_SENT_REQUESTS = 102 (2 sent)
-- USER_ID_ONE_RECEIVED_REQUEST = 102 (1 received)
-- USER_ID_TWO_RECEIVED_REQUESTS = 103 (2 received)
-- USER_ID_ONE_ACCEPTED_REQUEST = 102 (1 accepted friend)
-- USER_ID_TWO_ACCEPTED_REQUESTS = 103 (2 accepted friends)
-- USER_ID_ONE_PENDING_REQUEST = 101, USER_ID_THREE_PENDING_REQUESTS = 104
INSERT INTO requests(id, request_status, sender, receiver) VALUES (201, 'PENDING', 101, 104);
INSERT INTO requests(id, request_status, sender, receiver) VALUES (202, 'ACCEPTED', 105, 103);
INSERT INTO requests(id, request_status, sender, receiver) VALUES (203, 'ACCEPTED', 106, 103);
INSERT INTO requests(id, request_status, sender, receiver) VALUES (204, 'ACCEPTED', 105, 102);
INSERT INTO requests(id, request_status, sender, receiver) VALUES (205, 'PENDING', 102, 103);
INSERT INTO requests(id, request_status, sender, receiver) VALUES (206, 'PENDING', 102, 104);
INSERT INTO requests(id, request_status, sender, receiver) VALUES (207, 'PENDING', 107, 103);

-- Ensure match 1 has a winner set to player.id=9
 Por facilitar el testeo lo comento
UPDATE match
   SET status = 'FINISHED',
           start_time = '2025-12-01 18:00:00',
           end_time   = '2025-12-01 19:00:00',
           winner_id  = 9
 WHERE id = 1;
 */


-- ACHIEVEMENTS ------------
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (1,'If you play 5 games',5.0,'https://cdn-icons-png.flaticon.com/512/3430/3430778.png ','GAMES_PLAYED','FACIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (2,'If you play 15 games',15.0,'https://cdn-icons-png.flaticon.com/512/3430/3430778.png','GAMES_PLAYED','INTERMEDIO');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (3,'If you play 25 games',25.0,'https://cdn-icons-png.flaticon.com/512/3430/3430778.png','GAMES_PLAYED','DIFICIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (4,'If you win 5 games',5.0,'https://cdn-icons-png.flaticon.com/512/5021/5021877.png','VICTORIES','FACIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (5,'If you win 10 games',10.0,'https://cdn-icons-png.flaticon.com/512/5021/5021877.png','VICTORIES','INTERMEDIO');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (6,'If you win 20 games',20.0,'https://cdn-icons-png.flaticon.com/512/5021/5021877.png','VICTORIES','DIFICIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (7,'If you play more than 20 minutes',20.1,'https://cdn-icons-png.flaticon.com/512/850/850960.png','TOTAL_PLAY_TIME','FACIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (8,'If you play more than 60 minutes',60.1,'https://cdn-icons-png.flaticon.com/512/850/850960.png','TOTAL_PLAY_TIME','INTERMEDIO');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (9,'If you play more than 120 minutes',120.1,'https://cdn-icons-png.flaticon.com/512/850/850960.png','TOTAL_PLAY_TIME','DIFICIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (10,'If you accumulate 50 action points',50.0,'https:/cdn-icons-png.flaticon.com/512/603/603855.png','ACTION_POINTS_EARNED','FACIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (11,'If you accumulate 100 action points',100.0,'https:/cdn-icons-png.flaticon.com/512/603/603855.png','ACTION_POINTS_EARNED','INTERMEDIO');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (12,'If you accumulate 200 action points',200.0,'https:/cdn-icons-png.flaticon.com/512/603/603855.png','ACTION_POINTS_EARNED','DIFICIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (13,'If you win 10 battles',10.0,'https://cdn-icons-png.flaticon.com/512/1732/1732476.png','BATTLES_WON','FACIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (14,'If you win 25 battles',25.0,'https://cdn-icons-png.flaticon.com/512/1732/1732476.png','BATTLES_WON','INTERMEDIO');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (15,'If you win 50 battles',50.0,'https://cdn-icons-png.flaticon.com/512/1732/1732476.png','BATTLES_WON','DIFICIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (16,'If you visit 10 rooms',10.0,'https://cdn-icons-png.flaticon.com/512/11117/11117864.png','ROOMS_VISITED','FACIL');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (17,'If you visit 30 rooms',30.0,'https://cdn-icons-png.flaticon.com/512/11117/11117864.png','ROOMS_VISITED','INTERMEDIO');
INSERT INTO achievement(id,description,threshold,badge_image,metric,tier) VALUES (18,'If you visit 60 rooms',60.0,'https://cdn-icons-png.flaticon.com/512/11117/11117864.png','ROOMS_VISITED','DIFICIL');

