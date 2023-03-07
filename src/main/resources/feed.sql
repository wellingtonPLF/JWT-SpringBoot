
--Password: 12345678

DO 
'
BEGIN
	IF NOT EXISTS (SELECT id FROM roles WHERE id = 1) THEN
		INSERT INTO roles(id, role_name)
		VALUES (1, ''ROLE_ADMIN'');
	END IF;
	
	IF NOT EXISTS (SELECT id FROM roles WHERE id = 2) THEN
		INSERT INTO roles(id, role_name)
		VALUES (2, ''ROLE_USER'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 1) THEN
		INSERT INTO usuario(id, username, password, email, datanasc)
		VALUES (1, ''Well'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'', 
		''well@gmail.com'', ''2023-01-01'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 2) THEN
		INSERT INTO usuario(id, username, password, email, datanasc)
		VALUES (2, ''Lara'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'', 
		''lara@gmail.com'', ''2022-03-18'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 3) THEN
		INSERT INTO usuario(id, username, password, email, datanasc)
		VALUES (3, ''Joe'', 
		''{bcrypt}$2a$10$dQWjpRaxiORPCqh4hEQVW.Ka.FkRLdzEvoSPJ26pQ6I7Fqo4wrDzG'', 
		''joe@gmail.com'', ''2020-06-25'');	
	END IF;
	
	IF NOT EXISTS (SELECT * FROM user_roles WHERE user_id = 1 and role_id = 1) THEN
		insert into user_roles(user_id, role_id)
		values (1, 1);
	END IF;

	IF NOT EXISTS (SELECT * FROM user_roles WHERE user_id = 2 and role_id = 2) THEN
		insert into user_roles(user_id, role_id)
		values (2, 2);
	END IF;

	IF NOT EXISTS (SELECT * FROM user_roles WHERE user_id = 3 and role_id = 2) THEN
		insert into user_roles(user_id, role_id)
		values (3, 2);
	END IF;
		
	/*
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = ) THEN
		INSERT INTO usuario(id, username, password, datanasc)
		VALUES (, '''', '''', '''');	
	END IF;
 	*/
END
'  LANGUAGE PLPGSQL;
