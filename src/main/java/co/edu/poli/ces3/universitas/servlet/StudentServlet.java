package co.edu.poli.ces3.universitas.servlet;

import co.edu.poli.ces3.universitas.dto.Student;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        System.out.println("Metodo recibido: " + method);
        switch (method) {
            case "PATCH":
                doPatch(req, resp);
                break;
            case "DELETE":
                doDelete(req, resp);
                break;
            case "PUT":
                doPut(req, resp);
                break;
            case "GET":
                doGet(req, resp);
                break;
            case "POST":
                doPost(req, resp);
                break;
            default:
                super.service(req, resp);
                break;
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");

        if (studentId != null && !studentId.isEmpty()) {
            // Buscamos un estudiante por ID
            Student foundStudent = findStudentById(studentId);

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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");
        if (studentId == null || studentId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID no proporcionado\"}");
            out.flush();
            return;
        }

        Student updatedStudent = this.gson.fromJson(request.getReader(), Student.class);
        int index = -1;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(studentId)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            updatedStudent.setId(studentId); // Mantenemos el mismo ID
            students.set(index, updatedStudent);
            out.print(this.gson.toJson(updatedStudent));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Estudiante no encontrado\"}");
        }
        out.flush();
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");
        if (studentId == null || studentId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID no proporcionado\"}");
            out.flush();
            return;
        }

        Student studentToDelete = findStudentById(studentId);

        if (studentToDelete != null) {
            students.remove(studentToDelete);
            out.print(this.gson.toJson(studentToDelete));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Estudiante no encontrado\"}");
        }
        out.flush();
    }

    public void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");
        if (studentId == null || studentId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID no proporcionado\"}");
            out.flush();
            return;
        }

        Student foundStudent = findStudentById(studentId);

        if (foundStudent != null) {
            JsonObject jsonObject = this.gson.fromJson(request.getReader(), JsonObject.class);

            if (jsonObject.has("document")) foundStudent.setDocument(jsonObject.get("document").getAsString());
            if (jsonObject.has("firstName")) foundStudent.setFirstName(jsonObject.get("firstName").getAsString());
            if (jsonObject.has("lastName")) foundStudent.setLastName(jsonObject.get("lastName").getAsString());
            if (jsonObject.has("age")) foundStudent.setAge(jsonObject.get("age").getAsInt());
            if (jsonObject.has("email")) foundStudent.setEmail(jsonObject.get("email").getAsString());
            if (jsonObject.has("phoneNumber")) foundStudent.setPhoneNumber(jsonObject.get("phoneNumber").getAsString());
            if (jsonObject.has("address")) foundStudent.setAddress(jsonObject.get("address").getAsString());
            if (jsonObject.has("gpa")) foundStudent.setGpa(jsonObject.get("gpa").getAsDouble());

            out.print(this.gson.toJson(foundStudent));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Estudiante no encontrado\"}");
        }
        out.flush();
    }

    private Student findStudentById(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }
}
