package Pedidos;

import Carrito.CarritoManager;
import Catalogo.CatalogoManager;
import Catalogo.Producto;
import Users.SessionManager;
import Users.User;
import Users.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidoManager {
    private final UserManager userManager;
    private final CarritoManager carritoManager;
    private final CatalogoManager catalogoManager;
    private static int contadorPedido = 1;

    public PedidoManager(UserManager userManager, CarritoManager carritoManager, CatalogoManager catalogoManager) {
        this.userManager = userManager;
        this.carritoManager = carritoManager;
        this.catalogoManager = catalogoManager;
    }

    public Pedido generarPedido() {
        long dni = SessionManager.getDniUsuarioActivo();
        User user = userManager.getOne((int) dni);

        if (user == null) throw new IllegalStateException("Usuario no encontrado.");
        Map<String, String> carrito = carritoManager.obtenerCarrito(String.valueOf(user.getDni()));

        List<ItemPedido> productos = new ArrayList<>();
        double totalBruto = 0;

        for (Map.Entry<String, String> entry : carrito.entrySet()) {
            String codigo = entry.getKey();
            int cantidad = Integer.parseInt(entry.getValue());
            Producto prod = catalogoManager.getProductoPorCodigo(codigo);

            if (prod == null) continue;

            productos.add(new ItemPedido(codigo, prod.getNombre(), prod.getPrecio(), cantidad));
            totalBruto += prod.getPrecio() * cantidad;
        }

        double descuento = calcularDescuento(totalBruto);
        double impuesto = calcularImpuesto(totalBruto - descuento, user.getCondicionIva());
        double totalNeto = totalBruto - descuento + impuesto;
        carritoManager.eliminarCarrito();

        return new Pedido(
                contadorPedido++,
                user.getName(),
                user.getAddress(),
                user.getCondicionIva(),
                productos,
                totalBruto,
                descuento,
                impuesto,
                totalNeto
        );
    }

    private double calcularDescuento(double totalBruto) {
        return totalBruto > 10000 ? totalBruto * 0.05 : 0;
    }

    private double calcularImpuesto(double base, String condicionIva) {
        if (condicionIva == null) return 0;

        switch (condicionIva.toLowerCase()) {
            case "responsable inscripto":
            case "consumidor final":
                return base * 0.21;
            case "monotributista":
            case "exento":
            case "no responsable":
                return 0;
            default:
                return 0; // Por si hay errores o no se especifica bien
        }
    }
}
