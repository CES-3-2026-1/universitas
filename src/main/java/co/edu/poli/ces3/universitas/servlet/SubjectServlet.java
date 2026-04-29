package co.edu.poli.ces3.universitas.servlet;

import co.edu.poli.ces3.universitas.dto.Subject;
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

@WebServlet(name = "subjectServlet", value = "/subject")
public class SubjectServlet extends HttpServlet {
    private Vector<Subject> subjects;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.subjects = new Vector<>();
        this.gson = new Gson();

        // Sample data
        subjects.add(new Subject(UUID.randomUUID().toString(), "MAT101", "Matemáticas I"));
        subjects.add(new Subject(UUID.randomUUID().toString(), "PROG1", "Programación I"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");

        if (id != null && !id.isEmpty()) {
            Subject found = null;
            for (Subject s : subjects) {
                if (s.getId().equals(id)) {
                    found = s;
                    break;
                }
            }
            if (found != null) {
                out.print(this.gson.toJson(found));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Materia no encontrada\"}");
            }
        } else {
            out.print(this.gson.toJson(this.subjects));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Subject newSubject = this.gson.fromJson(request.getReader(), Subject.class);
        if (newSubject.getId() == null || newSubject.getId().isEmpty()) {
            newSubject.setId(UUID.randomUUID().toString());
        }

        this.subjects.add(newSubject);
        response.setStatus(HttpServletResponse.SC_CREATED);
        out.print(this.gson.toJson(newSubject));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        Subject updated = this.gson.fromJson(request.getReader(), Subject.class);

        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID de la materia es requerido\"}");
        } else {
            boolean found = false;
            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getId().equals(id)) {
                    updated.setId(id);
                    subjects.set(i, updated);
                    found = true;
                    break;
                }
            }
            if (found) {
                out.print(this.gson.toJson(updated));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Materia no encontrada\"}");
            }
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID de la materia es requerido\"}");
        } else {
            Subject toDelete = null;
            for (Subject s : subjects) {
                if (s.getId().equals(id)) {
                    toDelete = s;
                    break;
                }
            }
            if (toDelete != null) {
                subjects.remove(toDelete);
                out.print("{\"message\": \"Materia eliminada correctamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"La materia no existe\"}");
            }
        }
        out.flush();
    }
}
