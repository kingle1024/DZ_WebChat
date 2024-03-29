package Member;

import Page.BoardParam;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO implements MemberRepository{
    private Connection con;
    private PreparedStatement pstmt;
    private static DataSource dataFactory;

    public static void setDataFactory(DataSource dataFactory) {
        MemberDAO.dataFactory = dataFactory;
    }

    private void open() {
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
            if(con != null){
                // con.commit();
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Member findByIdAndPassword(String id, String pwd){
        open();
        String query = "select * from member where userid = ? and pwd = ?";

        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setString(2, pwd);
            ResultSet rs = pstmt.executeQuery();

            Member member = null;
            if (rs.next()) {
                member = Member.builder()
                        .userId(rs.getString("USERID"))
                        .pwd(rs.getString("PWD"))
                        .email(rs.getString("email"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .isAdmins(rs.getBoolean("isAdmin"))
                        .userStatus(rs.getString("userStatus"))
                        .createdate(rs.getTimestamp("createdate").toLocalDateTime())
                        .build();

                Timestamp loginDateTime = rs.getTimestamp("LOGINDATETIME");
                if (loginDateTime != null) {
                    LocalDateTime localDateTime = loginDateTime.toLocalDateTime();
                    member.setLoginDateTime(localDateTime);
                }
            }
            return member;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public Member findById(String id){
        open();
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
                                .isAdmins(rs.getBoolean("isAdmin"))
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
        } finally {
            close();
        }
    }
    public synchronized boolean insertMember(Member member) {
        try{
            open();
            pstmt = con.prepareStatement("insert into member" +
                    "(userid, pwd, name, phone, email, createdate, isAdmin, userStatus) " +
                    "values(?, ?, ?, ?, ?, ?, ?, ?)");

            //멤버 정보 설정
            pstmt.setString(1, member.getUserId());
            pstmt.setString(2, member.getPwd());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, String.valueOf(LocalDateTime.now()));
            pstmt.setBoolean(7, member.isAdmin());
            pstmt.setString(8, member.getUserStatus());
            return pstmt.executeUpdate() > 0;

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            close();
        }

        return false;
    }

    public boolean edit(Member member){
        open();
        String query = "update member " +
                "set pwd = ?, name = ?, email = ?, phone = ?, createdate = ?, logindatetime = ?, delete_yn = ?, userStatus = ? " +
                "where userId=?";
        try {
            System.out.println("edit member:"+member);
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, member.getPwd());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getPhone());
            pstmt.setTimestamp(5, Timestamp.valueOf(member.getCreatedate()));
            pstmt.setTimestamp(6, Timestamp.valueOf(member.getLoginDateTime()));
            pstmt.setBoolean(7, member.isAdmin());
            pstmt.setString(8, member.getUserStatus());
            pstmt.setString(9, member.getUserId());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }
    public long listSize(String name){
        try{
            open();
            String query = "select count(*) " +
                    "from member " +
                    " " +
                    "where name like concat('%', ?, '%') ";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();
            rs.next();

            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }
    public List<Member> search(String search){
        try {
            open();
            String query = "select * from member where name like concat('%', ?, '%') limit 5";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, search);

            ResultSet rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();

            while(rs.next()){
                Member m = Member.builder()
                        .userId(rs.getString("userid"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .email(rs.getString("email"))
                        .isAdmins(rs.getBoolean("isAdmin"))
                        .admin(rs.getBoolean("isAdmin"))
                        .userStatus(rs.getString("userStatus"))
                        .createdate(rs.getTimestamp("createdate").toLocalDateTime())
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
        }finally {
            close();
        }
    }
    public List<Member> list(BoardParam boardParam){
        try {
            open();
            String query = "select * from member where name like concat('%', ?, '%') limit ?, ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, boardParam.getSearch());
            pstmt.setLong(2, boardParam.getPageStart());
            pstmt.setLong(3, boardParam.getPageEnd());

            ResultSet rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();

            while(rs.next()){
                Member m = Member.builder()
                        .userId(rs.getString("userid"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .email(rs.getString("email"))
                        .isAdmins(rs.getBoolean("isAdmin"))
                        .admin(rs.getBoolean("isAdmin"))
                        .userStatus(rs.getString("userStatus"))
                        .createdate(rs.getTimestamp("createdate").toLocalDateTime())
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
        }finally {
            close();
        }
    }
    public Member searchId(Member member){
        open();
        String query = "select * from member where name = ? and email = ?";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            ResultSet rs = pstmt.executeQuery();
            Member resultMember = null;
            if(rs.next()){
                resultMember = Member.builder()
                        .userId(rs.getString("USERID"))
                        .build();
            }
            return resultMember;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }

    public Member searchPwd(Member member){
        String query = "select * from member where userid = ? and phone = ?";
        try {
            open();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, member.getUserId());
            pstmt.setString(2, member.getPhone());
            ResultSet rs = pstmt.executeQuery();
            Member resultMember = null;
            if(rs.next()){
                resultMember = Member.builder()
                        .pwd(rs.getString("PWD"))
                        .build();
            }
            return resultMember;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }

    public boolean userStatus_noUse(String id){
        try {
            open();
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
        }finally {
            close();
        }
    }

    public boolean userStatus_use(String id) {
        try {
            open();
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
        }finally {
            close();
        }
    }

    public boolean userStatus_withdraw(String id){
        try {
            open();
            String query = "update member " +
                    "set userStatus = ? " +
                    "where userid = ? ";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "STOP");
            pstmt.setString(2, id);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }

    public List<String> sd(){
        try {
            open();
            String query = "select distinct (sd) from config_emd ";
            pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            List<String> sds = new ArrayList<>();

            while(rs.next()){
                sds.add(rs.getString(1));
            }
            return sds;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }

    public List<String> sgg(String sd){
        try {
            open();
            String query = "select distinct (sgg) from config_emd where sd = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, sd);
            ResultSet rs = pstmt.executeQuery();
            List<String> sgg = new ArrayList<>();

            while(rs.next()){
                sgg.add(rs.getString(1));
            }
            return sgg;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }

    public List<String> sdName(String sdName){
        try {
            open();
            String query = "select name from config_emd where sgg = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, sdName);
            ResultSet rs = pstmt.executeQuery();
            List<String> sgg = new ArrayList<>();

            while(rs.next()){
                sgg.add(rs.getString(1));
            }
            return sgg;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }
}
