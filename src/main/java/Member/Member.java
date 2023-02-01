package Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {
    private String userId;
    private String pwd;
    private String name;
    private String phone;
    private String email;
    private String userStatus;
    private boolean isAdmins;
    private boolean admin;
    private LocalDateTime createdate;
    private LocalDateTime loginDateTime;

    public static class ExistMember extends Exception {
        public ExistMember(String reason) {
            super(reason);
        }
    }

    public static class NotExistMember extends Exception {
        public NotExistMember(String reason) {
            super(reason);
        }
    }

    public static class NotExistUidPwd extends Exception {
        public NotExistUidPwd(String reason) {
            super(reason);
        }
    }
}
