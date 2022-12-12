package Page;

public class PageUtil {
    // 전체 개수
    private long totalCount;
    // 한 페이지에 나오는 개수
    private long pageSize = 10;
    // 페이지블럭 개수
    private long pageBlockSize = 10;
    // 현재 페이지 번호
    private long pageIndex;
    // 시작페이지
    private long startPage;
    // 종료 페이지
    private long endPage;
    // 페이지 이동 시 전달되는 파라미터(쿼리xmfld)
    private final String queryString;

    public PageUtil(long totalCount, long pageSize, long pageIndex, String queryString){
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.queryString = queryString;
    }
    public String paper(){
        init();
        StringBuilder sb = new StringBuilder();

        String addQueryString = "";
        if(queryString != null && queryString.length() > 0){
            addQueryString = "&" + queryString;
        }

        for(long i = startPage; i<= endPage; i++){
            if(i == pageIndex)
                sb.append(String.format("<a class='on' href='?pageIndex=%d%s'>%d</a>", i, addQueryString, i));
            else
                sb.append(String.format("<a href='?pageIndex=%d%s'>%d</a>", i, addQueryString, i));
            sb.append(System.lineSeparator());
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
        startPage = ((pageIndex - 1) / pageBlockSize) * pageBlockSize + 1;
        endPage = startPage + pageBlockSize - 1;

        if(endPage > totalBlockCount){
            endPage = totalBlockCount;
        }
    }
}
