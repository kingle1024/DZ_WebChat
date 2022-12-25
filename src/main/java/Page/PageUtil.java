package Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class PageUtil {
    private List<?> list;
    // 전체 개수
    private final long totalCount;
    // 한 페이지에 나오는 개수
    private long pageSize;
    // 현재 페이지 번호
    private long pageIndex;
    // 시작페이지
    private long startPage;
    // 종료 페이지
    private long endPage;
    // 페이지 이동 시 전달되는 파라미터(쿼리xmfld)
    private final String queryString;

    public String paper(){
        init();
        StringBuilder sb = new StringBuilder();

        // 이전 페이지 처리
        if(startPage != pageIndex && totalCount != 0) {
            sb.append(String.format("<a href=\"#\" onclick=\"searchF('%d')\">&lt;&lt;</a>", 1));
            sb.append("&nbsp;");
            sb.append(String.format("<a href=\"#\" onclick=\"searchF('%d')\">&lt;</a>", pageIndex-1));
        }

        for(long i = startPage; i<= endPage; i++){
            if(i == pageIndex)
                sb.append(String.format("<span style='font-size:1.3rem;'> %d </span>", i));
            else {
                sb.append(String.format("<a href=\"#\" onclick=\"searchF('%d')\">%d</a>", i, i));
            }
            sb.append(System.lineSeparator());
        }

        if(endPage != pageIndex && totalCount != 0){
            sb.append(String.format("<a href=\"#\" onclick=\"searchF('%d')\">&gt;</a>", pageIndex+1));
            sb.append("&nbsp;");
            sb.append(String.format("<a href=\"#\" onclick=\"searchF('%d')\">&gt; &gt;</a>", (int) Math.ceil(totalCount / 10)+1));
        }

        sb.append(System.lineSeparator());

        return sb.toString();
    }

    private void init(){
        if(pageIndex < 1){
            pageIndex = 1;
        }
        if(pageSize < 1){
            pageSize = 1;
        }
        // 전체페이지 블럭 개수
        long totalBlockCount = totalCount / pageSize + (totalCount % pageSize > 0 ? 1 : 0);
        // 페이지블럭 개수
        final long pageBlockSize = 10;
        startPage = ((pageIndex - 1) / pageBlockSize) * pageBlockSize + 1;
        endPage = startPage + pageBlockSize - 1;

        if(endPage > totalBlockCount){
            endPage = totalBlockCount;
        }
    }
}
