package co.edu.poli.ces3.universitas.servlet;

import co.edu.poli.ces3.universitas.dto.Student;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.Vector;

@WebServlet(name = "studentServlet", value = "/student")
public class StudentServlet extends HttpServlet {
    private Vector<Student> students;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        students = new Vector<>();
        // Inicializar con algunos estudiantes de prueba
        students.add(new Student(UUID.randomUUID().toString(), "101010", "Juan", "Perez", 20, "juan@test.com", "123", "Calle 1", 4.5));
        students.add(new Student(UUID.randomUUID().toString(), "202020", "Maria", "Garcia", 21, "maria@test.com", "456", "Calle 2", 4.8));
        students.add(new Student(UUID.randomUUID().toString(), "303030", "Carlos", "Rodriguez", 22, "carlos@test.com", "789", "Calle 3", 3.9));
        students.add(new Student(UUID.randomUUID().toString(), "404040", "Ana", "Martinez", 19, "ana@test.com", "012", "Calle 4", 4.2));
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
                out.print(gson.toJson(foundStudent));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Estudiante no encontrado\", \"id\": \"" + studentId + "\"}");
            }
        } else {
            out.print(gson.toJson(students));
        }
        out.flush();
    }
}
