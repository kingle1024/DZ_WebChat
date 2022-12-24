package Board;

import BoardPopularity.BoardPopularity;
import BoardPopularity.BoardPopularityDAO;
import File.BoardFile;
import File.BoardFileDAO;

import java.util.List;

public class BoardService {
    BoardDAO boardDAO = new BoardDAO();
    BoardFileDAO boardFileDAO = new BoardFileDAO();
    BoardPopularityDAO boardPopularityDAO = new BoardPopularityDAO();

    public BoardView getBoard(String no) {
        Board board = boardDAO.viewBoard(no);
        BoardView boardView = BoardView.of(board);
        List<BoardFile> boardFiles = boardFileDAO.list(no);
        boardView.setBoardFiles(boardFiles);
        return boardView;
    }

    public BoardView viewAndCount(String no) {
        boardDAO.addHit(no);
        Board board = boardDAO.viewBoard(no);
        BoardView boardView = BoardView.of(board);
        List<BoardFile> boardFiles = boardFileDAO.list(no);
        boardView.setBoardFiles(boardFiles);
        return boardView;
    }

    public Board view(String no) {
        return boardDAO.viewBoard(no);
    }

    public int del(String no){
        return boardDAO.del(no);
    }

    public BoardPopularity findByBnoAndUserIdAndIsDelete(String no, String userId) {
        return boardPopularityDAO.findByBnoAndUserIdAndIsDelete(no, userId);
    }
}
