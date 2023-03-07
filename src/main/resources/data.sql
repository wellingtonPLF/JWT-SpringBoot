--Password: 12345678

INSERT INTO usuario(id, name, password, email, datanasc)
VALUES (1, 'Well', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG', 
'well@gmail.com', '2023-01-01');	

INSERT INTO usuario(id, name, password, email, datanasc)
VALUES (2, 'Lara', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG', 
'lara@gmail.com', '2022-03-18');	

INSERT INTO usuario(id, name, password, email, datanasc)
VALUES (3, 'Luiza', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG', 
'luiza@gmail.com', '2001-03-04');