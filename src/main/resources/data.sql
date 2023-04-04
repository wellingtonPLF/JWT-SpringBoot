--Password: 12345678

INSERT INTO roles(idrole, role_name)
VALUES (1, 'ROLE_ADMIN');
		
INSERT INTO roles(idrole, role_name)
VALUES (2, 'ROLE_USER');	

INSERT INTO auth(auth_id, email, username, password)
VALUES (1, 'well@gmail.com', 'well', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG');	

INSERT INTO auth(auth_id, email, username, password)
VALUES (2, 'lara@gmail.com', 'lara', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG');

INSERT INTO auth(auth_id, email, username, password)
VALUES (3, 'john@gmail.com', 'john', 
'{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG');	

INSERT INTO usuario(user_id, nickname, borndate, auth_id)
VALUES (1, 'Wellington', '2023-01-01', 1);	
		
INSERT INTO usuario(user_id, nickname, borndate, auth_id)
VALUES (2, 'Larissa', '2022-03-18', 2);
	
INSERT INTO usuario(user_id, nickname, borndate, auth_id)
VALUES (3, 'John Wick', '2020-06-25', 3);

INSERT INTO user_roles(user_id, role_id)
VALUES (1, 1);
		
INSERT INTO user_roles(user_id, role_id)
VALUES (2, 2);
		
INSERT INTO user_roles(user_id, role_id)
VALUES (3, 2);