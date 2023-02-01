create
    definer = root@localhost procedure INSERT_REPLY_BOARD(IN p_parentNo varchar(100), IN p_title varchar(100), IN p_writer varchar(100),
                                                          IN p_content varchar(100), IN p_hit int, IN p_type varchar(100),
                                                          IN p_isDelete tinyint, IN p_writerId varchar(100),
                                                          IN p_password varchar(100), OUT total int)
BEGIN 
-- missing source code
    START TRANSACTION;
        INSERT INTO boards
        (btitle, bwriter, bcontent, bhit, type, isDelete, bwriterId, password, parentNo)
        VALUES (p_title,
                p_writer,
                p_content,
                p_hit,
                p_type,
                p_isDelete,
                p_writerId,
                p_password,
                p_parentNo
                );

    COMMIT;
END;

