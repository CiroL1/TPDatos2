package gestores;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;

public class Neo4jManager implements AutoCloseable {
    private final Driver driver;

    public Neo4jManager(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public String runSingleResultQuery(String cypher, String resultKey) {
        try (Session session = driver.session()) {
            Result result = session.run(cypher);
            if (result.hasNext()) {
                return result.single().get(resultKey).asString();
            } else {
                return null;
            }
        }
    }

    @Override
    public void close() {
        if (driver != null) driver.close();
    }
}
