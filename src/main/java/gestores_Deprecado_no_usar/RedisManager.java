package gestores_Deprecado_no_usar;

import redis.clients.jedis.Jedis;

public class RedisManager {
    private final Jedis jedis;

    public RedisManager(String host, int port) {
        jedis = new Jedis(host, port);
    }

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public void close() {
        if (jedis != null) jedis.close();
    }
}
