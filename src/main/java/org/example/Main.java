package org.example;

import Catalogo.*;
import Carrito.CarritoManager;
import Facturas.*;
import Pagos.*;
import Pedidos.*;
import Users.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // === 1. MANAGERS Y CONEXIONES ===
        UserManager userManager = new UserManager();
        userManager.connect();

        CatalogoManager catalogoManager = new CatalogoManager();
        CarritoManager carritoManager = new CarritoManager();
        TarjetaManager tarjetaManager = new TarjetaManager();
        tarjetaManager.connect();

        // === 2. USUARIO DE PRUEBA ===
        User newUser = new User();
        newUser.dni = 123456789;
        newUser.name = "John Doe";
        newUser.address = "123 Main St";
        newUser.sessionTime = 0;
        newUser.userType = "LOW";
        newUser.password = "password123";
        newUser.condicionIva = "Monotributista";

        if (userManager.getOne(newUser.dni) == null) {
            userManager.insert(newUser);
            System.out.println("User created.");
        } else {
            System.out.println("User already exists.");
        }

        SessionManager sessionManager = new SessionManager(userManager);
        sessionManager.login(newUser.dni, "password123");

        // === 3. TARJETA DE PRUEBA ===
        Tarjeta tarjeta = new Tarjeta(
                Tarjeta.Tipo.CREDITO,
                newUser.dni,
                LocalDate.of(2030, 1, 1),
                "999",
                "John Doe",
                30000.0
        );
        tarjeta.setNumeroTarjeta(999999);
        tarjetaManager.insert(tarjeta);

        // === 4. PRODUCTOS Y CATALOGO ===
        Producto producto1 = new Producto("PRODU1", "Zapatillas", 345.3, 8);
        Producto producto2 = new Producto("PRODU2", "Zapatillas", 129, 17);
        Producto producto4 = new Producto("PRODU4", "Billetera", 345.3, 8);

        catalogoManager.agregarProducto(producto1);
        catalogoManager.agregarProducto(producto2);
        catalogoManager.agregarProducto(producto4);

        // === 5. CARRITO ===
        carritoManager.crearCarrito(String.valueOf(newUser.dni));
        carritoManager.agregarProducto(producto1.getCodigo(), 2);
        carritoManager.agregarProducto(producto2.getCodigo(), 1);
        carritoManager.agregarProducto(producto4.getCodigo(), 3);

        // === 6. GENERAR PEDIDO ===
        PedidoManager pedidoManager = new PedidoManager(userManager, carritoManager, catalogoManager);
        Pedido pedido = pedidoManager.generarPedido();

        // === 7. PROCESAR MÉTODO DE PAGO ===
        PagoManager pagoManager = new PagoManager(tarjetaManager);
        MetodoPago metodoPago = MetodoPagoFactory.crearMetodoPago("tarjeta_credito", pagoManager, tarjeta.getNumeroTarjeta());
        boolean aprobado = metodoPago.procesarPago(pedido.valorNeto);

        if (!aprobado) {
            System.out.println("El pago no pudo ser procesado (saldo insuficiente).");
            return;
        }

        // === 8. GENERAR FACTURA ===
        FacturaManager facturaManager = new FacturaManager(userManager);
        Factura factura = facturaManager.generarFactura(pedido, metodoPago, "Empleado Test");

        // === 9. MOSTRAR FACTURA ===
        System.out.println("\n=== FACTURA GENERADA ===");
        System.out.println(factura);

        // === 10. GUARDAR FACTURA EN MONGODB ===
        MongoFacturaRepository repo = new MongoFacturaRepository();
        repo.guardarFactura(factura);
        repo.cerrarConexion();
        System.out.println("Factura guardada correctamente en MongoDB.");

        // === 11. CIERRE ===
        sessionManager.logout();
        userManager.close();
        tarjetaManager.close();
    }
}
