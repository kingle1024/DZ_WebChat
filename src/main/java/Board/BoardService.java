package Board;

import BoardPopularity.BoardPopularity;
import BoardPopularity.BoardPopularityDAO;
import File.BoardFile;
import File.BoardFileDAO;
import Member.MemberBatis;
import Member.MemberDAO;
import Page.BoardParam;
import Page.PageUtil;

import java.util.List;

public class BoardService {
    BoardDAO boardDAO = new BoardDAO();
    MemberDAO memberDAO = new MemberDAO();
    BoardFileDAO boardFileDAO = new BoardFileDAO();
    BoardPopularityDAO boardPopularityDAO = new BoardPopularityDAO();
    MemberBatis memberBatis = new MemberBatis();
    public BoardView getBoard(String no, String loginId) {
        Board board = boardDAO.viewBoard(no);
        if(!loginId.equals(board.getBwriterId())){
            return null;
        }

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
        boardView.setLikeCnt((int) boardPopularityDAO.findByBnoAndType(no, "like"));
        boardView.setDisLikeCnt((int) boardPopularityDAO.findByBnoAndType(no, "dislike"));

        return boardView;
    }

    public Board view(String no) {
        return boardDAO.viewBoard(no);
    }

    public Board normalView(String no, String password){
        Board board = boardDAO.viewBoard(no);
        if(!password.equals(board.getPassword())){
            return null;
        }
        return board;
    }
    public boolean findByNoAndPassword(String no, String password){
        return boardDAO.findByNoAndPassword(no, password);
    }

    public int del(String no){
        return boardDAO.del(no);
    }

    public String getMyStatus(String no, String userId){
        BoardPopularity boardPopularity =
                boardPopularityDAO.findByBnoAndUserIdAndIsDelete(no, userId);

        String likeType = boardPopularity.getType();
        if ("like".equals(likeType)) {
            return "like";
        } else if ("dislike".equals(likeType)) {
            return "dislike";
        }else{
            return "no";
        }
    }

    public int normalInsert(Board board){
        return boardDAO.insert(board);
    }

//    public void insert(){
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        // 업로드 파일 임시로 저장할 경로 설정 -> /temp
//        String fileRepository = "/Users/ejy1024/Documents/upload";
//        factory.setRepository(new File((fileRepository+"/temp"))); // 임시공간
//        ServletFileUpload upload = new ServletFileUpload(factory);
//        List<FileItem> items = upload.parseRequest(request);
//    }
    public int insert(Board board, List<BoardFile> boardFiles){
        int result = boardDAO.callInsert(board);

        for(BoardFile bf : boardFiles){
            bf.setNumber(result);
            boardFileDAO.insert(bf);
        }
        return result;
    }
    public boolean edit(Board board){

        return boardDAO.edit(board);
    }
    public boolean edit(Board board, String writerId){
        if(!writerId.equals(board.getBwriterId())){
            return false;
        }
        boardDAO.edit(board);
        return true;
    }

    public PageUtil pageUtil(String search, String pageIndex, String type, String listType){
        BoardParam param = BoardParam.builder()
                .pageIndex(pageIndex)
                .search(search)
                .type(type)
                .build();
        param.init();

        long totalCount = 0;

        List<?> list = null;
        if(listType.equals("board")){
            list = boardDAO.list(param);
            totalCount = boardDAO.listSize(param);
        }else if(listType.equals("member")){
//            list = memberDAO.list(param);
            totalCount = memberDAO.listSize(search);
            list = memberBatis.memberList(param);
        }

        return PageUtil.builder()
                .list(list)
                .totalCount(totalCount)
                .pageSize(param.getPageSize())
                .pageIndex(param.getL_pageIndex())
                .build();
    }
}
