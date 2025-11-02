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

-- Partida en progreso 
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (2,'Partida en curso','ABC123','PLAYING','2025-10-26 20:00:00',NULL,5,3,false);

-- Partida finalizada
INSERT INTO match(id,name,code,status,start_time,end_time,max_players,min_players,is_private)
VALUES (3,'Partida terminada','XYZ789','FINISHED','2025-10-26 18:00:00','2025-10-26 19:00:00',4,3,true);