package com.clases.PSP_FINAL_1T_AGF_2;
import java.sql.*;

public class Empleado extends Thread {

    int numRegistrosPorHilo;
    int indice;
    int ingresosBBDD;


    public int getIngresosBBDD() {
        return ingresosBBDD;
    }

    public void setIngresosBBDD(int ingresosBBDD) {
        this.ingresosBBDD = ingresosBBDD;
    }

    public Empleado(int numRegistrosPorHilo, int indice) {
        this.numRegistrosPorHilo = numRegistrosPorHilo;
        this.indice = indice;
    }

    @Override
    public void run() {
        super.run();
        leerDatos(indice, ingresosBBDD);
    }


    public void leerDatos(int indice, int ingresosBBDD) {
        synchronized (this) {
            try {
                for (int j = (numRegistrosPorHilo * indice) + 1; j <= numRegistrosPorHilo * (indice + 1); j++) {
                    Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/bbdd_psp_1", "DAM2020_PSP", "DAM2020_PSP");
                    Statement consulta = conexion.createStatement();
                    ResultSet resultSet = consulta.executeQuery("select * from empleados where ID=" + j);
                    resultSet.next();
                    ingresosBBDD += resultSet.getInt(3);
                    conexion.close();
                }
                setIngresosBBDD(ingresosBBDD);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("La conexion a la base de datos ha fallado. \nAsegurate de que tienes encendido xampp y los modulos de Apache y MySQL.");
            }
        }
    }
}
