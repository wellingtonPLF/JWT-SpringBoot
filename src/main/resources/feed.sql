
--Password: 12345678

DO 
'
BEGIN
	IF NOT EXISTS (SELECT idrole FROM roles WHERE idrole = 1) THEN
		INSERT INTO roles(idrole, role_name)
		VALUES (1, ''ROLE_ADMIN'');
	END IF;
	
	IF NOT EXISTS (SELECT idrole FROM roles WHERE idrole = 2) THEN
		INSERT INTO roles(idrole, role_name)
		VALUES (2, ''ROLE_USER'');	
	END IF;
	
	IF NOT EXISTS (SELECT iduser FROM usuario WHERE iduser = 1) THEN
		INSERT INTO usuario(iduser, username, password, email, datanasc)
		VALUES (1, ''Well'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'', 
		''well@gmail.com'', ''2023-01-01'');	
	END IF;
	
	IF NOT EXISTS (SELECT iduser FROM usuario WHERE iduser = 2) THEN
		INSERT INTO usuario(iduser, username, password, email, datanasc)
		VALUES (2, ''Lara'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'', 
		''lara@gmail.com'', ''2022-03-18'');	
	END IF;
	
	IF NOT EXISTS (SELECT iduser FROM usuario WHERE iduser = 3) THEN
		INSERT INTO usuario(iduser, username, password, email, datanasc)
		VALUES (3, ''Joe'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'', 
		''joe@gmail.com'', ''2020-06-25'');	
	END IF;
	
	IF NOT EXISTS (SELECT * FROM user_roles WHERE user_id = 1 and role_id = 1) THEN
		INSERT INTO user_roles(user_id, role_id)
		VALUES (1, 1);
	END IF;

	IF NOT EXISTS (SELECT * FROM user_roles WHERE user_id = 2 and role_id = 2) THEN
		INSERT INTO user_roles(user_id, role_id)
		VALUES (2, 2);
	END IF;

	IF NOT EXISTS (SELECT * FROM user_roles WHERE user_id = 3 and role_id = 2) THEN
		INSERT INTO user_roles(user_id, role_id)
		VALUES (3, 2);
	END IF;
END
'  LANGUAGE PLPGSQL;
