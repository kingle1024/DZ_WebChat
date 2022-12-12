package BoardPopularity;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardPopularityDAO {
    final Connection con;

    PreparedStatement pstmt;
    public BoardPopularityDAO() throws NamingException {
        Context ctx = new InitialContext();
        Context envContext = (Context) ctx.lookup("java:/comp/env");
        DataSource dataFactory = (DataSource) envContext.lookup("jdbc/maria");
        try {
            con = dataFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void close() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BoardPopularity findByBnoAndUserIdAndIsDelete(String bno, String userId){
        try{
            String query = "select * from board_popularity where bno = ? and userId = ? and isDelete = 0";

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, bno);
            pstmt.setString(2, userId);
            ResultSet rs = pstmt.executeQuery();
            BoardPopularity boardPopularity = null;

            if(rs.next()){
                boardPopularity = new BoardPopularity();
                boardPopularity.setBno(rs.getInt("bno"));
                boardPopularity.setUserId(rs.getString("userId"));
                boardPopularity.setType(rs.getString("type"));
            }
            return boardPopularity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public boolean insert(BoardPopularity boardPopularity){
        try {
            String query = "insert into board_popularity(bno, userId, type, isDelete, register_date) values(?, ?, ?, 0, now())";
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, boardPopularity.getBno());
            pstmt.setString(2, boardPopularity.getUserId());
            pstmt.setString(3, boardPopularity.getType());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(BoardPopularity boardPopularity){
        String query = "update board_popularity " +
                "set bno = ?, userId = ?, type = ?";
        return true;
    }
}
