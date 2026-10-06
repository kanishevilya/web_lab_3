package lab3;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {
    private static final Set<String> ALLOWED_Y = Set.of("-2", "-1.5", "-1", "-0.5", "0", "0.5", "1", "1.5", "2");
    private static final Set<String> ALLOWED_R = Set.of("1", "1.5", "2", "2.5", "3");

    private static final BigDecimal THREE = new BigDecimal("3");
    private static final BigDecimal MINUS_THREE = new BigDecimal("-3");
    private static final BigDecimal FIVE = new BigDecimal("5");
    private static final BigDecimal MINUS_FIVE = new BigDecimal("-5");

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Прямой доступ запрещен. Используйте /controller");
            return;
        }
        long start = System.nanoTime();

        String rawX = req.getParameter("x");
        String rawY = req.getParameter("y");
        String rawR = req.getParameter("r");
        String source = req.getParameter("source");

        BigDecimal x, y, r;
        try {
            x = parseBigDecimal(rawX);
            y = parseBigDecimal(rawY);
            r = parseBigDecimal(rawR);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный формат чисел");
            return;
        }

        boolean fromGraph = "graph".equals(source);

        boolean rValid = false;
        for (String val : ALLOWED_R) {
            if (r.compareTo(new BigDecimal(val)) == 0) {
                rValid = true;
                break;
            }
        }

        boolean xValid = x.compareTo(MINUS_THREE) > 0 && x.compareTo(THREE) < 0;

        boolean yValid = false;
        if (fromGraph) {
            yValid = y.compareTo(MINUS_FIVE) >= 0 && y.compareTo(FIVE) <= 0;
        } else {
            for (String val : ALLOWED_Y) {
                if (y.compareTo(new BigDecimal(val)) == 0) {
                    yValid = true;
                    break;
                }
            }
        }

        if (!xValid || !yValid || !rValid) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Значения выходят за пределы допустимых диапазонов");
            return;
        }

        boolean hit = Area.contains(x, y, r);
        Result result = new Result(x, y, r, hit, Instant.now().toString(), System.nanoTime() - start);

        ServletContext context = getServletContext();
        List<Result> list = (List<Result>) context.getAttribute(ResultsListener.ATTR);
        if (list == null) {
            synchronized (context) {
                list = (List<Result>) context.getAttribute(ResultsListener.ATTR);
                if (list == null) {
                    list = new CopyOnWriteArrayList<>();
                    context.setAttribute(ResultsListener.ATTR, list);
                }
            }
        }
        list.add(result);

        req.setAttribute("result", result);
        req.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(req, resp);
    }

    private static BigDecimal parseBigDecimal(String s) {
        if (s == null) throw new IllegalArgumentException("null");
        String trimmed = s.trim().replace(',', '.');
        if (trimmed.isEmpty()) throw new IllegalArgumentException("empty");
        return new BigDecimal(trimmed);
    }
}