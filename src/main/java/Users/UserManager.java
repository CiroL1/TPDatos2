package Users;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import java.util.HashMap;
import java.util.Map;

public class UserManager implements CassandraManager<User> {
    private static final String KEYSPACE = "test";
    private static final String TABLE = "users";
    private CqlSession session;

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
                "dni int PRIMARY KEY, " +
                "name text, " +
                "address text, " +
                "session_time int, " +
                "user_type text, " +
                "password text)");
    }

    @Override
    public boolean insert(User u) {
        if (getOne(u.dni) != null) {
            return false;
        }
        String query = "INSERT INTO " + KEYSPACE + "." + TABLE +
                " (dni, name, address, session_time, user_type, password) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(u.dni, u.name, u.address, u.sessionTime, u.userType, u.password));
        return true;
    }

    @Override
    public Map<Integer, User> getAll() {
        Map<Integer, User> users = new HashMap<>();
        ResultSet rs = session.execute("SELECT * FROM " + KEYSPACE + "." + TABLE);
        for (Row row : rs) {
            User u = new User();
            u.dni = row.getInt("dni");
            u.name = row.getString("name");
            u.address = row.getString("address");
            u.sessionTime = row.getInt("session_time");
            u.userType = row.getString("user_type");
            u.password = row.getString("password");
            users.put(u.dni, u);
        }
        return users;
    }

    @Override
    public User getOne(int dni) {
        String query = "SELECT * FROM " + KEYSPACE + "." + TABLE + " WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        Row row = session.execute(prepared.bind(dni)).one();
        if (row != null) {
            User u = new User();
            u.dni = row.getInt("dni");
            u.name = row.getString("name");
            u.address = row.getString("address");
            u.sessionTime = row.getInt("session_time");
            u.userType = row.getString("user_type");
            u.password = row.getString("password");
            return u;
        } else {
            return null;
        }
    }

    @Override
    public void update(User u) {
        String query = "UPDATE " + KEYSPACE + "." + TABLE +
                " SET name = ?, address = ?, session_time = ?, user_type = ?, password = ? WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(u.name, u.address, u.sessionTime, u.userType, u.password, u.dni));
    }

    @Override
    public void delete(int dni) {
        String query = "DELETE FROM " + KEYSPACE + "." + TABLE + " WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(dni));
    }

    @Override
    public void close() {
        SingletonCassandra.closeSession();
    }

    public boolean checkPassword(int dni, String password) {
        String query = "SELECT password FROM " + KEYSPACE + "." + TABLE + " WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        Row row = session.execute(prepared.bind(dni)).one();
        return row != null && password.equals(row.getString("password"));
    }
}
