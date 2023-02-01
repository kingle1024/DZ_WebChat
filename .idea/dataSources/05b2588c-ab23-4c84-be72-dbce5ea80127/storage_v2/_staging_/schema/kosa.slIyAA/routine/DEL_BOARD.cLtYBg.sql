create
    definer = root@localhost procedure DEL_BOARD(IN p_bno int)
BEGIN
    START TRANSACTION;
        UPDATE boards
        SET isDelete = 1
        WHERE bno = p_bno;

        INSERT INTO del_board (bno, del_date)
        VALUES (p_bno, now());
    COMMIT ;
END;

