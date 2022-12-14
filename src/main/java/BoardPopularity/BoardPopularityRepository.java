package BoardPopularity;

public interface BoardPopularityRepository {
    long findByBnoAndType(String bno, String type);
    BoardPopularity findByBnoAndUserIdAndIsDelete(String bno, String userId);
    boolean insert(BoardPopularity boardPopularity);
    boolean update(BoardPopularity boardPopularity);
}
