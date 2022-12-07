package Auth;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    final Connection con;
    PreparedStatement pstmt;

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
                                .email(rs.getString("email"))
                                .name(rs.getString("name"))
                                .phone(rs.getString("phone"))
                                .isAdmin(rs.getBoolean("isAdmin"))
                                .userStatus(rs.getString("userStatus"))
                                .createdate(rs.getTimestamp("createdate").toLocalDateTime())
                                .build();

                Timestamp loginDateTime = rs.getTimestamp("LOGINDATETIME");
                if(loginDateTime != null){
                    LocalDateTime localDateTime = loginDateTime.toLocalDateTime();
                    member.setLoginDateTime(localDateTime);
                }
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
    public boolean edit(Member member){
        String query = "update member " +
                "set pwd = ?, name = ?, email = ?, phone = ? " +
                "where userId=?";
        try {
            System.out.println("edit member:"+member);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, member.getPwd());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getUserId());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Member> list(String search){
        try {
            String query = "select * from member where name like ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "%"+search+"%");

            ResultSet rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();

            while(rs.next()){


                Member m = Member.builder()
                        .userId(rs.getString("userid"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .email(rs.getString("email"))
                        .isAdmin(rs.getBoolean("isAdmin"))
                        .userStatus(rs.getString("userStatus"))
                        .createdate(rs.getTimestamp("createdate").toLocalDateTime())
//                        .loginDateTime(rs.getTimestamp("LOGINDATETIME").toLocalDateTime())
                        .build();

                Timestamp loginDateTime = rs.getTimestamp("LOGINDATETIME");
                if(loginDateTime != null){
                    LocalDateTime localDateTime = loginDateTime.toLocalDateTime();
                    m.setLoginDateTime(localDateTime);
                }

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
            return result > 0;
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
            return result > 0;
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
