import com.github.javafaker.Faker;

import java.sql.*;

public class Empleado extends Thread {

    private int id;
    private String email;
    private int ingresos;
    int numRegistros;
    int numHilos;
    int numRegistrosPorHilo;
    int indice;
    int ingresosBBDD;
    int modulo;

    public int getIngresosBBDD() {
        return ingresosBBDD;
    }

    public void setIngresosBBDD(int ingresosBBDD) {
        this.ingresosBBDD = ingresosBBDD;
    }

    public Empleado(int numRegistros, int numHilos, int numRegistrosPorHilo, int indice, int modulo) {
        this.numRegistros = numRegistros;
        this.numHilos = numHilos;
        this.numRegistrosPorHilo = numRegistrosPorHilo;
        this.indice = indice;
        this.modulo = modulo;
    }

    @Override
    public void run() {
        super.run();
        leerDatos(indice, ingresosBBDD);
    }


    public void leerDatos(int indice, int ingresosBBDD) {

        try {
            for (int j = (numRegistrosPorHilo * indice) + 1; j <= numRegistrosPorHilo * (indice + 1); j++) {
                ingresos = (int) (Math.random() * 990) + 10;
                email = Faker.instance().internet().emailAddress();
                Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/bbdd_psp_1", "DAM2020_PSP", "DAM2020_PSP");
                Statement consulta = conexion.createStatement();
                ResultSet resultSet = consulta.executeQuery("select * from empleados where ID=" + j);
                while (resultSet.next()){
                    ingresosBBDD += resultSet.getInt(3);
                }
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setIngresosBBDD(ingresosBBDD);
    }
}
