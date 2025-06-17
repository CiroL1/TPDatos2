package Carrito;

import java.util.Map;

public interface RedisManager {
    void crearCarrito(String dni);
    void agregarProducto(String codigoProducto, int cantidad);
    void eliminarProducto(String codigoProducto);
    void modificarCantidad(String codigoProducto, int nuevaCantidad);
    Map<String, String> obtenerCarrito();
    void eliminarCarrito();
}
