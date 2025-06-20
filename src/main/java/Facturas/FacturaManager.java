package Facturas;

import Pedidos.Pedido;
import Users.SessionManager;
import Users.User;
import Users.UserManager;
import Pagos.MetodoPago;

public class FacturaManager {

    private final UserManager userManager;

    public FacturaManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public Factura generarFactura(Pedido pedido, MetodoPago metodoPago, String empleadoFacturador) {
        long dni = SessionManager.getDniUsuarioActivo();
        User user = userManager.getOne((int) dni);

        if (user == null) {
            throw new IllegalStateException("Usuario no encontrado. No se puede generar factura.");
        }

        return new Factura(
                user.getName(),
                user.getAddress(),
                user.getCondicionIva() != null ? user.getCondicionIva():"No especificada",
                pedido,
                metodoPago,
                empleadoFacturador
        );
    }
}
