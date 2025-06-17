package Users;

import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public class SingletonCassandra {
    private static CqlSession session;

    private SingletonCassandra() {}

    public static CqlSession getSession() {
        if (session == null) {
            session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                    .withLocalDatacenter("datacenter1")
                    .build();
        }
        return session;
    }

    public static void closeSession() {
        if (session != null) {
            session.close();
            session = null;
        }
    }
}
