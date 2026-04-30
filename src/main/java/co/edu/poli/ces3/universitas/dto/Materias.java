package co.edu.poli.ces3.universitas.dto;

import java.time.LocalDate;

public class Materias {
    private String code;
    private String name;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int semestre;

    public Materias() {
    }

    public Materias(String code, String name, LocalDate fechaInicio, LocalDate fechaFin, int semestre) {
        this.code = code;
        this.name = name;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.semestre = semestre;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return "Materias{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", semestre=" + semestre +
                '}';
    }
}

