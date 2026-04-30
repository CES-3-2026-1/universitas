package co.edu.poli.ces3.universitas.servlet;

import co.edu.poli.ces3.universitas.dto.Materias;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@WebServlet(name = "materiasServlet", urlPatterns = {"/materias", "/materias/quinto"})
public class MateriasServlet extends HttpServlet {
    private Vector<Materias> materias;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.materias = new Vector<>();
        // Adaptador para LocalDate
        TypeAdapter<LocalDate> localDateAdapter = new TypeAdapter<LocalDate>() {
            @Override
            public void write(JsonWriter out, LocalDate value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value.toString());
                }
            }
            @Override
            public LocalDate read(JsonReader in) throws IOException {
                String str = in.nextString();
                return LocalDate.parse(str);
            }
        };
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, localDateAdapter)
                .create();
        // Materias de prueba
        materias.add(new Materias("MAT101", "Algebra", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 6, 1), 5));
        materias.add(new Materias("MAT102", "Fisica", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 6, 1), 5));
        materias.add(new Materias("MAT103", "Historia", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 6, 1), 4));
        materias.add(new Materias("MAT104", "Calculo", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 6, 1), 5));
        materias.add(new Materias("MAT105", "Literat", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 6, 1), 5));
        materias.add(new Materias("MAT106", "Program", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 6, 1), 5));
        materias.add(new Materias("MAT107", "Biologia", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 6, 1), 3));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String path = request.getServletPath();
        String code = request.getParameter("code");

        if (path.equals("/materias/quinto")) {
            // Solo materias de 5to semestre y nombre <= 8 caracteres
            List<Materias> filtered = materias.stream()
                    .filter(m -> m.getSemestre() == 5 && m.getName().length() <= 8)
                    .collect(Collectors.toList());
            if (filtered.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"No existen materias del quinto semestre con nombre de hasta 8 caracteres\"}");
            } else {
                out.print(this.gson.toJson(filtered));
            }
        } else if (code != null && !code.isEmpty()) {
            Materias found = null;
            for (Materias m : materias) {
                if (m.getCode().equals(code)) {
                    found = m;
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
            out.print(this.gson.toJson(this.materias));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Materias nuevaMateria = this.gson.fromJson(request.getReader(), Materias.class);
        if (nuevaMateria.getCode() == null || nuevaMateria.getCode().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El código de la materia es requerido\"}");
        } else {
            this.materias.add(nuevaMateria);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print(this.gson.toJson(nuevaMateria));
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String code = request.getParameter("code");
        Materias materiaActualizada = this.gson.fromJson(request.getReader(), Materias.class);

        if (code == null || code.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El código de la materia es requerido\"}");
        } else {
            int index = -1;
            for (int i = 0; i < materias.size(); i++) {
                if (materias.get(i).getCode().equals(code)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                materiaActualizada.setCode(code);
                materias.set(index, materiaActualizada);
                out.print(this.gson.toJson(materiaActualizada));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Materia no encontrada\"}");
            }
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El código de la materia es requerido\"}");
        } else {
            Materias materiaAEliminar = null;
            for (Materias m : materias) {
                if (m.getCode().equals(code)) {
                    materiaAEliminar = m;
                    break;
                }
            }
            if (materiaAEliminar != null) {
                materias.remove(materiaAEliminar);
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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getMethod();
        if (method.equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            try {
                super.service(req, resp);
            } catch (ServletException e) {
                throw new IOException(e);
            }
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String code = request.getParameter("code");
        Materias materiaParcial = this.gson.fromJson(request.getReader(), Materias.class);

        if (code == null || code.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"El código de la materia es requerido\"}");
        } else {
            Materias materiaExistente = null;
            for (Materias m : materias) {
                if (m.getCode().equals(code)) {
                    materiaExistente = m;
                    break;
                }
            }
            if (materiaExistente != null) {
                if (materiaParcial.getName() != null) {
                    materiaExistente.setName(materiaParcial.getName());
                }
                if (materiaParcial.getFechaInicio() != null) {
                    materiaExistente.setFechaInicio(materiaParcial.getFechaInicio());
                }
                if (materiaParcial.getFechaFin() != null) {
                    materiaExistente.setFechaFin(materiaParcial.getFechaFin());
                }
                if (materiaParcial.getSemestre() != 0) {
                    materiaExistente.setSemestre(materiaParcial.getSemestre());
                }
                out.print(this.gson.toJson(materiaExistente));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Materia no encontrada\"}");
            }
        }
        out.flush();
    }
}

