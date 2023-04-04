
--Password: 12345678

DO 
'
BEGIN
	IF NOT EXISTS (SELECT role_id FROM roles WHERE role_id = 1) THEN
		INSERT INTO roles(role_id, role_name)
		VALUES (1, ''ROLE_ADMIN'');
	END IF;
	
	IF NOT EXISTS (SELECT role_id FROM roles WHERE role_id = 2) THEN
		INSERT INTO roles(role_id, role_name)
		VALUES (2, ''ROLE_USER'');	
	END IF;
	
	IF NOT EXISTS (SELECT auth_id FROM auth WHERE auth_id = 1) THEN
		INSERT INTO auth(auth_id, email, username, password)
		VALUES (1, ''well@gmail.com'', ''well'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'');	
	END IF;
	
	IF NOT EXISTS (SELECT auth_id FROM auth WHERE auth_id = 2) THEN
		INSERT INTO auth(auth_id, email, username, password)
		VALUES (2, ''lara@gmail.com'', ''lara'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'');	
	END IF;
	
	IF NOT EXISTS (SELECT auth_id FROM auth WHERE auth_id = 3) THEN
		INSERT INTO auth(auth_id, email, username, password)
		VALUES (3, ''john@gmail.com'', ''john'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'');	
	END IF;

	IF NOT EXISTS (SELECT user_id FROM usuario WHERE user_id = 1) THEN
		INSERT INTO usuario(user_id, nickname, borndate, auth_id)
		VALUES (1, ''Wellington'', ''2023-01-01'', 1);	
	END IF;
		
	IF NOT EXISTS (SELECT user_id FROM usuario WHERE user_id = 2) THEN
		INSERT INTO usuario(user_id, nickname, borndate, auth_id)
		VALUES (2, ''Larissa'', ''2022-03-18'', 2);	
	END IF;
	
	IF NOT EXISTS (SELECT user_id FROM usuario WHERE user_id = 3) THEN
		INSERT INTO usuario(user_id, nickname, borndate, auth_id)
		VALUES (3, ''John Wick'', ''2020-06-25'', 3);	
	END IF;
	
	IF NOT EXISTS (SELECT * FROM auth_roles WHERE auth_id = 1 and role_id = 1) THEN
		INSERT INTO auth_roles(auth_id, role_id)
		VALUES (1, 1);
	END IF;

	IF NOT EXISTS (SELECT * FROM auth_roles WHERE auth_id = 2 and role_id = 2) THEN
		INSERT INTO auth_roles(auth_id, role_id)
		VALUES (2, 2);
	END IF;

	IF NOT EXISTS (SELECT * FROM auth_roles WHERE auth_id = 3 and role_id = 2) THEN
		INSERT INTO auth_roles(auth_id, role_id)
		VALUES (3, 2);
	END IF;
END
'  LANGUAGE PLPGSQL;

