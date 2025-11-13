-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO appusers(id,username,email,password,authority) VALUES (1,'admin1','admin1@example.com','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',1);


INSERT INTO achievement(id,name,description,threshold,badge_image,metric) VALUES (1,'Principiante','Si juegas 5 partidas',10.0,'https://cdn-icons-png.flaticon.com/512/5243/5243423.png','GAMES_PLAYED');
INSERT INTO achievement(id,name,description,threshold,badge_image,metric) VALUES (2,'Explorador','Si juegas 25 partidas',25.0,'https://cdn-icons-png.flaticon.com/512/603/603855.png','GAMES_PLAYED');
INSERT INTO achievement(id,name,description,threshold,metric) VALUES (3,'Experto','Si ganas 20 partidas',20.0,'VICTORIES');

-- Ten player users, named player1 with passwor 0wn3r
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO appusers(id,username,email,password,authority) VALUES (4,'player1','player1@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (5,'player2','player2@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (6,'player3','player3@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (7,'player4','player4@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (8,'player5','player5@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (9,'player6','player6@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (10,'player7','player7@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (11,'player8','player8@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (12,'player9','player9@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (13,'player10','player10@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (2, 'CYB6650','cyb6650@example.com','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (14,'QSS7721','qss7721@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (15,'QFL3393','qfl3393@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (16, 'SBJ4592', 'sbj4592@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (3, 'FSS8078', 'fss8078@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2);
INSERT INTO appusers(id,username,email,password,authority) VALUES (17, 'XNT3290', 'xnt3290@example.com','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e', 2);

-- Match

-- Partida no iniciada, no empezada aún
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (1,'Fiesta para todos!!! ÚNETE!',NULL, 'WAITING',NULL,NULL,6,3,false);

INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (5,'Fiesta ',NULL, 'WAITING',NULL,NULL,5,3,false);

INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (4,'nerea!','DEF345', 'WAITING',NULL,NULL,5,3,true);

-- Partida en progreso 
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (2,'Partida en curso','ABC123','PLAYING','2025-10-26 20:00:00',NULL,5,3,false);

-- Partida finalizada
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (3,'Partida terminada','XYZ789','FINISHED','2025-10-26 18:00:00','2025-10-26 19:00:00',4,3,true);

INSERT INTO cards(id,front_image, back_image,letter ) 
VALUES (1,'/resources/images/cards/Carta 1.jpg','/resources/images/cards/backCard.jpg','A'),
        (2,'/resources/images/cards/Carta 2.jpg','/resources/images/cards/backCard.jpg','A'),
        (3,'/resources/images/cards/Carta 3.jpg','/resources/images/cards/backCard.jpg','A'),
        (4,'/resources/images/cards/Carta 4.jpg','/resources/images/cards/backCard.jpg','A'),
        (5,'/resources/images/cards/Carta 5.jpg','/resources/images/cards/backCard.jpg','A'),
        (6,'/resources/images/cards/Carta 6.jpg','/resources/images/cards/backCard.jpg','A'),
        (7,'/resources/images/cards/Carta 7.jpg','/resources/images/cards/backCard.jpg','A'),
        (8,'/resources/images/cards/Carta 8.jpg','/resources/images/cards/backCard.jpg','A'),
        (9,'/resources/images/cards/Carta 9.jpg','/resources/images/cards/backCard.jpg','A'),
        (10,'/resources/images/cards/Carta 10.jpg','/resources/images/cards/backCard.jpg','A'),
        (11,'/resources/images/cards/Carta 11.jpg','/resources/images/cards/backCard.jpg','B'),
        (12,'/resources/images/cards/Carta 12.jpg','/resources/images/cards/backCard.jpg','B'),
        (13,'/resources/images/cards/Carta 13.jpg','/resources/images/cards/backCard.jpg','B'),
        (14,'/resources/images/cards/Carta 14.jpg','/resources/images/cards/backCard.jpg','B'),
        (15,'/resources/images/cards/Carta 15.jpg','/resources/images/cards/backCard.jpg','C'),
        (16,'/resources/images/cards/Carta 16.jpg','/resources/images/cards/backCard.jpg','C'),
        (17,'/resources/images/cards/Carta 17.jpg','/resources/images/cards/backCard.jpg','C'),
        (18,'/resources/images/cards/Carta 18.jpg','/resources/images/cards/backCard.jpg','C'),
        (19,'/resources/images/cards/Carta 19.jpg','/resources/images/cards/backCard.jpg','C'),
        (20,'/resources/images/cards/Carta 20.jpg','/resources/images/cards/backCard.jpg','C'),
        (21,'/resources/images/cards/Carta 21.jpg','/resources/images/cards/backCard.jpg','E'),
        (22,'/resources/images/cards/Carta 22.jpg','/resources/images/cards/backCard.jpg','E'),
        (23,'/resources/images/cards/Carta 23.jpg','/resources/images/cards/backCard.jpg','E'),
        (24,'/resources/images/cards/Carta 24.jpg','/resources/images/cards/backCard.jpg','E'),
        (25,'/resources/images/cards/Carta 25.jpg','/resources/images/cards/backCard.jpg','E'),
        (26,'/resources/images/cards/Carta 26.jpg','/resources/images/cards/backCard.jpg','E'),
        (27,'/resources/images/cards/Carta 27.jpg','/resources/images/cards/backCard.jpg','E'),
        (28,'/resources/images/cards/Carta 28.jpg','/resources/images/cards/backCard.jpg','E'),
        (29,'/resources/images/cards/Carta 29.jpg','/resources/images/cards/backCard.jpg','E'),
        (30,'/resources/images/cards/Carta 30.jpg','/resources/images/cards/backCard.jpg','E'),
        (31,'/resources/images/cards/Carta 31.jpg','/resources/images/cards/backCard.jpg','E'),
        (32,'/resources/images/cards/Carta 32.jpg','/resources/images/cards/backCard.jpg','E'),
        (33,'/resources/images/cards/Carta 33.jpg','/resources/images/cards/backCard.jpg','F'),
        (34,'/resources/images/cards/Carta 34.jpg','/resources/images/cards/backCard.jpg','F'),
        (35,'/resources/images/cards/Carta 35.jpg','/resources/images/cards/backCard.jpg','F'),
        (36,'/resources/images/cards/Carta 36.jpg','/resources/images/cards/backCard.jpg','F'),
        (37,'/resources/images/cards/Carta 37.jpg','/resources/images/cards/backCard.jpg','L'),
        (38,'/resources/images/cards/Carta 38.jpg','/resources/images/cards/backCard.jpg','L'),
        (39,'/resources/images/cards/Carta 39.jpg','/resources/images/cards/backCard.jpg','L'),
        (40,'/resources/images/cards/Carta 40.jpg','/resources/images/cards/backCard.jpg','L'),
        (41,'/resources/images/cards/Carta 41.jpg','/resources/images/cards/backCard.jpg','M'),
        (42,'/resources/images/cards/Carta 42.jpg','/resources/images/cards/backCard.jpg','M'),
        (43,'/resources/images/cards/Carta 43.jpg','/resources/images/cards/backCard.jpg','M'),
        (44,'/resources/images/cards/Carta 44.jpg','/resources/images/cards/backCard.jpg','M'),
        (45,'/resources/images/cards/Carta 45.jpg','/resources/images/cards/backCard.jpg','O'),
        (46,'/resources/images/cards/Carta 46.jpg','/resources/images/cards/backCard.jpg','O'),
        (47,'/resources/images/cards/Carta 47.jpg','/resources/images/cards/backCard.jpg','O'),
        (48,'/resources/images/cards/Carta 48.jpg','/resources/images/cards/backCard.jpg','O'),
        (49,'/resources/images/cards/Carta 49.jpg','/resources/images/cards/backCard.jpg','P'),
        (50,'/resources/images/cards/Carta 50.jpg','/resources/images/cards/backCard.jpg','P'),
        (51,'/resources/images/cards/Carta 51.jpg','/resources/images/cards/backCard.jpg','P'),
        (52,'/resources/images/cards/Carta 52.jpg','/resources/images/cards/backCard.jpg','P'),
        (53,'/resources/images/cards/Carta 53.jpg','/resources/images/cards/backCard.jpg','R'),
        (54,'/resources/images/cards/Carta 54.jpg','/resources/images/cards/backCard.jpg','R'),
        (55,'/resources/images/cards/Carta 55.jpg','/resources/images/cards/backCard.jpg','R'),
        (56,'/resources/images/cards/Carta 56.jpg','/resources/images/cards/backCard.jpg','R'),
        (57,'/resources/images/cards/Carta 57.jpg','/resources/images/cards/backCard.jpg','R'),
        (58,'/resources/images/cards/Carta 58.jpg','/resources/images/cards/backCard.jpg','R'),
        (59,'/resources/images/cards/Carta 59.jpg','/resources/images/cards/backCard.jpg','R'),
        (60,'/resources/images/cards/Carta 60.jpg','/resources/images/cards/backCard.jpg','R'),
        (61,'/resources/images/cards/Carta 61.jpg','/resources/images/cards/backCard.jpg','S'),
        (62,'/resources/images/cards/Carta 62.jpg','/resources/images/cards/backCard.jpg','S'),
        (63,'/resources/images/cards/Carta 63.jpg','/resources/images/cards/backCard.jpg','S'),
        (64,'/resources/images/cards/Carta 64.jpg','/resources/images/cards/backCard.jpg','S'); 


        
        
