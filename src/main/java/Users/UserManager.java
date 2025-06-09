package Users;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String KEYSPACE = "test";
    private static final String TABLE = "users";
    private CqlSession session;

    public void connect() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .build();
        createTable();
    }

    private void createTable() {
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

    public void insertUser(User u) {
        String query = "INSERT INTO " + KEYSPACE + "." + TABLE +
                " (dni, name, address, session_time, user_type, password) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(u.dni, u.name, u.address, u.sessionTime, u.userType, u.password));
    }

    public Map<Integer, User> getAllUsers() {
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

    public User getUserByDni(int dni) {
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

    public void updateUser(User u) {
        String query = "UPDATE " + KEYSPACE + "." + TABLE +
                " SET name = ?, address = ?, session_time = ?, user_type = ?, password = ? WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(u.name, u.address, u.sessionTime, u.userType, u.password, u.dni));
        System.out.println("User updated.");
    }

    public void deleteUserByDni(int dni) {
        String query = "DELETE FROM " + KEYSPACE + "." + TABLE + " WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        session.execute(prepared.bind(dni));
        System.out.println("User deleted if existed.");
    }

    public boolean checkPassword(int dni, String password) {
        String query = "SELECT password FROM " + KEYSPACE + "." + TABLE + " WHERE dni = ?";
        PreparedStatement prepared = session.prepare(query);
        Row row = session.execute(prepared.bind(dni)).one();
        if (row != null) {
            String storedPassword = row.getString("password");
            return storedPassword != null && storedPassword.equals(password);
        }
        return false;
    }


    public void close() {
        if (session != null) {
            session.close();
        }
    }
}
