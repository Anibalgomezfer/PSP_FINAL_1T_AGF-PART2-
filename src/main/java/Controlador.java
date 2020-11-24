
import java.sql.*;
import java.util.ArrayList;

public class Controlador {
    public static void main(String[] args) {
        int numRegistros = 0;
        int numHilos = 5;
        int ingresosBBDD = 0;

        ArrayList<Empleado> arrayEmpleados = new ArrayList<>();
        leerRegistrosHilos(numRegistros, numHilos, ingresosBBDD, arrayEmpleados);
        leerRegistrosSecuencial(numRegistros, ingresosBBDD);


    }

    private static void leerRegistrosHilos(int numRegistros, int numHilos, int ingresosBBDD, ArrayList<Empleado> arrayEmpleados) {

        long startTime = System.currentTimeMillis();

        try {
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/bbdd_psp_1", "DAM2020_PSP", "DAM2020_PSP");
            Statement consulta = conexion.createStatement();
            ResultSet resultSet = consulta.executeQuery("SELECT COUNT(*) from empleados");
            while (resultSet.next()) {
                numRegistros = resultSet.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        int modulo = (numRegistros % numHilos);
        int numRegistrosPorHilo = numRegistros / numHilos;

        for (int indice = 0; indice < numHilos; indice++) {
            if (indice == (numHilos-1)){
                numRegistrosPorHilo = numRegistrosPorHilo+modulo;
                Empleado hilo = new Empleado(numRegistros, numHilos, numRegistrosPorHilo, indice, modulo);
                arrayEmpleados.add(hilo);
            }else{
                modulo=0;
                Empleado hilo = new Empleado(numRegistros, numHilos, numRegistrosPorHilo, indice, modulo);
                arrayEmpleados.add(hilo);
            }
        }

        for (int j = 0; j < arrayEmpleados.size(); j++) {
            arrayEmpleados.get(j).run();
            ingresosBBDD += arrayEmpleados.get(j).getIngresosBBDD();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("La suma de los ingresos de todos los empleados es: " + ingresosBBDD);
        System.out.println("Este proceso ha tardado " + (endTime - startTime)/100 + " milisegundos utilizando 5 hilos.");
    }


    private static void leerRegistrosSecuencial(int numRegistros, int ingresosBBDD) {

        long startTime = System.currentTimeMillis();

        try {
            Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/bbdd_psp_1", "DAM2020_PSP", "DAM2020_PSP");
            Statement consulta = conexion.createStatement();
            ResultSet resultSet = consulta.executeQuery("SELECT COUNT(*) from empleados");
            while (resultSet.next()) {
                numRegistros = resultSet.getInt(1);
            }

            for (int j = 0; j <= numRegistros; j++) {
                ResultSet resultSet2 = consulta.executeQuery("select * from empleados where ID=" + j);
                while (resultSet2.next()) {
                    ingresosBBDD += resultSet2.getInt(3);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("La suma de los ingresos de todos los empleados es: " + ingresosBBDD);
        System.out.println("Este proceso ha tardado " + (endTime - startTime) + " milisegundos leyendo de manera secuencial.");
    }
}
