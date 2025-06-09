package org.example.managers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import java.net.InetSocketAddress;

public class CassandraManager {
    private static final String KEYSPACE = "ejemplo";
    private static final String TABLE = "usuarios";
    private CqlSession session;

    public void conectar() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .build();
        crearSchema();
    }

    private void crearSchema() {
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE +
                " WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}");

        session.execute("CREATE TABLE IF NOT EXISTS " + KEYSPACE + "." + TABLE + " (" +
                "dni text PRIMARY KEY, " +
                "nombre text, " +
                "apellido text, " +
                "direccion text)");
    }

    public void insertarUsuario(String dni, String nombre, String apellido, String direccion) {
        String query = "INSERT INTO " + KEYSPACE + "." + TABLE + " (dni, nombre, apellido, direccion) VALUES (?, ?, ?, ?)";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(dni, nombre, apellido, direccion));
    }

    public void mostrarUsuarios() {
        ResultSet rs = session.execute("SELECT * FROM " + KEYSPACE + "." + TABLE);
        for (Row row : rs) {
            System.out.printf("DNI: %s, Nombre: %s, Apellido: %s, Dirección: %s%n",
                    row.getString("dni"), row.getString("nombre"), row.getString("apellido"), row.getString("direccion"));
        }
    }

    public void buscarPorDni(String dni) {
        String query = "SELECT * FROM " + KEYSPACE + "." + TABLE + " WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        Row row = session.execute(prepared.bind(dni)).one();
        if (row != null) {
            System.out.printf("DNI: %s, Nombre: %s, Apellido: %s, Dirección: %s%n",
                    row.getString("dni"), row.getString("nombre"), row.getString("apellido"), row.getString("direccion"));
        } else {
            System.out.println("No se encontró un usuario con ese DNI.");
        }
    }

    public void borrarPorDni(String dni) {
        String query = "DELETE FROM " + KEYSPACE + "." + TABLE + " WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(dni));
        System.out.println("Usuario eliminado si existía.");
    }

    public void actualizarPorDni(String dni, String nuevoNombre, String nuevoApellido, String nuevaDireccion) {
        String query = "UPDATE " + KEYSPACE + "." + TABLE + " SET nombre = ?, apellido = ?, direccion = ? WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(nuevoNombre, nuevoApellido, nuevaDireccion, dni));
        System.out.println("Usuario actualizado si existía.");
    }

    public void cerrar() {
        if (session != null) {
            session.close();
        }
    }
}