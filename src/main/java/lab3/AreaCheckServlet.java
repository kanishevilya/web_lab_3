package lab3;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {
    private static final Set<Double> Y_VALUES = Set.of(-2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0);
    private static final Set<Double> R_VALUES = Set.of(1.0, 1.5, 2.0, 2.5, 3.0);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI)==null){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Используйте /controller");
            return;
        }
        long start = System.nanoTime();

        double x, y, r;
        try {
            x = parse(req.getParameter("x"));
            y = parse(req.getParameter("y"));
            r = parse(req.getParameter("r"));
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректные числа");
            return;
        }

        boolean fromGraph = "graph".equals(req.getParameter("source"));
        boolean ok = x > -3 && x < 3 && R_VALUES.contains(r) && (fromGraph ? (y >= -5 && y <= 5) : Y_VALUES.contains(y));

        if(!ok){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректные числа");
            return;
        }

        boolean hit = Area.contains(x, y, r);
        Result result = new Result(x,y,r,hit, LocalDateTime.now().format(FMT), System.nanoTime()-start);

        ServletContext context = getServletContext();
        List<Result> list = (List<Result>) context.getAttribute(ResultsListener.ATTR);

        if(list==null){
            synchronized (context) {
                list = (List<Result>) context.getAttribute(ResultsListener.ATTR);
                if(list==null){
                    list = new CopyOnWriteArrayList<>();
                    context.setAttribute(ResultsListener.ATTR, list);
                }
            }
        }
        list.add(result);

        req.setAttribute("result", result);
        req.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(req, resp);
    }

    private static double parse(String s) {
        if (s == null) throw new IllegalArgumentException("null");
        double v = Double.parseDouble(s.trim().replace(',', '.'));
        if (Double.isNaN(v) || Double.isInfinite(v)) throw new IllegalArgumentException("nan");
        return v;
    }

}
