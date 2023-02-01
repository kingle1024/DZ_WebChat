create
    definer = root@localhost procedure INSERT_BOARD(
        IN p_title varchar(100),
        IN p_writer varchar(100),
        IN p_content varchar(100),
        IN p_hit int,
        IN p_type varchar(100),
        IN p_isDelete tinyint,
        IN p_writerId varchar(100),
        IN p_password varchar(100),
        OUT total int
    )

BEGIN 
    START TRANSACTION;
        INSERT INTO boards
            (btitle, bwriter, bcontent, bhit, type, isDelete, bwriterId, password)
        VALUES (p_title, p_writer, p_content, p_hit, p_type, p_isDelete, p_writerId, p_password);

        SELECT LAST_INSERT_ID()
        INTO total;

    COMMIT;   
END;

