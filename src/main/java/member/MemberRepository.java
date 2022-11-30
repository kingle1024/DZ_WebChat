package member;

public interface MemberRepository {
    void insertMember(Member member) throws Member.ExistMember;

}
