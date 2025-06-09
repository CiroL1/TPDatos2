package org.example;

import redis.clients.jedis.Jedis;
import com.datastax.oss.driver.api.core.CqlSession;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        testRedis();
        testCassandra();
        testNeo4j();
    }

    public static void testRedis() {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            jedis.set("saludo", "Hola desde Redis!");
            String valor = jedis.get("saludo");
            System.out.println("[Redis OK] Valor: " + valor);
        } catch (Exception e) {
            System.err.println("[Redis ERROR] " + e.getMessage());
        }
    }

    public static void testCassandra() {
        try (CqlSession session = CqlSession.builder()
                // Probá primero con "127.0.0.1"
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .build()) {
            System.out.println("[Cassandra OK] Conectado a Cassandra.");
        } catch (Exception e) {
            System.err.println("[Cassandra ERROR] " + e.getMessage());
        }
    }

    public static void testNeo4j() {
        try (Driver driver = GraphDatabase.driver(
                "bolt://localhost:7687",
                AuthTokens.basic("neo4j", "test1234"));  // ¡Actualiza la contraseña aquí!
             Session session = driver.session()) {
            String saludo = session.run("RETURN 'Hola desde Neo4j!' AS mensaje")
                    .single().get("mensaje").asString();
            System.out.println("[Neo4j OK] Mensaje: " + saludo);
        } catch (Exception e) {
            System.err.println("[Neo4j ERROR] " + e.getMessage());
        }
    }
}