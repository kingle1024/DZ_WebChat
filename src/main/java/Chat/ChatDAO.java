package Chat;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO implements ChatRepository{
    private Connection con;

    private PreparedStatement pstmt;
    private static DataSource dataFactory;

    public static void setDataFactory(DataSource dataFactory) {
        ChatDAO.dataFactory = dataFactory;
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
    public List<ChatUrl> list(){
        try {
            open();
            List<ChatUrl> list = new ArrayList<>();
            String query = "select * from tb_auth_url where use_yn = 'Y'";
            pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                list.add(new ChatUrl(rs.getString("url")));
            }

            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return null;
    }
}
