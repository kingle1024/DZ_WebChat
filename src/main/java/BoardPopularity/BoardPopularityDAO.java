package BoardPopularity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardPopularityDAO implements BoardPopularityRepository{
    private Connection con;

    private PreparedStatement pstmt;
    private static DataSource dataFactory;
    public static void setDataFactory(DataSource dataFactory) {
        BoardPopularityDAO.dataFactory = dataFactory;
    }
    private void open(){
        try{
            con = dataFactory.getConnection();
        } catch (SQLException e){
            throw new RuntimeException();
        }
    }
    private void close() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }

            if (con != null) {
                con.close();
//                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public long findByBnoAndType(String bno, String type){
        try{
            open();
            String query = "select count(*) " +
                    "from board_popularity " +
                    "where bno = ? and type = ? and isDelete = 0";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, bno);
            pstmt.setString(2, type);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getInt(1);
            }else{
                return 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public BoardPopularity findByBnoAndUserIdAndIsDelete(String bno, String userId){
        try{
            open();
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
            open();
            String query = "insert into board_popularity(bno, userId, type, isDelete, register_date) values(?, ?, ?, 0, now())";
            pstmt = con.prepareStatement(query);

            pstmt.setInt(1, boardPopularity.getBno());
            pstmt.setString(2, boardPopularity.getUserId());
            pstmt.setString(3, boardPopularity.getType());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public boolean update(BoardPopularity boardPopularity){
        try {
            open();
            String query = "update board_popularity " +
                    "set bno = ?, userId = ?, type = ?, isDelete = ? " +
                    "where bno = ? and userId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, boardPopularity.getBno());
            pstmt.setString(2, boardPopularity.getUserId());
            pstmt.setString(3, boardPopularity.getType());
            pstmt.setBoolean(4, boardPopularity.isDelete());
            pstmt.setInt(5, boardPopularity.getBno());
            pstmt.setString(6, boardPopularity.getUserId());
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }
}
