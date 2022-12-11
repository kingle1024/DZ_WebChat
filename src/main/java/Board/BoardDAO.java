package Board;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
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
    private void close() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Board> list(String search, String type){
        try{
            String query = "select * " +
                    "from boards " +
                    "where isDelete = 0 " +
                    "and type = ? " +
                    "and btitle like ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, type);
            pstmt.setString(2, "%"+search+"%");

            ResultSet rs = pstmt.executeQuery();
            List<Board> boards = new ArrayList<>();
            while(rs.next()){
                Board b = Board.builder()
                        .bno(rs.getString("bno"))
                        .btitle(rs.getString("btitle"))
                        .bcontent(rs.getString("bcontent"))
                        .bwriter(rs.getString("bwriter"))
                        .bhit(rs.getInt("bhit"))
                        .bdate(rs.getTimestamp("bdate").toLocalDateTime())
                        .build();
                boards.add(b);
            }
            return boards;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
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
                        .bwriterId(rs.getString("bwriterId"))
                        .bhit(rs.getInt("bhit"))
                        .build();
            }
            return board;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean findByNoAndPassword(String no, String password){
        String query = "select * from boards where bno = ? and password = ?";
        try{
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, no);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
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
        }finally {
            close();
        }
    }
    public boolean del(String bno){
        CallableStatement cstmt = null;
        try {
            cstmt = con.prepareCall("{call DEL_MEMBER(?)}");
            cstmt.setString(1, bno);
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            callableClose(cstmt);
        }
    }

    private static void callableClose(CallableStatement cstmt) {
        try {
            if(cstmt != null) cstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insert(Board board){
        try {
            String query = "insert into boards" +
                    "(btitle, bwriter, bcontent, bhit, type, isDelete, bwriterId) " +
                    "values (?, ?, ?, ?, ?, 0, ?)";
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBwriter());
            pstmt.setString(3, board.getBcontent());
            pstmt.setInt(4, board.getBhit());
            pstmt.setString(5, board.getType());
            pstmt.setString(6, board.getBwriterId());
//            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }

    }

    public boolean edit(Board board) {
        String query = "update boards " +
                "set btitle = ?, bcontent = ? " +
                "where bno = ?";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBcontent());
            pstmt.setString(3, board.getBno());

            int result = pstmt.executeUpdate();
            return result > 0 ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }
}
