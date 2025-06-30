package org.example;

import Catalogo.*;
import Carrito.CarritoManager;
import Facturas.*;
import Pagos.*;
import Pedidos.*;
import Users.*;

import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IOException {
        // === 1. MANAGERS Y CONEXIONES ===
        UserManager userManager = new UserManager();
        userManager.connect();

        CatalogoManager catalogoManager = new CatalogoManager();
        CarritoManager carritoManager = new CarritoManager();
        TarjetaManager tarjetaManager = new TarjetaManager();
        tarjetaManager.connect();

        SessionManager sessionManager = new SessionManager(userManager);

        // === 2. PRIMER USUARIO ===
        User user1 = new User();
        user1.setDni(123456789);
        user1.setName("Carlos Rodriguez");
        user1.setAddress("123 Main St");
        user1.setSessionTime(12000);
        user1.setUserType("LOW");
        user1.setPassword("password123");
        user1.setCondicionIva("Monotributista");

        if (userManager.getOne(user1.getDni()) == null) {
            userManager.insert(user1);
            System.out.println("User 1 created.");
        } else {
            System.out.println("User 1 already exists.");
        }

        sessionManager.login(user1.getDni(), "password123");
        System.in.read();
        int numeroTarjetaUser1 = 999999;
        if (tarjetaManager.getOne(numeroTarjetaUser1) == null) {
            Tarjeta tarjeta1 = new Tarjeta(
                    Tarjeta.Tipo.CREDITO,
                    user1.getDni(),
                    LocalDate.of(2030, 1, 1),
                    "999",
                    user1.getName(),
                    30000.0
            );
            tarjeta1.setNumeroTarjeta(numeroTarjetaUser1);
            tarjetaManager.insert(tarjeta1);
            System.out.println("Tarjeta User 1 creada.");
        } else {
            System.out.println("Tarjeta User 1 ya existe.");
        }
        System.in.read();
        // === 3. INSERTAR PRODUCTOS SI NO EXISTEN ===
        Producto producto1 = new Producto("PRODU1", "Zapatillas", 345.3, 8);
        Producto producto2 = new Producto("PRODU2", "Zapatillas", 129, 17);
        Producto producto4 = new Producto("PRODU4", "Billetera", 345.3, 8);

        if (catalogoManager.getProductoPorCodigo(producto1.getCodigo()) == null)
            catalogoManager.agregarProducto(producto1);
        if (catalogoManager.getProductoPorCodigo(producto2.getCodigo()) == null)
            catalogoManager.agregarProducto(producto2);
        if (catalogoManager.getProductoPorCodigo(producto4.getCodigo()) == null)
            catalogoManager.agregarProducto(producto4);

        System.out.println("Productos listos en catálogo.");
        System.in.read();
        // === 4. CICLO 1 CON USUARIO 1 ===
        System.out.println("\n--- Ciclo 1: Usuario 1, creando carrito y agregando productos ---");
        carritoManager.crearCarrito(String.valueOf(user1.getDni()));
        carritoManager.agregarProducto(producto1.getCodigo(), 2);
        carritoManager.agregarProducto(producto2.getCodigo(), 1);
        carritoManager.agregarProducto(producto4.getCodigo(), 3);

        System.out.println("Carrito actual: " + carritoManager.obtenerCarrito(String.valueOf(user1.getDni())));
        System.in.read();

        System.out.println("Modificando carrito: quitamos 1 unidad de PRODU4");
        carritoManager.modificarCantidad(producto4.getCodigo(), 2);
        System.out.println("Carrito modificado: " + carritoManager.obtenerCarrito(String.valueOf(user1.getDni())));

        System.out.println("Rollback al estado anterior del carrito");
        carritoManager.estadoAnterior();
        System.out.println("Carrito tras rollback: " + carritoManager.obtenerCarrito(String.valueOf(user1.getDni())));
        System.in.read();
        PedidoManager pedidoManager = new PedidoManager(userManager, carritoManager, catalogoManager);
        Pedido pedido1 = pedidoManager.generarPedido();
        System.out.println("Pedido 1 generado con total neto: " + pedido1.getValorNeto());

        PagoManager pagoManager = new PagoManager(tarjetaManager);
        MetodoPago metodoPago1 = MetodoPagoFactory.crearMetodoPago("tarjeta_credito", pagoManager, numeroTarjetaUser1);
        boolean aprobado1 = metodoPago1.procesarPago(pedido1.getValorNeto());

        if (!aprobado1) {
            System.out.println("El pago 1 no pudo ser procesado (saldo insuficiente).");
        } else {
            System.out.println("Pago 1 aprobado.");
        }
        System.in.read();
        FacturaManager facturaManager = new FacturaManager(userManager);
        if (aprobado1) {
            Factura factura1 = facturaManager.generarFactura(pedido1, metodoPago1, "Empleado Test");
            System.out.println("Factura 1 generada:\n" + factura1);

            MongoFacturaRepository repo = new MongoFacturaRepository();
            repo.guardarFactura(factura1);
            repo.cerrarConexion();
            System.out.println("Factura 1 guardada correctamente en MongoDB.");
        }
        System.in.read();
        // === 5. LOGOUT USUARIO 1 ===
        sessionManager.logout();

        // === 6. SEGUNDO USUARIO ===
        User user2 = new User();
        user2.setDni(987654321);
        user2.setName("Ana Rodriguez");
        user2.setAddress("456 Second St");
        user2.setSessionTime(15000);
        user2.setUserType("HIGH");
        user2.setPassword("password456");
        user2.setCondicionIva("Responsable Inscripto");

        if (userManager.getOne(user2.getDni()) == null) {
            userManager.insert(user2);
            System.out.println("\nUser 2 created.");
        } else {
            System.out.println("\nUser 2 already exists.");
        }

        sessionManager.login(user2.getDni(), "password456");

        int numeroTarjetaUser2 = 888888;
        if (tarjetaManager.getOne(numeroTarjetaUser2) == null) {
            Tarjeta tarjeta2 = new Tarjeta(
                    Tarjeta.Tipo.DEBITO,
                    user2.getDni(),
                    LocalDate.of(2028, 6, 30),
                    "123",
                    user2.getName(),
                    15000.0
            );
            tarjeta2.setNumeroTarjeta(numeroTarjetaUser2);
            tarjetaManager.insert(tarjeta2);
            System.out.println("Tarjeta User 2 creada.");
        } else {
            System.out.println("Tarjeta User 2 ya existe.");
        }

        // === 7. CICLO 2 CON USUARIO 2 ===
        System.out.println("\n--- Ciclo 2: Usuario 2, nuevo carrito y compra ---");
        carritoManager.eliminarCarrito();
        carritoManager.crearCarrito(String.valueOf(user2.getDni()));

        carritoManager.agregarProducto(producto1.getCodigo(), 1);
        carritoManager.agregarProducto(producto4.getCodigo(), 1);
        System.out.println("Carrito 2 actual: " + carritoManager.obtenerCarrito(String.valueOf(user2.getDni())));

        Pedido pedido2 = pedidoManager.generarPedido();
        System.out.println("Pedido 2 generado con total neto: " + pedido2.getValorNeto());

        MetodoPago metodoPago2 = MetodoPagoFactory.crearMetodoPago("tarjeta_credito", pagoManager, numeroTarjetaUser2);
        boolean aprobado2 = metodoPago2.procesarPago(pedido2.getValorNeto());

        if (!aprobado2) {
            System.out.println("El pago 2 no pudo ser procesado (saldo insuficiente).");
        } else {
            System.out.println("Pago 2 aprobado.");
        }

        if (aprobado2) {
            Factura factura2 = facturaManager.generarFactura(pedido2, metodoPago2, "Empleado Test");
            System.out.println("Factura 2 generada:\n" + factura2);

            MongoFacturaRepository repo = new MongoFacturaRepository();
            repo.guardarFactura(factura2);
            repo.cerrarConexion();
            System.out.println("Factura 2 guardada correctamente en MongoDB.");
        }

        // === 8. CIERRE ===
        sessionManager.logout();
        userManager.close();
        tarjetaManager.close();

        System.out.println("\n=== Fin del proceso de prueba con dos usuarios distintos ===");

        MongoFacturaRepository repo = new MongoFacturaRepository();
        repo.obtenerTodasLasFacturas();
        repo.cerrarConexion();
    }
}
