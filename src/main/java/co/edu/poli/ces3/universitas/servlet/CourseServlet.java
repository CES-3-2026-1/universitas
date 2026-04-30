package co.edu.poli.ces3.universitas.servlet;

import co.edu.poli.ces3.universitas.dto.Course;
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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "courseServlet", value = "/course")
public class CourseServlet extends HttpServlet {
    private Vector<Course> courses;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.courses = new Vector<>();
        this.gson = new Gson();

        // Datos de prueba
        // COINCIDEN: Semestre 5 y Nombre <= 8 caracteres
        courses.add(new Course("101", "C001", "Fisica", "2026-01-20", "2026-06-15", 5)); // ID fijo para pruebas
        courses.add(new Course(UUID.randomUUID().toString(), "C002", "Quimica", "2026-01-20", "2026-06-15", 5));
        
        // NO COINCIDEN: Semestre 5 pero Nombre > 8 caracteres (11 chars)
        courses.add(new Course(UUID.randomUUID().toString(), "C003", "Matematicas", "2026-01-20", "2026-06-15", 5));
        
        // NO COINCIDEN: Semestre != 5 (Semestre 3)
        courses.add(new Course(UUID.randomUUID().toString(), "C004", "Ingles", "2026-01-20", "2026-06-15", 3));
    }

    // Maneja GET: Retorna solo cursos de semestre 5 con nombre <= 8 chars
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        List<Course> filteredCourses = courses.stream()
                .filter(c -> c.getSemester() == 5)
                .filter(c -> c.getName() != null && c.getName().length() <= 8)
                .collect(Collectors.toList());

        if (filteredCourses.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"No se encontraron materias del semestre 5 con nombre corto (<= 8 caracteres)\"}");
        } else {
            out.print(this.gson.toJson(filteredCourses));
        }
        out.flush();
    }

    // Maneja POST: Crea un nuevo curso
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Course newCourse = this.gson.fromJson(request.getReader(), Course.class);
        if (newCourse.getId() == null || newCourse.getId().isEmpty()) {
            newCourse.setId(UUID.randomUUID().toString());
        }

        this.courses.add(newCourse);
        response.setStatus(HttpServletResponse.SC_CREATED);
        out.print(this.gson.toJson(newCourse));
        out.flush();
    }

    // Maneja PUT: Actualiza un curso por ID en URL
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        Course updated = this.gson.fromJson(request.getReader(), Course.class);

        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID del curso es requerido\"}");
        } else {
            boolean found = false;
            for (int i = 0; i < courses.size(); i++) {
                if (courses.get(i).getId().equals(id)) {
                    updated.setId(id);
                    courses.set(i, updated);
                    found = true;
                    break;
                }
            }
            if (found) {
                out.print(this.gson.toJson(updated));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Curso no encontrado\"}");
            }
        }
        out.flush();
    }

    // Maneja DELETE: Borra un curso por ID en URL
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID del curso es requerido\"}");
        } else {
            Course toDelete = null;
            for (Course c : courses) {
                if (c.getId().equals(id)) {
                    toDelete = c;
                    break;
                }
            }
            if (toDelete != null) {
                courses.remove(toDelete);
                out.print("{\"message\": \"Curso eliminado correctamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"El curso no existe\"}");
            }
        }
        out.flush();
    }
}
