package Board;

import BoardPopularity.BoardPopularity;
import Page.BoardParam;

import java.util.List;

public interface BoardRepository {
    long listSize(BoardParam param);
    List<Board> list(BoardParam boardParam);
    Board viewBoard(String no);
    boolean findByNoAndPassword(String no, String password);
    void addHit(String no);
    int del(String bno);
    BoardPopularity findByBnoAndUserId(String bno, String userId);
    boolean boardPopularity(String bno, String userId);
    int insert(Board board);
    boolean edit(Board board);
}
