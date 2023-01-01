package Mvc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BaseDAO {
    protected Connection con;
    protected PreparedStatement pstmt;


    public BaseDAO() {
        try{
            con = WebUtil.getConnection();
            con.setAutoCommit(false);
        } catch (SQLException e){
            throw new RuntimeException();
        }
    }

    protected void close() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
