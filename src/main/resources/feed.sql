/*
DO 
'
BEGIN
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 1) THEN
		INSERT INTO usuario(id, name, password, email, datanasc)
		VALUES (1, ''Well'', ''123'', ''well@gmail.com'', ''2023-01-01'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 2) THEN
		INSERT INTO usuario(id, name, password, email, datanasc)
		VALUES (2, ''Lara'', ''1234'', ''lara@gmail.com'', ''2022-03-18'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 3) THEN
		INSERT INTO usuario(id, name, password, email, datanasc)
		VALUES (3, ''Joe'', ''12358'', ''joe@gmail.com'', ''2020-06-25'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 4) THEN
		INSERT INTO usuario(id, name, password, email, datanasc)
		VALUES (4, ''Janna'', ''4545'', ''janna@gmail.com'', ''2015-08-09'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = ) THEN
		INSERT INTO usuario(id, name, password, datanasc)
		VALUES (, '''', '''', '''');	
	END IF; 
END;
' LANGUAGE PLPGSQL;
*/