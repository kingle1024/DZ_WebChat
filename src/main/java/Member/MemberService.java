package Member;

import java.util.List;

public class MemberService {
    MemberDAO memberDAO = new MemberDAO();
    public boolean userStatus(String id, String type){
        switch (type){
            case "nouse":{
                return memberDAO.userStatus_noUse(id);
            }
            case "use":{
                return memberDAO.userStatus_use(id);
            }
            case "withdraw":{
                return memberDAO.userStatus_withdraw(id);
            }
            default:{
                return false;
            }
        }
    }
    public List<Member> search(String query){
        return memberDAO.search(query);
    }
}
