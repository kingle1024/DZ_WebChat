package Auth;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MemberDAO {
    private final Connection con;
    private PreparedStatement pstmt;

    public MemberDAO() throws NamingException {
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
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isExisted(Member member) throws SQLException {
        boolean result;
        String id = member.getUserId();
        String pwd = member.getPwd();

//        con = dataFactory.getConnection();
        String query = "select if(count(*) = 1, 'true', 'false') as result " +
                "from member " +
                "where userid = ? and pwd = ?";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, id);
        pstmt.setString(2, pwd);
        ResultSet rs = pstmt.executeQuery();
        rs.next();

        result = Boolean.parseBoolean(rs.getString("result"));

        return result;
    }

    public Member viewMember(String id){
        System.out.println("viewMember:"+id);
        String query = "select * from member where userid = ?";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            Member member = null;
            if(rs.next()){
                member = new Member();
//                Member.builder()
//                        .userId(rs.getString("id"))
//                        .
//                        .build();
                member.setUserId(rs.getString("userId"));
                member.setPwd(rs.getString("pwd"));
                member.setName(rs.getString("name"));
                member.setPhone(rs.getString("phone"));
            }

            return member;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public synchronized void insertMember(Member member) throws Member.ExistMember {
        try{
            pstmt = con.prepareStatement("insert into member" +
                    "(userid, pwd, name, phone, email, createdate) " +
                    "values(?, ?, ?, ?, ?, ?)");

            //멤버 정보 설정
            pstmt.setString(1, member.getUserId());
            pstmt.setString(2, member.getPwd());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, String.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            close();
        }
    }

}
