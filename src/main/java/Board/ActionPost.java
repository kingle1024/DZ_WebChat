package Board;

import BoardPopularity.BoardPopularity;
import BoardPopularity.BoardPopularityDAO;
import Custom.RQ;
import File.BoardFile;
import File.BoardFileDAO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActionPost {
    BoardPopularityDAO boardPopularityDAO = new BoardPopularityDAO();
    BoardDAO boardDAO = new BoardDAO();
    BoardFileDAO boardFileDAO = new BoardFileDAO();
    String fileRepository = "/Users/ejy1024/Documents/upload";

    public JSONObject popularity(HttpServletRequest request, HttpServletResponse response){
        try {
            HttpSession session = request.getSession();
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
            return jsonResult;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject normalInsert(HttpServletRequest request, HttpServletResponse response){
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
            return jsonResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject insert(HttpServletRequest request, HttpServletResponse response){
        try {
            HttpSession session = request.getSession();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 업로드 파일 임시로 저장할 경로 설정 -> /temp
            factory.setRepository(new File((fileRepository+"/temp"))); // 임시공간
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
                    if(item.getSize() == 0) continue;
                    String[] contentType = item.getContentType().split("/");
                    String realName = System.currentTimeMillis() + "." + contentType[1];
                    BoardFile boardFile = BoardFile.builder()
                            .orgName(item.getName())
                            .realName(realName)
                            .ContentType(item.getContentType())
                            .length((int) item.getSize())
                            .build();
                    boardFiles.add(boardFile);

                    File saveFile = new File(fileRepository +"/"+realName);
                    if(saveFile.exists()){
                        saveFile = new File(fileRepository +"/"+realName +"_1");
                    }
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
            int result = boardDAO.callInsert(board);
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
            return jsonResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject normalEdit(HttpServletRequest request, HttpServletResponse response){
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

                return jsonResult;
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

            return jsonResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject noticeEdit(HttpServletRequest request, HttpServletResponse response){
        try {
            HttpSession session = request.getSession();
            String uid = (String) session.getAttribute("login_id");

            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 업로드 파일 임시로 저장할 경로 설정 -> /temp
            factory.setRepository(new File((fileRepository+"/temp"))); // 임시공간
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);

            int bno = -1;
            String title = null;
            String content = null;
            List<BoardFile> boardFiles = new ArrayList<>();

            for(FileItem item : items){
                if(item.isFormField()){
                    String value = new String(item.getString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                    switch (item.getFieldName()){
                        case "bno":{
                            bno = Integer.parseInt(value);
                            break;
                        }
                        case "title":{
                            title = value;
                            break;
                        }
                        case "editor":{
                            content = value;
                            break;
                        }
                    }
                }else{ // 파일일 때 처리
                    if(item.getSize() == 0) continue;
                    String[] contentType = item.getContentType().split("/");
                    String realName = System.currentTimeMillis() + "." + contentType[1];

                    BoardFile boardFile = BoardFile.builder()
                            .orgName(item.getName())
                            .realName(realName)
                            .ContentType(item.getContentType())
                            .length((int) item.getSize())
                            .build();
                    boardFiles.add(boardFile);

                    File saveFile = new File(fileRepository + "/" + realName);
                    item.write(saveFile);
                }
            }

            // TODO 삭제된 데이터 목록을 가져와서 삭제 진행
            JSONObject jsonResult = new JSONObject();

            Board board = boardDAO.viewBoard(String.valueOf(bno));
            board.setBtitle(title);
            board.setBcontent(content);

            boolean result = boardDAO.edit(board);
            if(!result && !uid.equals(board.getBwriterId())){
                jsonResult.put("status", false);
                jsonResult.put("message", "수정 실패");
            }else{
                jsonResult.put("status", true);
                jsonResult.put("message", "수정 성공");
                jsonResult.put("url", "/board/notice/view?no="+bno);
            }
            System.out.println(jsonResult);
            return jsonResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public JSONObject normalDel(HttpServletRequest request, HttpServletResponse response){
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
            }else if(boardDAO.del(String.valueOf(bno)) < 0){
                jsonResult.put("message", "삭제 실패");
                jsonResult.put("status", false);
            }else{
                jsonResult.put("message", "삭제 성공");
                jsonResult.put("status", true);
                jsonResult.put("url", "/board/normal/list?type=normal");
            }

            return jsonResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public JSONObject normalPasswordCheck(HttpServletRequest request, HttpServletResponse response){
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
            return jsonResult;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String normalEditpage(HttpServletRequest request, HttpServletResponse response){
        String password = request.getParameter("password");
        int bno = Integer.parseInt(request.getParameter("bno"));

        boolean validPassword = boardDAO.findByNoAndPassword(String.valueOf(bno), password);

        if(!validPassword){
            return "/board/normal/view?no="+bno;
        }else{
            Board board = boardDAO.viewBoard(String.valueOf(bno));
            request.setAttribute("board", board);
            request.setAttribute("password", password);
            return "/jsp/normal/edit.jsp";
        }
    }

    @RQ(url = "/replyForm")
    public JSONObject reply(HttpServletRequest request, HttpServletResponse response){
        try {
            HttpSession session = request.getSession();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File((fileRepository + "/temp")));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);

            String title = "";
            String content = "";
            String type = "";
            String parentNo = "";
            String writer = (String) session.getAttribute("login_name");
            String writerId = (String) session.getAttribute("login_id");

            List<BoardFile> boardFiles = new ArrayList<>();

            for (FileItem item : items) {
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
                        case "bno":{
                            parentNo = value;
                        }
                    }
                } else {
                    if(item.getSize() == 0) continue;
                    String[] contentType = item.getContentType().split("/");
                    String realName = System.currentTimeMillis() + "." + contentType[1];

                    BoardFile boardFile = BoardFile.builder()
                            .orgName(item.getName())
                            .realName(realName)
                            .ContentType(item.getContentType())
                            .length((int) item.getSize())
                            .build();
                    boardFiles.add(boardFile);

                    File saveFile = new File(fileRepository + "/" + realName);
                    if(saveFile.exists()){
                        saveFile = new File(fileRepository + "/" + realName +"_1");
                    }
                    item.write(saveFile);
                }
            }

            Board board = Board.builder()
                    .btitle(title)
                    .bwriter(writer)
                    .bcontent(content)
                    .bhit(0)
                    .bdate(LocalDateTime.now())
                    .type(type)
                    .bwriterId(writerId)
                    .parentNo(parentNo)
                    .build();

            int result = boardDAO.insertReply(board);
            JSONObject jsonResult = new JSONObject();

            for(BoardFile bf : boardFiles){
                bf.setNumber(result);
                boardFileDAO.insert(bf);
            }

            if(result < 0) {
                jsonResult.put("status", false);
                jsonResult.put("message", "등록 실패");
            } else {
                jsonResult.put("status", true);
                jsonResult.put("url", "/board/list?type="+type);
                jsonResult.put("message", "등록 성공");
            }
            return jsonResult;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
