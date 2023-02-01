package Board;

import File.BoardFile;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BoardView {
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
    List<BoardFile> boardFiles;
    public static BoardView of(Board board){
        BoardView boardView = BoardView.builder()
                .bno(board.getBno())
                .btitle(board.getBtitle())
                .bcontent(board.getBcontent())
                .bwriter(board.getBwriter())
                .bwriterId(board.getBwriterId())
                .type(board.getType())
                .password(board.getPassword())
                .parentNo(board.getParentNo())
                .bhit(board.getBhit())
                .likeCnt(board.getLikeCnt())
                .disLikeCnt(board.getDisLikeCnt())
                .isDelete(board.isDelete())
                .bdate(board.getBdate())
                .build();
        return boardView;
    }
}
