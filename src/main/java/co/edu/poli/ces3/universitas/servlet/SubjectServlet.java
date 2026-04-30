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
import java.util.ArrayList;
import java.util.List;
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
        subjects.add(new Subject("101", "Java", "2024-01-15", "2024-05-30", 5));
        subjects.add(new Subject("102", "Python", "2024-01-15", "2024-05-30", 5));
        subjects.add(new Subject("103", "Bases de Datos", "2024-01-15", "2024-05-30", 5)); // > 8 chars
        subjects.add(new Subject("104", "Web 1", "2024-01-15", "2024-05-30", 5));
        subjects.add(new Subject("105", "Cálculo", "2024-01-15", "2024-05-30", 4)); // Not semester 5
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        List<Subject> filteredSubjects = new ArrayList<>();

        for (Subject s : subjects) {
            if (s.getSemester() == 5 && s.getName().length() <= 8) {
                filteredSubjects.add(s);
            }
        }

        if (filteredSubjects.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"No se encontraron materias del semestre 5 con nombre de máximo 8 caracteres\"}");
        } else {
            out.print(this.gson.toJson(filteredSubjects));
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
            if (newSubject.getCode() == null || newSubject.getCode().isEmpty()) {
                newSubject.setCode(UUID.randomUUID().toString());
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

        String subjectCode = request.getParameter("code");
        if (subjectCode == null || subjectCode.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El código de la materia es requerido en la URL\"}");
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

            int index = -1;
            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getCode().equals(subjectCode)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                updatedSubject.setCode(subjectCode);
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

        String subjectCode = request.getParameter("code");
        if (subjectCode == null || subjectCode.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El código de la materia es requerido\"}");
        } else {
            Subject toDelete = null;
            for (Subject s : subjects) {
                if (s.getCode().equals(subjectCode)) {
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

        String subjectCode = request.getParameter("code");
        if (subjectCode == null || subjectCode.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El código de la materia es requerido en la URL\"}");
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
                if (s.getCode().equals(subjectCode)) {
                    toUpdate = s;
                    break;
                }
            }

            if (toUpdate != null) {
                if (patchData.getName() != null) toUpdate.setName(patchData.getName());
                if (patchData.getStartDate() != null) toUpdate.setStartDate(patchData.getStartDate());
                if (patchData.getEndDate() != null) toUpdate.setEndDate(patchData.getEndDate());
                if (patchData.getSemester() != 0) toUpdate.setSemester(patchData.getSemester());
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
