package Auth;

import Member.Member;
import Member.MemberRepository;
import Member.MemberDAO;

import java.time.LocalDateTime;

public class LoginService {
    MemberRepository dao = new MemberDAO();

    public Member findById(String userId, String pwd){
        Member member = dao.findByIdAndPassword(userId, pwd);
        if(member == null) return null;

        if (!"STOP".equals(member.getUserStatus())) {
            member.setLoginDateTime(LocalDateTime.now());
            dao.edit(member);
        }
        return member;
    }
}
