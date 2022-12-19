package File;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardFileDAO {
    private Connection con;

    private PreparedStatement pstmt;
    private static DataSource dataFactory;

    public static void setDataFactory(DataSource dataFactory) {
        BoardFileDAO.dataFactory = dataFactory;
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

    public boolean insert(BoardFile boardFile){
        try{
            open();
            String query = "insert into board_file" +
                    "(number, org_name, real_name, content_type, length)" +
                    "values (?, ?, ?, ?, ?) ";
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, boardFile.getNumber());
            pstmt.setString(2, boardFile.getOrgName());
            pstmt.setString(3, boardFile.getRealName());
            pstmt.setString(4, boardFile.getContentType());
            pstmt.setInt(5, boardFile.getLength());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e){
            throw new RuntimeException();
        } finally {
            close();
        }
    }

    public List<BoardFile> list(String bno){
        try{
            open();
            String query = "select * from board_file where number = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, bno);

            ResultSet rs = pstmt.executeQuery();
            List<BoardFile> boardFiles = new ArrayList<>();
            while(rs.next()){
                BoardFile boardFile = BoardFile.builder()
                        .number(rs.getInt("number"))
                        .orgName(rs.getString("org_name"))
                        .realName(rs.getString("real_name"))
                        .ContentType(rs.getString("content_type"))
                        .length(rs.getInt("length"))
                        .build();
                boardFiles.add(boardFile);
            }

            return boardFiles;
        } catch (SQLException e){
            throw new RuntimeException();
        } finally {
            close();
        }
    }
}
