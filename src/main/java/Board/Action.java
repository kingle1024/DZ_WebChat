package Board;

import BoardPopularity.BoardPopularity;
import BoardPopularity.BoardPopularityDAO;
import File.BoardFile;
import File.BoardFileDAO;
import Page.BoardParam;
import Page.PageUtil;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class Action {
    BoardDAO boardDAO = new BoardDAO();
    BoardFileDAO boardFileDAO = new BoardFileDAO();
    BoardPopularityDAO boardPopularityDAO = new BoardPopularityDAO();


    public String edit(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();

        String bno = request.getParameter("bno");
        String loginId = (String) session.getAttribute("login_id");

        Board board = boardDAO.viewBoard(bno);

        List<BoardFile> boardFiles = boardFileDAO.list(bno);
        System.out.println("boardFiles"+boardFiles);
        request.setAttribute("boardFiles", boardFiles);

        if(!board.getBwriterId().equals(loginId)){
            request.setAttribute("board", board);
        }

        return "/jsp/notice/edit.jsp";
    }

    public String normalList(HttpServletRequest request, HttpServletResponse response){
        String search = request.getParameter("search");
        if(search == null) search = "";

        BoardParam parameter = new BoardParam();
        String pageIndex = request.getParameter("pageIndex");
        if(pageIndex == null) pageIndex = String.valueOf(0);
        String pageSize = request.getParameter("pageSize");
        if(pageSize == null) pageSize = String.valueOf(0);

        String type = "normal";

        parameter.setPageIndex(Long.parseLong(pageIndex));
        parameter.setPageSize(Long.parseLong(pageSize));
        parameter.setSearch(search);
        parameter.setType(type);
        parameter.init(); // pageIndex, pageSize 갖고 현재 어느 페이지에 있고, 페이징을 어떻게 해줄지 초기 데이터 설정

        long totalCount = boardDAO.listSize(search, type);

        String queryString = parameter.getQueryString();
        PageUtil pageUtil = new PageUtil(
                totalCount,
                parameter.getPageSize(),
                parameter.getPageIndex(),
                queryString
        );

        List<Board> boardsList = boardDAO.list(search, type, parameter);
        request.setAttribute("boardsList", boardsList);
        request.setAttribute("type", type);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("pager", pageUtil.paper());

        return "/jsp/normal/list.jsp";
    }

    public String list(HttpServletRequest request, HttpServletResponse response){
        String search = request.getParameter("search");
        if(search == null) search = "";

        BoardParam parameter = new BoardParam();
        String pageIndex = request.getParameter("pageIndex");
        if(pageIndex == null) pageIndex = String.valueOf(0);
        String pageSize = request.getParameter("pageSize");
        if(pageSize == null) pageSize = String.valueOf(0);
        String type = request.getParameter("type");

        parameter.setPageIndex(Long.parseLong(pageIndex));
        parameter.setPageSize(Long.parseLong(pageSize));
        parameter.setSearch(search);
        parameter.setType(type);
        parameter.init();

        List<Board> boardsList = boardDAO.list(search, type, parameter);
        long totalCount = boardDAO.listSize(search, type);

        String queryString = parameter.getQueryString();
        PageUtil pageUtil = new PageUtil(
                totalCount,
                parameter.getPageSize(),
                parameter.getPageIndex(),
                queryString
        );

        request.setAttribute("boardsList", boardsList);
        request.setAttribute("type", type);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("pager", pageUtil.paper());

        return "/jsp/notice/list.jsp";
    }

    public String normalView(HttpServletRequest request, HttpServletResponse response){
        String no = request.getParameter("no");
        boardDAO.addHit(no);
        Board board = boardDAO.viewBoard(no);
        request.setAttribute("board", board);
        return "/jsp/normal/view.jsp";
    }

    public String noticeView(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        String no = request.getParameter("no");
        boardDAO.addHit(no);
        Board board = boardDAO.viewBoard(no);
        List<BoardFile> boardFiles = boardFileDAO.list(no);

        long likeCount = boardPopularityDAO.findByBnoAndType(board.getBno(), "like");
        long disLikeCount = boardPopularityDAO.findByBnoAndType(board.getBno(), "dislike");
        String userId = (String) session.getAttribute("login_id");

        BoardPopularity boardPopularity = boardPopularityDAO.findByBnoAndUserIdAndIsDelete(no, userId);

        if(boardPopularity == null){
            request.setAttribute("myStatus", "no");
        }else {
            String likeType = boardPopularity.getType();
            if ("like".equals(likeType)) {
                request.setAttribute("myStatus", "like");
            } else if ("dislike".equals(likeType)) {
                request.setAttribute("myStatus", "dislike");
            }
        }

        request.setAttribute("boardFiles", boardFiles);
        request.setAttribute("board", board);
        request.setAttribute("like", likeCount);
        request.setAttribute("dislike", disLikeCount);

        return "/jsp/notice/view.jsp";
    }
    public JSONObject del(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        String bno = request.getParameter("bno");
        Board board = boardDAO.viewBoard(bno);
        String loginId = (String) session.getAttribute("login_id");
        JSONObject jsonResult = new JSONObject();
        if(!board.getBwriterId().equals(loginId)){
            jsonResult.put("message", "글쓴이가 아니면 삭제할 수 없습니다.");
            jsonResult.put("status", false);
        }else if(boardDAO.del(bno) < 0){
            jsonResult.put("message", "삭제 실패");
            jsonResult.put("status", false);
        }else{
            jsonResult.put("message", "삭제 성공");
            jsonResult.put("status", true);
        }
        return jsonResult;
    }
}
