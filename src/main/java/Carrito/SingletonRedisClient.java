package Carrito;

import redis.clients.jedis.Jedis;

public class SingletonRedisClient {
    private static Jedis instancia = null;

    private SingletonRedisClient() {}

    public static Jedis getInstance() {
        if (instancia == null) {
            synchronized (SingletonRedisClient.class) {
                if (instancia == null) {
                    instancia = new Jedis("localhost", 6379);
                }
            }
        }
        return instancia;
    }
}
