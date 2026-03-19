package co.edu.poli.ces3.universitas;

import co.edu.poli.ces3.universitas.dto.Student;

import java.sql.Struct;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Student juan = new Student();
        int a = 2147483647;
        float v = 5;
        double y = 9.9;
        boolean p = true;
        char c = 'd';
        long l = 888;
        short s = 8889;
        String nombre = new String( "d");
        Integer myNumber = 9;
        Double myDouble = new Double("34343");
        //juan.getId();

        // Estructura de datos estática: Arreglo de tamaño fijo
        Student[] students = new Student[3];

        // Crear y almacenar estudiantes
        students[0] = new Student(
                UUID.randomUUID().toString(),
                "10101010",
                "Carlos",
                "Pérez",
                20,
                "carlos.perez@universitas.edu.co",
                "3001234567",
                "Calle 10 #20-30",
                4.5
        );

        students[1] = new Student(
                UUID.randomUUID().toString(),
                "20202020",
                "Lucía",
                "Gómez",
                19,
                "lucia.gomez@universitas.edu.co",
                "3109876543",
                "Carrera 50 #10-15",
                4.8
        );

        students[2] = new Student(
                UUID.randomUUID().toString(),
                "30303030",
                "Andrés",
                "Rodríguez",
                22,
                "andres.rod@universitas.edu.co",
                "3201112233",
                "Avenida Siempre Viva 123",
                3.9
        );

        // Imprimir los estudiantes almacenados en el arreglo
        System.out.println("Lista de Estudiantes Almacenados:");
        for (int i = 0; i < students.length; i++) {
            System.out.println("Posición [" + i + "]: " + students[i]);
        }
    }
}
