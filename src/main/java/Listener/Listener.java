package Listener;

import Board.BoardDAO;
import BoardPopularity.BoardPopularityDAO;
import Chat.ChatDAO;
import File.BoardFileDAO;
import Member.MemberDAO;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class Listener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            Context ctx = new InitialContext();
            Context envContext = (Context) ctx.lookup("java:/comp/env");
            DataSource dataFactory = (DataSource) envContext.lookup("jdbc/maria");
            MemberDAO.setDataFactory(dataFactory);
            BoardDAO.setDataFactory(dataFactory);
            BoardFileDAO.setDataFactory(dataFactory);
            BoardPopularityDAO.setDataFactory(dataFactory);
            ChatDAO.setDataFactory(dataFactory);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
