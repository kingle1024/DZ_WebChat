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
    private String btype;
    private int bhit;
    private LocalDateTime bdate;
}
