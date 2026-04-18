package co.edu.poli.ces3.universitas.servlet;

import co.edu.poli.ces3.universitas.dto.Student;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

@WebServlet(name = "studentServlet", value = "/student")
public class StudentServlet extends HttpServlet {
    private Vector<Student> students;
    private Gson gson;

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");

        if (studentId != null && !studentId.isEmpty()) {
            Student foundStudent = null;
            for (Student s : students) {
                if (s.getId().equals(studentId)) {
                    foundStudent = s;
                    break;
                }
            }

            if (foundStudent != null) {
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
        }
        out.flush();
    }
}
