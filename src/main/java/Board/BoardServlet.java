package Board;

import BoardPopularity.BoardPopularity;
import BoardPopularity.BoardPopularityDAO;
import BoardPopularity.BoardPopularityRepository;
import File.BoardFile;
import File.BoardFileDAO;
import Page.BoardParam;
import Page.PageUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@WebServlet(name = "BoardServlet", value = "/board/*")
public class BoardServlet extends HttpServlet {
    BoardRepository boardDAO;
    BoardFileDAO boardFileDAO;
    BoardPopularityRepository boardPopularityDAO;
    PrintWriter out;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        System.out.println("BoardServlet doGet > " + requestURI);

        HttpSession session = request.getSession();

        boardDAO = new BoardDAO();
        boardPopularityDAO = new BoardPopularityDAO();
        boardFileDAO = new BoardFileDAO();
        out = response.getWriter();

        switch (requestURI) {
            case "/board/notice/edit": {
                String bno = request.getParameter("bno");
                String loginId = (String) session.getAttribute("login_id");

                Board board = boardDAO.viewBoard(bno);

                if(board.getBwriterId().equals(loginId)){
                    request.setAttribute("board", board);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notice/edit.jsp");
                    dispatcher.forward(request, response);
                }

                break;
            }

            case "/board/normal/list":{
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

                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/normal/list.jsp");
                dispatcher.forward(request, response);

                break;
            }
            case "/board/list":{
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

                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notice/list.jsp");
                dispatcher.forward(request, response);

                break;
            }
            case "/board/normal/view":{
                String no = request.getParameter("no");
                boardDAO.addHit(no);
                Board board = boardDAO.viewBoard(no);
                request.setAttribute("board", board);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/normal/view.jsp");
                dispatcher.forward(request, response);
                break;
            }
            case "/board/notice/view":{
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

                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notice/view.jsp");
                dispatcher.forward(request, response);

                break;
            }
            case "/board/del":{
                String bno = request.getParameter("bno");
                Board board = boardDAO.viewBoard(bno);
                String loginId = (String) session.getAttribute("login_id");
                JSONObject jsonResult = new JSONObject();
                if(!board.getBwriterId().equals(loginId)){
                    jsonResult.put("message", "글쓴이가 아니면 삭제할 수 없습니다.");
                    jsonResult.put("status", false);
                }else if(!boardDAO.del(bno)){
                    jsonResult.put("message", "삭제 실패");
                    jsonResult.put("status", false);
                }else{
                    jsonResult.put("message", "삭제 성공");
                    jsonResult.put("status", true);
                }
                out.println(jsonResult);
                break;
            }

            default:
                System.out.println("requestURI : " + requestURI);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        System.out.println("BoardServlet doPost > " + requestURI);
        HttpSession session = request.getSession();

        try {
            boardDAO = new BoardDAO();
            boardPopularityDAO = new BoardPopularityDAO();
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (requestURI) {
            case "/board/popularity":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    String bno = String.valueOf(jsonObject.getInt("bno"));
                    String type = jsonObject.getString("type");
                    String loginId = (String) session.getAttribute("login_id");
                    System.out.println(bno + " " + type + " " + loginId);

                    BoardPopularity boardPopularity = boardPopularityDAO.findByBnoAndUserIdAndIsDelete(bno, loginId);
                    JSONObject jsonResult = new JSONObject();
                    if (boardPopularity == null) { // 기존에 데이터가 없는 경우 추가
                        boardPopularity = new BoardPopularity();
                        boardPopularity.setBno(Integer.parseInt(bno));
                        boardPopularity.setUserId(loginId);
                        boardPopularity.setType(type);
                        boardPopularityDAO.insert(boardPopularity);
                        jsonResult.put("message", "성공");
                        jsonResult.put("status", "add");
                    } else {
                        // 동일한 데이터가 존재하면 제거
                        if (boardPopularity.getType().equals(type)) {
                            System.out.println("취소");
                            // del
                            boardPopularity.setDelete(true);

                            boardPopularityDAO.update(boardPopularity);
                            jsonResult.put("message", "취소되었습니다.");
                            jsonResult.put("status", "cancel");
                        } else {
                            // 변경
                            System.out.println("변경");
                            boardPopularity.setDelete(true);
                            boardPopularityDAO.update(boardPopularity);
                            boardPopularity.setType(type);
                            boardPopularity.setDelete(false);
                            boardPopularityDAO.insert(boardPopularity);
                            jsonResult.put("message", "변경되었습니다.");
                            jsonResult.put("status", "change");
                        }
                    }
                    out.println(jsonResult);
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            }
            case "/board/normal/insert":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String type = (String) jsonObject.get("type");
                    String password = (String) jsonObject.get("password");
                    String rePassword = (String) jsonObject.get("rePassword");

                    Board board = Board.builder()
                            .btitle((String) jsonObject.get("title"))
                            .bwriter((String) jsonObject.get("writer"))
                            .bcontent((String) jsonObject.get("content"))
                            .password(password)
                            .type(type)
                            .build();
                    int result = boardDAO.insert(board);

                    JSONObject jsonResult = new JSONObject();

                    if(!password.equals(rePassword)){
                        jsonResult.put("status", false);
                        jsonResult.put("message", "비밀번호가 서로 다릅니다.");
                    }else if (result < 0) {
                        jsonResult.put("status", false);
                        jsonResult.put("message", "등록 실패");
                    }else {
                        jsonResult.put("status", true);
                        jsonResult.put("url", "/board/normal/list?type="+type);
                        jsonResult.put("message", "등록 성공");
                    }

                    System.out.println(jsonResult);
                    out.println(jsonResult);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "/board/insert":{
                try {
                    DiskFileItemFactory factory = new DiskFileItemFactory();
                    // 업로드 파일 임시로 저장할 경로 설정 -> /temp
                    String tempRepository = "/Users/ejy1024/Documents/upload/temp";
                    factory.setRepository(new File((tempRepository))); // 임시공간
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    List<FileItem> items = upload.parseRequest(request);

                    String title = "";
                    String content = "";
                    String type = "";
                    List<BoardFile> boardFiles = new ArrayList<>();

                    for(FileItem item : items){
                        if(item.isFormField()){
                            String value = new String(item.getString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                            switch (item.getFieldName()){
                                case "title":{
                                    title = value;
                                    break;
                                }
                                case "editor":{
                                    content = value;
                                    break;
                                }
                                case "type":{
                                    type = value;
                                    break;
                                }
                            }
                        }else{
                            BoardFile boardFile = new BoardFile();

                            boardFile.setOrgName(item.getName());
                            String[] contentType = item.getContentType().split("/");
                            String realName = System.currentTimeMillis() + "." + contentType[1];
                            boardFile.setRealName(realName);
                            boardFile.setContentType(item.getContentType());
                            boardFile.setLength((int) item.getSize());
                            boardFiles.add(boardFile);

                            File saveFile = new File(tempRepository +"/"+realName);
                            // 실제 저장 경로
                            item.write(saveFile);
                        }
                    }

                    String writer = (String) session.getAttribute("login_name");
                    String writerId = (String) session.getAttribute("login_id");


                    // TODO 등록한 계정이 관리자가 아닐 때에 바로 리턴 처리
//                    int isAdmin = () session.getAttribute("login_admin");

                    Board board = Board.builder()
                            .btitle(title)
                            .bwriter(writer)
                            .bcontent(content)
                            .bhit(0)
                            .bdate(LocalDateTime.now())
                            .type(type)
                            .bwriterId(writerId)
                            .password("")
                            .build();
                    int result = boardDAO.insert(board);
                    JSONObject jsonResult = new JSONObject();

                    for(BoardFile bf : boardFiles){
                        bf.setNumber(result);
                        boardFileDAO.insert(bf);
                    }

                    if (result < 0) {
                        jsonResult.put("status", false);
                        jsonResult.put("message", "등록 실패");
                    }else {
                        jsonResult.put("status", true);
                        jsonResult.put("url", "/board/list?type="+type);
                        jsonResult.put("message", "등록 성공");
                    }
                    System.out.println(jsonResult);
                    out.println(jsonResult);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "/board/normal/edit":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();

                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject jsonResult = new JSONObject();

                    int bno = (int) jsonObject.get("bno");
                    Board board = boardDAO.viewBoard(String.valueOf(bno));
                    String password = (String) jsonObject.get("password");
                    System.out.println(password +" " +board.getPassword());
                    if(!password.equals(board.getPassword())){
                        jsonResult.put("status", false);
                        jsonResult.put("message", "비밀번호가 틀립니다.");
                        out.println(jsonResult);
                        return;
                    }

                    String btitle = (String) jsonObject.get("btitle");
                    String bcontent = (String) jsonObject.get("bcontent");
                    String bwriter = (String) jsonObject.get("bwriter");

                    board.setBtitle(btitle);
                    board.setBcontent(bcontent);
                    board.setBwriter(bwriter);

                    boolean result = boardDAO.edit(board);
                    if(!result){
                        jsonResult.put("status", false);
                        jsonResult.put("message", "수정 실패");
                    }else{
                        jsonResult.put("status", true);
                        jsonResult.put("message", "수정 성공");
                        jsonResult.put("url", "/board/normal/view?no="+bno);
                    }
                    out.println(jsonResult);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "/board/notice/edit":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();

                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String uid = (String) session.getAttribute("login_id");
                    JSONObject jsonResult = new JSONObject();

                    int bno = (int) jsonObject.get("bno");
                    Board board = boardDAO.viewBoard(String.valueOf(bno));

                    String btitle = (String) jsonObject.get("btitle");
                    String bcontent = (String) jsonObject.get("bcontent");
                    System.out.println("bcontent:"+bcontent);

                    board.setBtitle(btitle);
                    board.setBcontent(bcontent);

                    boolean result = boardDAO.edit(board);
                    if(!result && !uid.equals(board.getBwriterId())){
                        jsonResult.put("status", false);
                        jsonResult.put("message", "수정 실패");
                    }else{
                        jsonResult.put("status", true);
                        jsonResult.put("message", "수정 성공");
                        jsonResult.put("url", "/board/notice/view?no="+bno);
                    }
                    out.println(jsonResult);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "/board/normal/del": {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
                    String jsonStr = in.readLine();

                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String password = (String) jsonObject.get("password");
                    int bno = (int) jsonObject.get("bno");
                    boolean validPassword = boardDAO.findByNoAndPassword(String.valueOf(bno), password);
                    JSONObject jsonResult = new JSONObject();

                    if(!validPassword){
                        jsonResult.put("status", false);
                        jsonResult.put("message", "패스워드가 틀립니다.");
                    }else if(!boardDAO.del(String.valueOf(bno))){
                        jsonResult.put("message", "삭제 실패");
                        jsonResult.put("status", false);
                    }else{
                        jsonResult.put("message", "삭제 성공");
                        jsonResult.put("status", true);
                        jsonResult.put("url", "/board/normal/list?type=normal");
                    }

                    out.println(jsonResult);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "/board/normal/passwordCheck":{
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
                    String jsonStr = in.readLine();

                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String password = (String) jsonObject.get("password");
                    int bno = (int) jsonObject.get("bno");
                    boolean validPassword = boardDAO.findByNoAndPassword(String.valueOf(bno), password);
                    JSONObject jsonResult = new JSONObject();
                    if(validPassword){
                        jsonResult.put("status", true);
                    }else{
                        jsonResult.put("status", false);
                        jsonResult.put("message", "비밀번호가 틀립니다.");
                    }
                    out.println(jsonResult);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case "/board/normal/editPage":{
                try{
                    String password = request.getParameter("password");
                    int bno = Integer.parseInt(request.getParameter("bno"));

                    boolean validPassword = boardDAO.findByNoAndPassword(String.valueOf(bno), password);

                    if(!validPassword){
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/board/normal/view?no="+bno);
                        dispatcher.forward(request, response);
                    }else{
                        Board board = boardDAO.viewBoard(String.valueOf(bno));
                        request.setAttribute("board", board);
                        request.setAttribute("password", password);
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/normal/edit.jsp");
                        dispatcher.forward(request, response);
                    }
                }catch(IOException | ServletException e){
                    throw new RuntimeException(e);
                }
                break;
            }
            default : {
            }
        }
    }
}
