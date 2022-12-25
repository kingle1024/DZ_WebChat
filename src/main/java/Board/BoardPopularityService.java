package Board;

import BoardPopularity.BoardPopularity;
import BoardPopularity.BoardPopularityDAO;
import org.json.JSONObject;

public class BoardPopularityService {
    BoardPopularityDAO boardPopularityDAO = new BoardPopularityDAO();

    public BoardPopularity findByBnoAndUserIdAndIsDelete(String no, String loginId){
        return boardPopularityDAO.findByBnoAndUserIdAndIsDelete(no, loginId);
    }
    public JSONObject insert(String no, String loginId, String type){
        BoardPopularity boardPopularity = boardPopularityDAO.findByBnoAndUserIdAndIsDelete(no, loginId);
        JSONObject jsonResult = new JSONObject();

        if(boardPopularity == null){
            boardPopularity = new BoardPopularity();
            boardPopularity.setBno(Integer.parseInt(no));
            boardPopularity.setUserId(loginId);
            boardPopularity.setType(type);
            boardPopularityDAO.insert(boardPopularity);
            jsonResult.put("message", "성공");
            jsonResult.put("status", "add");
        } else {
            // 동일한 데이터가 존재하면 제거
            if(type.equals(boardPopularity.getType())){
                boardPopularity.setDelete(true);
                boardPopularityDAO.update(boardPopularity);
                jsonResult.put("message", "취소되었습니다.");
                jsonResult.put("status", "cancel");
            } else {
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
    }
}
