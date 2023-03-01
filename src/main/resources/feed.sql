DO 
'
BEGIN
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 1) THEN
		INSERT INTO usuario(id, name, password, datanasc)
		VALUES (1, ''Well'', ''123'', ''2023-01-01'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 2) THEN
		INSERT INTO usuario(id, name, password, datanasc)
		VALUES (2, ''Lara'', ''1234'', ''2022-03-18'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 3) THEN
		INSERT INTO usuario(id, name, password, datanasc)
		VALUES (3, ''Joe'', ''12358'', ''2020-06-25'');	
	END IF;
	
	IF NOT EXISTS (SELECT id FROM usuario WHERE id = 5) THEN
		INSERT INTO usuario(id, name, password, datanasc)
		VALUES (4, ''Janna'', ''4545'', ''2015-08-09'');	
	END IF;
	
--	IF NOT EXISTS (SELECT id FROM usuario WHERE id = ) THEN
	--	INSERT INTO usuario(id, name, password, datanasc)
	--	VALUES (, '''', '''', '''');	
--	END IF;
END;
' LANGUAGE PLPGSQL;