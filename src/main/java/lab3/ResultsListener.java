package lab3;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@WebListener
public class ResultsListener implements ServletContextListener {
    public static final String ATTR = "results";

    @Override
    public void contextInitialized(ServletContextEvent e) {
        List<Result> list = new CopyOnWriteArrayList<>();
        e.getServletContext().setAttribute(ATTR, list);
    }

}
