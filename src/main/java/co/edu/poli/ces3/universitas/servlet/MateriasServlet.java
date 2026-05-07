package co.edu.poli.ces3.universitas.servlet;

import co.edu.poli.ces3.universitas.dto.Materia;
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
import java.util.stream.Collectors;

@WebServlet(name = "materiasServlet", value = "/materias")
public class MateriasServlet extends HttpServlet {
    private List<Materia> materias;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.materias = new ArrayList<>();
        this.gson = new Gson();

        // Datos de prueba
        materias.add(new Materia("MAT1", "Calculo", "2024-01-01", "2024-06-01", 1));
        materias.add(new Materia("MAT2", "Fisica I", "2024-01-01", "2024-06-01", 5)); // Longitud 8, Semestre 5 -> OK
        materias.add(new Materia("MAT3", "Programacion Web", "2024-01-01", "2024-06-01", 5)); // Longitud > 8 -> NO
        materias.add(new Materia("MAT4", "BD I", "2024-01-01", "2024-06-01", 5)); // Longitud 4, Semestre 5 -> OK
        materias.add(new Materia("MAT5", "Redes", "2024-01-01", "2024-06-01", 4)); // Semestre 4 -> NO
        materias.add(new Materia("MAT6", "Ingenieria", "2024-01-01", "2024-06-01", 5)); // Longitud 10 -> NO
        materias.add(new Materia("MAT7", "Logica", "2024-01-01", "2024-06-01", 5)); // Longitud 6, Semestre 5 -> OK
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        List<Materia> filteredMaterias = materias.stream()
                .filter(m -> m.getSemestre() == 5)
                .filter(m -> m.getNombre().length() <= 8)
                .collect(Collectors.toList());

        if (filteredMaterias.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"No se encontraron materias del semestre 5 con nombre de hasta 8 caracteres\"}");
        } else {
            out.print(this.gson.toJson(filteredMaterias));
        }
        out.flush();
    }
}
