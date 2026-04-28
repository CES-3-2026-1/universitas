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

        // Inicializamos con algunas materias de prueba
        subjects.add(new Subject(UUID.randomUUID().toString(), "Programación III"));
        subjects.add(new Subject(UUID.randomUUID().toString(), "Bases de Datos I"));
        subjects.add(new Subject(UUID.randomUUID().toString(), "Ingeniería de Software"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String subjectId = request.getParameter("id");

        if (subjectId != null && !subjectId.isEmpty()) {
            Subject foundSubject = null;
            for (Subject s : subjects) {
                if (s.getId().equals(subjectId)) {
                    foundSubject = s;
                    break;
                }
            }

            if (foundSubject != null) {
                out.print(this.gson.toJson(foundSubject));
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

        String subjectId = request.getParameter("id");
        Subject updatedSubject = this.gson.fromJson(request.getReader(), Subject.class);

        if (subjectId == null || subjectId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID de la materia es requerido\"}");
        } else {
            int index = -1;
            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getId().equals(subjectId)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                updatedSubject.setId(subjectId);
                subjects.set(index, updatedSubject);
                out.print(this.gson.toJson(updatedSubject));
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

        String subjectId = request.getParameter("id");

        if (subjectId == null || subjectId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID de la materia es requerido\"}");
        } else {
            Subject subjectToDelete = null;
            for (Subject s : subjects) {
                if (s.getId().equals(subjectId)) {
                    subjectToDelete = s;
                    break;
                }
            }

            if (subjectToDelete != null) {
                subjects.remove(subjectToDelete);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"message\": \"Materia eliminada correctamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"La materia no existe\"}");
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

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String subjectId = request.getParameter("id");
        Subject partialSubject = this.gson.fromJson(request.getReader(), Subject.class);

        if (subjectId == null || subjectId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID de la materia es requerido\"}");
        } else {
            Subject existingSubject = null;
            for (Subject s : subjects) {
                if (s.getId().equals(subjectId)) {
                    existingSubject = s;
                    break;
                }
            }

            if (existingSubject != null) {
                if (partialSubject.getName() != null) {
                    existingSubject.setName(partialSubject.getName());
                }
                out.print(this.gson.toJson(existingSubject));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Materia no encontrada\"}");
            }
        }
        out.flush();
    }
}
