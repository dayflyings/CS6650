package servlet;

import com.google.gson.Gson;
import util.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "SkierServlet", urlPatterns = "/skiers/*")
public class SkierServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String urlPath = request.getPathInfo();
        PrintWriter out = response.getWriter();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("missing parameters");
        }

        String[] urlParts = urlPath.split("/");
        int seasonLength = 8;
        int verticalLength = 3;
        if (urlParts.length == seasonLength) {
            handleSeasons(urlParts, response, out);
        } else if (urlParts.length == verticalLength) {
            handleVertical(urlParts, response, out);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Invalid parameters");
        }
    }

    private void handleSeasons(String[] urlParts, HttpServletResponse response, PrintWriter out) {
        if (!Tools.isUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            out.write("It works!");
        }
    }

    private void handleVertical(String[] urlParts, HttpServletResponse response, PrintWriter out) {
        if (!Tools.isNumeric(urlParts[1])) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("Data not found");
        } else {
            int skierId = Integer.parseInt(urlParts[1]);
            if (skierId < 0) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("Data not found");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                Map<String, Object> result = new HashMap<>();
                result.put("seasonID", "string");
                result.put("totalVert", 0);
                out.write(gson.toJson(result));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        PrintWriter out = response.getWriter();
        String urlPath = request.getPathInfo();

        if (urlPath == null || urlPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("missing paramterers");
        }

        String[] urlParts = urlPath.split("/");
        if (!Tools.isUrlValid(urlParts)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Invalid inputs");
        } else {
            int resortIdPosition = 1;
            int seasonIdPosition = 3;
            int dayIdPosition = 5;
            int resortId = Integer.parseInt(urlParts[resortIdPosition]);
            int seasonId = Integer.parseInt(urlParts[seasonIdPosition]);
            int dayId = Integer.parseInt(urlParts[dayIdPosition]);
            if ((resortId != 1 && resortId != 2) ||
                    (seasonId != 2017 && seasonId != 2018) ||
                    (dayId < 1 || dayId > 365)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("Data not found");
            } else {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.write("Write successful:" + sb);
            }
        }
    }
}
