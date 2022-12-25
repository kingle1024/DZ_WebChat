package Board;

import BoardPopularity.BoardPopularityDAO;
import Custom.RQ;
import File.BoardFileDAO;
import Page.PageUtil;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Action {
    BoardDAO boardDAO = new BoardDAO();
    BoardFileDAO boardFileDAO = new BoardFileDAO();
    BoardPopularityDAO boardPopularityDAO = new BoardPopularityDAO();
    BoardService boardService = new BoardService();

    @RQ(url = "/qna/add")
    public String qnaAdd(HttpServletRequest request, HttpServletResponse response){
        return "/jsp/qna/add.jsp";
    }
    @RQ(url = "/notice/add")
    public String noticeAdd(HttpServletRequest request, HttpServletResponse response){
        return "/jsp/notice/add.jsp";
    }
    @RQ(url = "/normal/add")
    public String normalAdd(HttpServletRequest request, HttpServletResponse response){
        return "/jsp/normal/add.jsp";
    }

    @RQ(url ="/search")
    public JSONObject search(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String search = request.getParameter("search");
        String pageIndex = request.getParameter("pageIndex");
        String type = request.getParameter("type");

        PageUtil pageUtil = boardService.pageUtil(search, pageIndex, type, "board");

        JSONObject jsonResult = new JSONObject();
        jsonResult.put("message", "성공");
        jsonResult.put("boardsList", pageUtil.getList());
        jsonResult.put("type", type);
        jsonResult.put("pager", pageUtil.paper());
        return jsonResult;
    }

    @RQ(url = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();

        String bno = request.getParameter("bno");
        String loginId = (String) session.getAttribute("login_id");

        BoardView board = boardService.getBoard(bno, loginId);
        if(board != null)
            request.setAttribute("board", board);

        return "/jsp/notice/edit.jsp";
    }

    @RQ(url = "/normal/list")
    public String normalList(HttpServletRequest request, HttpServletResponse response){
        String search = request.getParameter("search");
        String pageIndex = request.getParameter("pageIndex");
        String type = "normal";

        PageUtil pageUtil = boardService.pageUtil(search, pageIndex, type, "board");

        request.setAttribute("boardsList", pageUtil.getList());
        request.setAttribute("type", type);
        request.setAttribute("pager", pageUtil.paper());

        return "/jsp/normal/list.jsp";
    }

    @RQ(url = "/list")
    public String list(HttpServletRequest request, HttpServletResponse response){
        String type = request.getParameter("type");

        PageUtil pageUtil = boardService.pageUtil("", "", type, "board");
        request.setAttribute("page", pageUtil);
        request.setAttribute("type", type);
        request.setAttribute("pager", pageUtil.paper());

        return "/jsp/"+type+"/list.jsp";
    }

    @RQ(url = "/normal/view")
    public String normalView(HttpServletRequest request, HttpServletResponse response){
        String no = request.getParameter("no");
        BoardView board = boardService.viewAndCount(no);

        request.setAttribute("board", board);
        return "/jsp/normal/view.jsp";
    }

    @RQ(url = "/notice/view")
    public String noticeView(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        String no = request.getParameter("no");

        BoardView board = boardService.viewAndCount(no);
        String userId = (String) session.getAttribute("login_id");
        String myStatus = boardService.getMyStatus(no, userId);

        request.setAttribute("myStatus", myStatus);
        request.setAttribute("board", board);

        return "/jsp/"+board.getType()+"/view.jsp";
    }
    @RQ(url = "/del")
    public JSONObject del(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        String bno = request.getParameter("bno");
        Board board = boardService.view(bno);

        String loginId = (String) session.getAttribute("login_id");
        JSONObject jsonResult = new JSONObject();
        if(!board.getBwriterId().equals(loginId)){
            jsonResult.put("message", "글쓴이가 아니면 삭제할 수 없습니다.");
            jsonResult.put("status", false);
        }else if(boardService.del(bno) < 0){
            jsonResult.put("message", "삭제 실패");
            jsonResult.put("status", false);
        }else{
            jsonResult.put("message", "삭제 성공");
            jsonResult.put("status", true);
            jsonResult.put("url", "/board/list?type=notice");
        }
        return jsonResult;
    }
    @RQ(url = "/replyForm")
    public String replyForm(HttpServletRequest request, HttpServletResponse response){
        String bno = request.getParameter("bno");
        BoardView board = boardService.viewAndCount(bno);
        request.setAttribute("board", board);

        return "/jsp/qna/replyForm.jsp";
    }
}
