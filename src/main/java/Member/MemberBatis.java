package Member;

import Page.BoardParam;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;
import java.util.List;

public class MemberBatis {
    public static SqlSessionFactory sqlMapper = null;
    private static SqlSessionFactory getInstance() {
        if (sqlMapper == null) {
            try {
                String resource = "Mybatis/SqlMapConfig.xml";
                Reader reader = Resources.getResourceAsReader(resource);
                sqlMapper = new SqlSessionFactoryBuilder().build(reader);
                reader.close();()
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sqlMapper;
    }

    public List<Member> memberList(BoardParam param){
        sqlMapper = getInstance();
        SqlSession session = sqlMapper.openSession();
        return session.selectList("mapper.member.memberList", param);
    }
}
