package Carrito;

import redis.clients.jedis.Jedis;
import Users.SessionManager;

import java.util.Map;

public class CarritoManager implements RedisManager {
    private final Jedis redis;

    public CarritoManager() {
        this.redis = SingletonRedisClient.getInstance();
    }

    private String getDniActivo() {
        String dni = String.valueOf(SessionManager.getDniUsuarioActivo());
        if (dni.equals("-1")) throw new IllegalStateException("No hay usuario activo en la sesión.");
        return dni;
    }

    @Override
    public void crearCarrito(String dni) {
        if (!redis.exists(dni)) {
            redis.hset(dni, "init", "1");
            redis.hdel(dni, "init");
        }
    }

    @Override
    public void agregarProducto(String codigoProducto, int cantidad) {
        String dni = getDniActivo();
        redis.hincrBy(dni, codigoProducto, cantidad);
    }

    @Override
    public void eliminarProducto(String codigoProducto) {
        String dni = getDniActivo();
        redis.hdel(dni, codigoProducto);
    }

    @Override
    public void modificarCantidad(String codigoProducto, int nuevaCantidad) {
        String dni = getDniActivo();
        if (nuevaCantidad <= 0) {
            eliminarProducto(codigoProducto);
        } else {
            redis.hset(dni, codigoProducto, String.valueOf(nuevaCantidad));
        }
    }

    @Override
    public Map<String, String> obtenerCarrito() {
        return redis.hgetAll(getDniActivo());
    }

    @Override
    public void eliminarCarrito() {
        redis.del(getDniActivo());
    }
}
