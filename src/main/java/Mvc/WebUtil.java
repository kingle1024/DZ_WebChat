package Mvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class WebUtil {
    private static DataSource dataSource;
    public static void setDataSource(DataSource dataSource){
        WebUtil.dataSource = dataSource;
    }
    public static Connection getConnection(){
        if(WebUtil.dataSource == null) return null;
        try {
            return WebUtil.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
