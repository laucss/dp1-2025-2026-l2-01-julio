-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (1,'admin1','admin1@example.com','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1,25,'/Avatar_default.png');


INSERT INTO achievement(id,name,description,threshold,badge_image,metric) VALUES (1,'Principiante','Si juegas 5 partidas',10.0,'https:/cdn-icons-png.flaticon.com/512/5243/5243423.png','GAMES_PLAYED');
INSERT INTO achievement(id,name,description,threshold,badge_image,metric) VALUES (2,'Explorador','Si juegas 25 partidas',25.0,'https:/cdn-icons-png.flaticon.com/512/603/603855.png','GAMES_PLAYED');
INSERT INTO achievement(id,name,description,threshold,metric) VALUES (3,'Experto','Si ganas 20 partidas',20.0,'VICTORIES');

-- Ten player users, named player1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (4,'player1','player1@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (5,'player2','player2@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (6,'player3','player3@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (7,'player4','player4@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (8,'player5','player5@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (9,'player6','player6@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (10,'player7','player7@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (11,'player8','player8@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (12,'player9','player9@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (13,'player10','player10@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (2, 'CYB6650','cyb6650@example.com','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (14,'QSS7721','qss7721@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (15,'QFL3393','qfl3393@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (16, 'SBJ4592', 'sbj4592@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (3, 'FSS8078', 'fss8078@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2,25,'/Avatar_default.png');
INSERT INTO appusers(id,username,email,password,authority,age,avatar) VALUES (17, 'XNT3290', 'xnt3290@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2,25,'/Avatar_default.png');

-- Match

-- Partida no iniciada, no empezada aún
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES (1,'Fiesta para todos!!! ÚNETE!',NULL, 'WAITING',NULL,NULL,6,3,3,false);

INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES (5,'Fiesta ',NULL, 'WAITING',NULL,NULL,5,3,3,false);

INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES (4,'nerea!','DEF345', 'WAITING',NULL,NULL,5,3,4,true);

-- Partida en progreso 
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES (2,'Partida en curso','ABC123','PLAYING','2025-10-26 20:00:00',NULL,5,3,3,false);

-- Partida finalizada
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,num_npcs,is_private)
VALUES (3,'Partida terminada','XYZ789','FINISHED','2025-10-26 18:00:00','2025-10-26 19:00:00',4,3,3,true);

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
        (53,'/images/cards/Carta 53.jpg','/images/cards/backCard.jpg','R'),
        (54,'/images/cards/Carta 54.jpg','/images/cards/backCard.jpg','R'),
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



        

INSERT INTO player (id, action_points, match_id, strength, user_id) 
        VALUES (7,null,1,null,4), 
               (8,null,1,null,5), 
               (9,null,1,null,6),
               (10,null,2,null,7);

INSERT INTO Room (id, name, black_dice, white_dice, adjacency_list_id) 
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
               (15, 'Spa', 3, 3),
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
               (36,35);

