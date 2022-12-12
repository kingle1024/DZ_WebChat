package Page;

import lombok.Data;

@Data
public class BoardParam {
    long pageIndex;
    long pageSize;

    String searchType;
    String search;
    String type;

    /*
    limit 0, 10 --> pageIndex : 1
    limit 10, 10 --> pageIndex : 2
    limit 20, 30    --> pageIndex : 3
     */
    public long getPageStart(){
        init(); // 초기화
        return (pageIndex - 1) * pageSize;
    }

    public long getPageEnd(){
        init();
        return pageSize;
    }

    public void init(){
        if(pageIndex < 1){
            pageIndex = 1;
        }
        if(pageSize < 10){
            pageSize = 10;
        }
    }

    public String getQueryString() {
        init();
        StringBuilder sb = new StringBuilder();

        if(search != null && search.length() > 0){
            if(sb.length() > 0){ // searchType이 있을 때 &로 묶어줌
                sb.append("&");
            }
            sb.append(String.format("search=%s", search));
        }
        if(type != null){
            if(sb.length() > 0){ // searchType이 있을 때 &로 묶어줌
                sb.append("&");
            }
            sb.append(String.format("type=%s", type));
        }

        return sb.toString();
    }

}
