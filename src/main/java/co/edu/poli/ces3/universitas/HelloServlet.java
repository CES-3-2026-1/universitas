package co.edu.poli.ces3.universitas;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;
    private String[] students;

    public void init() {
        message = "Listado de Estudiantes";
        students = new String[]{
                "Juan Perez",
                "Maria Garcia",
                "Carlos Rodriguez",
                "Ana Martinez",
                "Luis Hernandez",
                "Elena Gomez"
        };
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Universitas - Estudiantes</title>");
        out.println("<style>");
        out.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }");
        out.println(".container { background: white; padding: 2rem; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); width: 300px; }");
        out.println("h1 { color: #2c3e50; text-align: center; font-size: 1.5rem; margin-bottom: 1.5rem; border-bottom: 2px solid #3498db; padding-bottom: 0.5rem; }");
        out.println("ul { list-style: none; padding: 0; }");
        out.println("li { padding: 10px; border-bottom: 1px solid #eee; color: #555; transition: background 0.3s; }");
        out.println("li:last-child { border-bottom: none; }");
        out.println("li:hover { background-color: #e8f4fd; color: #3498db; cursor: pointer; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>" + message + "</h1>");
        out.println("<ul>");
        for (String student : students) {
            out.println("<li>" + student + "</li>");
        }
        out.println("</ul>");
        out.println("</div>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}