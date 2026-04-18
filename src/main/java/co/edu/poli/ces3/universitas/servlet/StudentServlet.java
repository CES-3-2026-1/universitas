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
import java.util.UUID;

@WebServlet(name = "studentServlet", value = "/student")
public class StudentServlet extends HttpServlet {
    private Vector<Student> students;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.students = new Vector<>();
        this.gson = new Gson();

        // Inicializamos con algunos estudiantes de prueba
        students.add(new Student(
                UUID.randomUUID().toString(),
                "101010",
                "Andrés",
                "Pérez",
                21,
                "andres.perez@poli.edu.co",
                "3001234567",
                "Calle 10",
                4.5
        ));
        students.add(new Student(
                UUID.randomUUID().toString(),
                "202020",
                "María",
                "López",
                19,
                "maria.lopez@poli.edu.co",
                "3119876543",
                "Carrera 5",
                4.8
        ));
        students.add(new Student(
                "123", // ID fijo para facilitar pruebas rápidas
                "303030",
                "Carlos",
                "Rodríguez",
                22,
                "carlos.rod@poli.edu.co",
                "3221112233",
                "Calle 50",
                3.9
        ));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");

        if (studentId != null && !studentId.isEmpty()) {
            // Buscamos un estudiante por ID
            Student foundStudent = null;
            for (Student s : students) {
                if (s.getId().equals(studentId)) {
                    foundStudent = s;
                    break;
                }
            }

            if (foundStudent != null) {
                out.print(this.gson.toJson(foundStudent));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Estudiante no encontrado\"}");
            }
        } else {
            // Devolvemos la lista completa
            out.print(this.gson.toJson(this.students));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Leemos el estudiante desde el cuerpo de la petición (JSON)
        Student newStudent = this.gson.fromJson(request.getReader(), Student.class);

        // Generamos un ID si no viene en la petición
        if (newStudent.getId() == null || newStudent.getId().isEmpty()) {
            newStudent.setId(UUID.randomUUID().toString());
        }

        this.students.add(newStudent);

        response.setStatus(HttpServletResponse.SC_CREATED);
        out.print(this.gson.toJson(newStudent));
        out.flush();
    }
}
