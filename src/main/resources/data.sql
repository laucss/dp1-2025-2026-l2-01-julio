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
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (1,'Fiesta para todos!!! ÚNETE!',NULL, 'WAITING',NULL,NULL,6,3,false),
        (5,'Fiesta ',NULL, 'WAITING',NULL,NULL,5,3,false);

INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (4,'nerea!','DEF345', 'WAITING',NULL,NULL,5,3,true);

-- Partida en progreso 
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (2,'Partida en curso','ABC123','PLAYING','2025-10-26 20:00:00',NULL,5,3,false);

-- Partida finalizada
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (3,'Partida terminada','XYZ789','FINISHED','2025-10-26 18:00:00','2025-10-26 19:00:00',4,3,true),
       (6,'Partida terminada 2',NULL,'FINISHED','2025-10-25 18:00:00','2025-10-25 19:00:00',6,3,false),
        (7,'Partida terminada 3','GHI456','FINISHED','2025-11-24 18:00:00','2025-11-24 19:00:00',5,3,true);

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
               (10,null,2,null,7),
               (11,12,6,8,4),
               (12,10,6,14,9),
               (13,5,7,null,4);
               
