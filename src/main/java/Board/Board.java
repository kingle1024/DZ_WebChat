package Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private String bno;
    private String btitle;
    private String bcontent;
    private String bwriter;
    private String bwriterId;
    private String type;
    private String password;
    private String parentNo;
    private int bhit;
    private int likeCnt;
    private int disLikeCnt;
    private boolean isDelete;
    private LocalDateTime bdate;
}
