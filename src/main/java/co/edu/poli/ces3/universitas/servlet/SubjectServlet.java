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
        subjects.add(new Subject(UUID.randomUUID().toString(), "Matemáticas", 4));
        subjects.add(new Subject(UUID.randomUUID().toString(), "Programación", 3));
        subjects.add(new Subject("sub-123", "Bases de Datos", 3));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String subjectId = request.getParameter("id");

        if (subjectId != null && !subjectId.isEmpty()) {
            Subject found = null;
            for (Subject s : subjects) {
                if (s.getId().equals(subjectId)) {
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

        try {
            Subject newSubject = this.gson.fromJson(request.getReader(), Subject.class);
            if (newSubject.getId() == null || newSubject.getId().isEmpty()) {
                newSubject.setId(UUID.randomUUID().toString());
            }
            this.subjects.add(newSubject);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print(this.gson.toJson(newSubject));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al crear materia: " + e.getMessage() + "\"}");
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String subjectId = request.getParameter("id");
        if (subjectId == null || subjectId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID de la materia es requerido en la URL\"}");
            out.flush();
            return;
        }

        try {
            Subject updatedSubject = this.gson.fromJson(request.getReader(), Subject.class);
            if (updatedSubject == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El cuerpo de la petición no puede estar vacío\"}");
                out.flush();
                return;
            }

            if (updatedSubject.getId() != null && !updatedSubject.getId().equals(subjectId)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El ID del cuerpo no coincide con el ID de la URL\"}");
                out.flush();
                return;
            }

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
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al procesar el JSON: " + e.getMessage() + "\"}");
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
            Subject toDelete = null;
            for (Subject s : subjects) {
                if (s.getId().equals(subjectId)) {
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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
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
        if (subjectId == null || subjectId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El ID de la materia es requerido en la URL\"}");
            out.flush();
            return;
        }

        try {
            Subject patchData = this.gson.fromJson(request.getReader(), Subject.class);
            if (patchData == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"El cuerpo de la petición no puede estar vacío\"}");
                out.flush();
                return;
            }

            Subject toUpdate = null;
            for (Subject s : subjects) {
                if (s.getId().equals(subjectId)) {
                    toUpdate = s;
                    break;
                }
            }

            if (toUpdate != null) {
                if (patchData.getName() != null) toUpdate.setName(patchData.getName());
                if (patchData.getCredits() != 0) toUpdate.setCredits(patchData.getCredits());
                out.print(this.gson.toJson(toUpdate));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Materia no encontrada\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Error al procesar el JSON: " + e.getMessage() + "\"}");
        }
        out.flush();
    }
}
