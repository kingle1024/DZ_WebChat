package Auth;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {
    private final DataSource dataFactory;
    private Connection con;
    private PreparedStatement pstmt;

    public MemberDAO() throws NamingException {
        Context ctx = new InitialContext();
        Context envContext = (Context) ctx.lookup("java:/comp/env");
        dataFactory = (DataSource) envContext.lookup("jdbc/maria");
    }
    public boolean isExisted(Member member) throws SQLException {
        boolean result;
        String id = member.getUserId();
        String pwd = member.getPwd();

        con = dataFactory.getConnection();
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
}
