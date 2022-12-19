package File;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BoardFile {
    private int fid;
    private int number;
    private String orgName;
    private String realName;
    private String ContentType;
    private int length;
}
