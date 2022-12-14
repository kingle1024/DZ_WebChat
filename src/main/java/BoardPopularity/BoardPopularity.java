package BoardPopularity;

import lombok.Data;

@Data
public class BoardPopularity {
    private int bno;
    private String userId;
    private String type;
    private boolean isDelete;
}
