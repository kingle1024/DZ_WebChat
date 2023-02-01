package Member;

import Page.BoardParam;

import java.util.List;

public interface MemberRepository {
    Member findById(String id);
    boolean insertMember(Member member) throws Member.ExistMember;
    boolean edit(Member member);
    List<Member> list(BoardParam boardParam);
    Member searchId(Member member);
    Member searchPwd(Member member);
    boolean userStatus_noUse(String id);
    boolean userStatus_use(String id);
    boolean userStatus_withdraw(String id);
    Member findByIdAndPassword(String userId, String pwd);
}
