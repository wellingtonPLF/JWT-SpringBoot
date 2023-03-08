--Password: 12345678

INSERT INTO roles(idrole, role_name)
VALUES (1, ''ROLE_ADMIN'');
		
INSERT INTO roles(idrole, role_name)
VALUES (2, ''ROLE_USER'');	

INSERT INTO usuario(iduser, name, password, email, datanasc)
VALUES (1, 'Well', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG', 
'well@gmail.com', '2023-01-01');	

INSERT INTO usuario(iduser, name, password, email, datanasc)
VALUES (2, 'Lara', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG', 
'lara@gmail.com', '2022-03-18');	

INSERT INTO usuario(iduser, name, password, email, datanasc)
VALUES (3, 'Luiza', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG', 
'luiza@gmail.com', '2001-03-04');

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 1);
		
INSERT INTO user_roles(user_id, role_id)
VALUES (2, 2);
		
INSERT INTO user_roles(user_id, role_id)
VALUES (3, 2);