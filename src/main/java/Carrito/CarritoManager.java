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
    public String crearClave(String dni){
      return "carrito:" + dni;
    }

    @Override
    public void crearCarrito(String dni) {
        if (!redis.exists(crearClave(dni))) {
            redis.hset(crearClave(dni), "init", "1");
            redis.hdel(crearClave(dni), "init");
        }
    }
    private void guardarEstadoAnterior() {
        String dni = getDniActivo();
        String carritoKey = crearClave(dni);
        String prevKey = carritoKey + ":prev";

        Map<String, String> carritoActual = redis.hgetAll(carritoKey);
        redis.del(prevKey);
        if (!carritoActual.isEmpty()) {
            redis.hset(prevKey, carritoActual);
        }
    }
    @Override
    public void agregarProducto(String codigoProducto, int cantidad) {
        guardarEstadoAnterior();
        String dni = getDniActivo();
        redis.hset(crearClave(dni), codigoProducto, String.valueOf(cantidad));
    }
    @Override
    public void eliminarProducto(String codigoProducto) {
        guardarEstadoAnterior();
        String dni = getDniActivo();
        redis.hdel(crearClave(dni), codigoProducto);
    }
    @Override
    public void modificarCantidad(String codigoProducto, int nuevaCantidad) {
        guardarEstadoAnterior();
        String dni = getDniActivo();
        if (nuevaCantidad <= 0) {
            eliminarProducto(codigoProducto);
        } else {
            redis.hset(crearClave(dni), codigoProducto, String.valueOf(nuevaCantidad));
        }
    }
    @Override
    public Map<String, String> obtenerCarrito(String dni) {
        return redis.hgetAll(crearClave(dni));
    }
    @Override
    public void eliminarCarrito() {
        redis.del(getDniActivo());
    }
    public void guardarCarrito() {
        String dni = getDniActivo();
        String carritoKey = crearClave(dni);
        String backupKey = carritoKey + ":backup";

        Map<String, String> carritoActual = redis.hgetAll(carritoKey);
        redis.del(backupKey);
        if (!carritoActual.isEmpty()) {
            redis.hset(backupKey, carritoActual);
        }
    }
    public void recuperarCarrito() {
        String dni = getDniActivo();
        String carritoKey = crearClave(dni);
        String backupKey = carritoKey + ":backup";

        if (redis.exists(backupKey)) {
            redis.del(carritoKey);
            redis.hset(carritoKey, redis.hgetAll(backupKey));
        } else {
            throw new IllegalStateException("No hay carrito guardado previamente.");
        }
    }
    public void estadoAnterior() {
        String dni = getDniActivo();
        String carritoKey = crearClave(dni);
        String prevKey = carritoKey + ":prev";

        if (redis.exists(prevKey)) {
            redis.del(carritoKey);
            redis.hset(carritoKey, redis.hgetAll(prevKey));
        } else {
            throw new IllegalStateException("No hay estado anterior disponible.");
        }
    }
}
