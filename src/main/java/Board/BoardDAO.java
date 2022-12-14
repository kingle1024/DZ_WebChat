package Board;

import BoardPopularity.BoardPopularity;
import Page.BoardParam;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO implements BoardRepository{
    private Connection con;

    private PreparedStatement pstmt;
    private static DataSource dataFactory;

    public static void setDataFactory(DataSource dataFactory) {
        BoardDAO.dataFactory = dataFactory;
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public long listSize(String search, String type){
        try{
            open();
            String query = "select count(*) " +
                    "from boards " +
                    "where isDelete = 0 " +
                    "and type = ? " +
                    "and btitle like ? ";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, type);
            pstmt.setString(2, "%"+search+"%");

            ResultSet rs = pstmt.executeQuery();
            rs.next();

            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }
    public List<Board> list(String search, String type, BoardParam boardParam){
        try{
            open();
            String query = "select * " +
                    "from boards " +
                    "where isDelete = 0 " +
                    "and type = ? " +
                    "and btitle like ? order by bno desc " +
                    "limit "+ boardParam.getPageStart()+", " + boardParam.getPageEnd();

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
            open();
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
                        .password(rs.getString("password"))
                        .build();
            }
            return board;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }
    public boolean findByNoAndPassword(String no, String password){
        String query = "select * from boards where bno = ? and password = ?";
        try{
            open();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, no);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }
    public void addHit(String no){
        String query = "UPDATE boards " +
                "SET bhit = bhit+1 " +
                "WHERE bno = ?";
        try {
            open();
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
            open();
            cstmt = con.prepareCall("{call DEL_BOARD(?)}");
            cstmt.setString(1, bno);
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            callableClose(cstmt);
            close();
        }
    }

    private static void callableClose(CallableStatement cstmt) {
        try {
            if(cstmt != null) cstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public BoardPopularity findByBnoAndUserId(String bno, String userId){
        try{
            open();
            String query = "select * from board_popularity where bno = ? and userId = ?";

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
    public boolean boardPopularity(String bno, String userId){
        try {
            open();
            String query = "insert into board_popularity(bno, userId, type) values(?, ?, now())";
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, bno);
            pstmt.setString(2, userId);

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(Board board){
        try {
            open();
            String query = "insert into boards" +
                    "(btitle, bwriter, bcontent, bhit, type, isDelete, bwriterId, password) " +
                    "values (?, ?, ?, ?, ?, 0, ?, ?)";
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBwriter());
            pstmt.setString(3, board.getBcontent());
            pstmt.setInt(4, board.getBhit());
            pstmt.setString(5, board.getType());
            pstmt.setString(6, board.getBwriterId());
            pstmt.setString(7, board.getPassword());

            pstmt.executeUpdate();
            pstmt.close();

            query = "SELECT LAST_INSERT_ID()";
            pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            int number = -1;
            if(rs.next()){
                number = rs.getInt(1);
            }

            return number;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }

    public boolean edit(Board board) {
        String query = "update boards " +
                "set btitle = ?, bcontent = ?, bwriter = ? " +
                "where bno = ?";
        try {
            open();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, board.getBtitle());
            pstmt.setString(2, board.getBcontent());
            pstmt.setString(3, board.getBwriter());
            pstmt.setString(4, board.getBno());

            int result = pstmt.executeUpdate();
            return result > 0 ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }
}
