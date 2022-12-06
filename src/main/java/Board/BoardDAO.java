package Board;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    final Connection con;

    PreparedStatement pstmt;
    public BoardDAO() throws NamingException {
        Context ctx = new InitialContext();
        Context envContext = (Context) ctx.lookup("java:/comp/env");
        DataSource dataFactory = (DataSource) envContext.lookup("jdbc/maria");
        try {
            con = dataFactory.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Board> list(String search){
        try{
            String query = "select * from boards where btitle like ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "%"+search+"%");

            ResultSet rs = pstmt.executeQuery();
            List<Board> boards = new ArrayList<>();
            while(rs.next()){
                Board b = Board.builder()
                        .bno(rs.getString("bno"))
                        .btitle(rs.getString("btitle"))
                        .bcontent(rs.getString("bcontent"))
                        .bwriter(rs.getString("bwriter"))
                        .bhit(rs.getInt("bhit"))
                        .build();
                boards.add(b);
            }
            return boards;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Board viewBoard(String no){
        String query = "select * from boards where bno = ?";
        try{
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, no);
            ResultSet rs = pstmt.executeQuery();
            Board board = null;

            if(rs.next()){
                board = Board.builder()
                        .bno(rs.getString("bno"))
                        .btitle(rs.getString("btitle"))
                        .bcontent(rs.getString("bcontent"))
                        .bwriter(rs.getString("bwriter"))
                        .bhit(rs.getInt("bhit"))
                        .build();
            }
            return board;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addHit(String no){
        String query = "UPDATE boards " +
                "SET bhit = bhit+1 " +
                "WHERE bno = ?";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, no);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
