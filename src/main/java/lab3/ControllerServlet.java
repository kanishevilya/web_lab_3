package lab3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        boolean hasPoint = isNotEmpty(req.getParameter("x"))
                && isNotEmpty(req.getParameter("y"))
                && isNotEmpty(req.getParameter("r"));

        String target = hasPoint ? "/check" : "/index.jsp";
        req.getRequestDispatcher(target).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
}