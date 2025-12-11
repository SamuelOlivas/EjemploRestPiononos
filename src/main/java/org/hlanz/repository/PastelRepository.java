package org.hlanz.repository;

import org.hlanz.entity.Pastel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PastelRepository {
    private static PastelRepository instance;

    // Atributos para la conexion a la base de datos
    private final String URL = "jdbc:postgresql://localhost:5432/Piononos";
    private final String USER = "postgres";
    private final String PASSWORD = "Rosaprofe.11";
    private Connection conexion = null;

    // Singleton
    private PastelRepository() {
        realizarConexion();
    }

    public static PastelRepository getInstance() {
        if (instance == null) {
            instance = new PastelRepository();
        }
        return instance;
    }

    private void realizarConexion() {
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion a base de datos Piononos realizada con exito");
        } catch (SQLException e) {
            System.out.println("No se conecto con la base de datos, error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Pastel> obtenerTodos() {
        List<Pastel> pasteles = new ArrayList<>();
        String sql = "SELECT * FROM pastel";

        try {
            Statement st = conexion.createStatement();
            ResultSet resultado = st.executeQuery(sql);

            while (resultado.next()) {
                Pastel pastel = new Pastel();
                pastel.setId(resultado.getLong("id"));
                pastel.setNombre(resultado.getString("nombre"));
                pastel.setSabor(resultado.getString("sabor"));
                pastel.setPrecio(resultado.getDouble("precio"));
                pastel.setPorciones(resultado.getInt("porciones"));
                pasteles.add(pastel);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los pasteles: " + e.getMessage());
            e.printStackTrace();
        }

        return pasteles;
    }

    public Pastel obtenerPorId(Long id) {
        String sql = "SELECT * FROM pastel WHERE id = ?";
        Pastel pastel = null;

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet resultado = pstmt.executeQuery();

            if (resultado.next()) {
                pastel = new Pastel();
                pastel.setId(resultado.getLong("id"));
                pastel.setNombre(resultado.getString("nombre"));
                pastel.setSabor(resultado.getString("sabor"));
                pastel.setPrecio(resultado.getDouble("precio"));
                pastel.setPorciones(resultado.getInt("porciones"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pastel por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return pastel;
    }

    public Pastel crear(Pastel pastel) {
        String sql = "INSERT INTO pastel (nombre, sabor, precio, porciones) VALUES (?, ?, ?, ?) RETURNING id";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, pastel.getNombre());
            pstmt.setString(2, pastel.getSabor());
            pstmt.setDouble(3, pastel.getPrecio());
            pstmt.setInt(4, pastel.getPorciones());

            ResultSet resultado = pstmt.executeQuery();
            if (resultado.next()) {
                pastel.setId(resultado.getLong("id"));
                System.out.println("Pastel creado con ID: " + pastel.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error al crear pastel: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return pastel;
    }

    public Pastel actualizar(Long id, Pastel pastel) {
        String sql = "UPDATE pastel SET nombre = ?, sabor = ?, precio = ?, porciones = ? WHERE id = ?";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, pastel.getNombre());
            pstmt.setString(2, pastel.getSabor());
            pstmt.setDouble(3, pastel.getPrecio());
            pstmt.setInt(4, pastel.getPorciones());
            pstmt.setLong(5, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                pastel.setId(id);
                System.out.println("Pastel actualizado con ID: " + id);
                return pastel;
            } else {
                System.out.println("No se encontro pastel con ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar pastel: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean eliminar(Long id) {
        String sql = "DELETE FROM pastel WHERE id = ?";

        try {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setLong(1, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Pastel eliminado con ID: " + id);
                return true;
            } else {
                System.out.println("No se encontro pastel con ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar pastel: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexion cerrada correctamente");
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexion: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
