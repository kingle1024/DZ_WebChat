package member;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;

public class MemberRepositoryDB implements MemberRepository{
    Connection conn = null;
    PreparedStatement pstmt = null;
    File file;

    public void open() {
        try {
//			file.getAbsoluteFile();

            Class.forName(Env.getProperty("driverClass"));
            System.out.println("JDBC 드라이버 로딩 성공");

            conn = DriverManager.getConnection(Env.getProperty("dbServerConn")
                    , Env.getProperty("dbUser")
                    , Env.getProperty("dbPasswd"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void close() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void insertMember(Member member) throws Member.ExistMember {
        try {
            open();

            pstmt = conn.prepareStatement("select * from member where userid = ?");

            //멤버 존재여부 확인
            pstmt.setString(1, member.getUserId());
            ResultSet rs = pstmt.executeQuery();
            int count = 0;

            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            if (count != 0) {
                throw new Member.ExistMember("[" + member.getUserId() + "] 아이디가 존재합니다" );
            }

            pstmt = conn.prepareStatement("insert into member" +
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

            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
