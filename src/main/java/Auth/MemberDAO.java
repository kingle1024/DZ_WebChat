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
import java.util.ArrayList;
import java.util.List;

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
        String query = "select * from member where userid = ?";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            Member member = null;

            if(rs.next()){
                member = Member.builder()
                                .userId(rs.getString("USERID"))
                                .pwd(rs.getString("PWD"))
                                .name(rs.getString("name"))
                                .phone(rs.getString("phone"))
                                .isAdmin(rs.getBoolean("isAdmin"))
                                .userStatus(rs.getString("userStatus"))
                                .build();
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
    public List<Member> list(){
        String query = "select * from member";

        try {
            pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();

            while(rs.next()){
                Member m  = new Member();
                m.setUserId(rs.getString("userid"));
                m.setName(rs.getString("name"));
                m.setPhone(rs.getString("phone"));
                members.add(m);
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userStatus_noUse(String id){
        try {

            String query = "update member " +
                    "set userStatus = ?" +
                    "where userid = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "NOUSE");
            pstmt.setString(2, id);
            int result = pstmt.executeUpdate();
            if(result > 0) return true;
            else return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userStatus_use(String id) {
        try {

            String query = "update member " +
                    "set userStatus = ?" +
                    "where userid = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "USE");
            pstmt.setString(2, id);
            int result = pstmt.executeUpdate();
            if(result > 0) return true;
            else return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean userStatus_withdraw(String id){
        try {
            String query = "update member " +
                    "set userStatus = ?" +
                    "where userid = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "STOP");
            pstmt.setString(2, id);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
