package Users;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TarjetaManager implements CassandraManager<Tarjeta> {

    private static final String KEYSPACE = "test";
    private static final String TABLE = "tarjetas";
    private CqlSession session;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void connect() {
        session = SingletonCassandra.getSession();
        createTable();
    }

    @Override
    public void createTable() {
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE +
                " WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}");

        session.execute("CREATE TABLE IF NOT EXISTS " + KEYSPACE + "." + TABLE + " (" +
                "numero_tarjeta int PRIMARY KEY, " +      // asumimos int para simplificar
                "tipo text, " +
                "dni int, " +
                "fecha_vencimiento text, " +             // guardamos como string yyyy-MM-dd
                "numero_seguridad text, " +
                "nombre_dueno text, " +
                "saldo double)");
    }

    @Override
    public boolean insert(Tarjeta t) {
        // Comprobar si ya existe la tarjeta por numero_tarjeta
        if (getOne(t.getNumeroTarjeta()) != null) {
            return false; // ya existe
        }

        String query = "INSERT INTO " + KEYSPACE + "." + TABLE +
                " (numero_tarjeta, tipo, dni, fecha_vencimiento, numero_seguridad, nombre_dueno, saldo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(
                t.getNumeroTarjeta(),
                t.getTipo().name(),
                t.getDni(),
                t.getFechaVencimiento().format(formatter),
                t.getNumeroSeguridad(),
                t.getNombreDueño(),
                t.getSaldo()
        ));
        return true;
    }

    @Override
    public Map<Integer, Tarjeta> getAll() {
        Map<Integer, Tarjeta> tarjetas = new HashMap<>();
        ResultSet rs = session.execute("SELECT * FROM " + KEYSPACE + "." + TABLE);
        for (Row row : rs) {
            Tarjeta t = mapRowToTarjeta(row);
            tarjetas.put(t.getNumeroTarjeta(), t);
        }
        return tarjetas;
    }

    @Override
    public Tarjeta getOne(int numeroTarjeta) {
        String query = "SELECT * FROM " + KEYSPACE + "." + TABLE + " WHERE numero_tarjeta = ?";
        PreparedStatement prepared = session.prepare(query);
        Row row = session.execute(prepared.bind(numeroTarjeta)).one();
        if (row != null) {
            return mapRowToTarjeta(row);
        } else {
            return null;
        }
    }

    @Override
    public void update(Tarjeta t) {
        String query = "UPDATE " + KEYSPACE + "." + TABLE + " SET " +
                "tipo = ?, dni = ?, fecha_vencimiento = ?, numero_seguridad = ?, nombre_dueno = ?, saldo = ? " +
                "WHERE numero_tarjeta = ?";

        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(
                t.getTipo().name(),
                t.getDni(),
                t.getFechaVencimiento().format(formatter),
                t.getNumeroSeguridad(),
                t.getNombreDueño(),
                t.getSaldo(),
                t.getNumeroTarjeta()
        ));
    }

    @Override
    public void delete(int numeroTarjeta) {
        String query = "DELETE FROM " + KEYSPACE + "." + TABLE + " WHERE numero_tarjeta = ?";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(numeroTarjeta));
    }

    @Override
    public void close() {
        SingletonCassandra.closeSession();
    }

    // Método auxiliar para mapear fila a objeto Tarjeta
    private Tarjeta mapRowToTarjeta(Row row) {
        int numeroTarjeta = row.getInt("numero_tarjeta");
        String tipoStr = row.getString("tipo");
        Tarjeta.Tipo tipo = Tarjeta.Tipo.valueOf(tipoStr);
        int dni = row.getInt("dni");
        String fechaStr = row.getString("fecha_vencimiento");
        LocalDate fechaVencimiento = LocalDate.parse(fechaStr, formatter);
        String numeroSeguridad = row.getString("numero_seguridad");
        String nombreDueño = row.getString("nombre_dueno");
        double saldo = row.getDouble("saldo");

        return new Tarjeta(tipo, dni, fechaVencimiento, numeroSeguridad, nombreDueño, saldo) {
            @Override
            public int getNumeroTarjeta() {
                return numeroTarjeta;
            }
        };
    }
}
