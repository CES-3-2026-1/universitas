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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");

        if (studentId == null || studentId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID del estudiante es requerido\"}");
        } else {
            Student studentToDelete = null;
            for (Student s : students) {
                if (s.getId().equals(studentId)) {
                    studentToDelete = s;
                    break;
                }
            }

            if (studentToDelete != null) {
                students.remove(studentToDelete);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"message\": \"Estudiante eliminado correctamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"El estudiante no existe\"}");
            }
        }
        out.flush();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");
        if (studentId == null || studentId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID del estudiante es requerido en la URL\"}");
            out.flush();
            return;
        }

        try {
            Student updatedStudent = this.gson.fromJson(request.getReader(), Student.class);
            if (updatedStudent == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El cuerpo de la petición no puede estar vacío\"}");
                out.flush();
                return;
            }

            if (updatedStudent.getId() != null && !updatedStudent.getId().equals(studentId)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El ID del cuerpo no coincide con el ID de la URL\"}");
                out.flush();
                return;
            }

            int index = -1;
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId().equals(studentId)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                updatedStudent.setId(studentId); // Aseguramos que el ID se mantenga
                students.set(index, updatedStudent);
                out.print(this.gson.toJson(updatedStudent));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Estudiante no encontrado\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al procesar el JSON: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("id");
        if (studentId == null || studentId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID del estudiante es requerido en la URL\"}");
            out.flush();
            return;
        }

        try {
            Student patchData = this.gson.fromJson(request.getReader(), Student.class);
            if (patchData == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El cuerpo de la petición no puede estar vacío\"}");
                out.flush();
                return;
            }

            if (patchData.getId() != null && !patchData.getId().equals(studentId)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El ID del cuerpo no coincide con el ID de la URL\"}");
                out.flush();
                return;
            }

            Student studentToUpdate = null;
            for (Student s : students) {
                if (s.getId().equals(studentId)) {
                    studentToUpdate = s;
                    break;
                }
            }

            if (studentToUpdate != null) {
                if (patchData.getFirstName() != null) studentToUpdate.setFirstName(patchData.getFirstName());
                if (patchData.getLastName() != null) studentToUpdate.setLastName(patchData.getLastName());
                if (patchData.getDocument() != null) studentToUpdate.setDocument(patchData.getDocument());
                if (patchData.getAge() != 0) studentToUpdate.setAge(patchData.getAge());
                if (patchData.getEmail() != null) studentToUpdate.setEmail(patchData.getEmail());
                if (patchData.getPhoneNumber() != null) studentToUpdate.setPhoneNumber(patchData.getPhoneNumber());
                if (patchData.getAddress() != null) studentToUpdate.setAddress(patchData.getAddress());
                if (patchData.getGpa() != 0.0) studentToUpdate.setGpa(patchData.getGpa());
                if (patchData.getSubjects() != null && !patchData.getSubjects().isEmpty()) studentToUpdate.setSubjects(patchData.getSubjects());
                
                out.print(this.gson.toJson(studentToUpdate));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Estudiante no encontrado\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al procesar el JSON: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

}
