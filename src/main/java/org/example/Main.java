package org.example;
import Catalogo.*;
import Users.SessionManager;
import Users.User;
import Users.UserManager;
import Carrito.CarritoManager;

public class Main {
    public static void main(String[] args) {
        // Step 1: Connect user manager and create table
        UserManager userManager = new UserManager();
        userManager.connect();

        // Step 2: Create and insert a new user
        User newUser = new User();
        newUser.dni = 123456789;
        newUser.name = "John Doe";
        newUser.address = "123 Main St";
        newUser.sessionTime = 0;
        newUser.userType = "LOW";
        newUser.password = "password123";

        // Insert user into the DB (if not already there)
        if (userManager.getOne(newUser.dni) == null) {
            userManager.insert(newUser);
            System.out.println("User created.");
        } else {
            System.out.println("User already exists.");
        }

        // Step 3: Manage session
        SessionManager sessionManager = new SessionManager(userManager);

        boolean loggedIn = sessionManager.login(newUser.dni, "password123");



        /*---------------------------------------- PRUEBAS MANEJO CATALOGO ----------------------------------------*/

        CatalogoManager manager = new CatalogoManager();

        /*GENERO PRODUCTOS NUEVOS (hice que el constructor solo tuviera que recibir los
        valores mas basicos - asi era mas rapido de manejar)*/
        Producto producto=new Producto("PRODU1","Zapatillas",345.3,8);
        Producto producto2=new Producto("PRODU2","Zapatillas",129,17);
        Producto producto3=new Producto("PRODU3","Patines",789,78);
        Producto producto4=new Producto("PRODU4","Billetera",345.3,8);

        /*agrego un par de valores iniciales mas al producto4*/
        producto4.agregarComentario("Muy bonita");
        producto4.agregarComentario("Me encanta el color");
        producto4.agregarAtributo("Disponible en color rojo, azul y marron");


        /*AGREGO LOS PRODUCTOS AL CATALOGO*/
        manager.agregarProducto(producto);
        manager.agregarProducto(producto2);
        manager.agregarProducto(producto3);
        manager.agregarProducto(producto4);
        System.out.println();

        /*BUSCO TODOS LOS PRODUCTOS QUE TENGAN EL NOMBRE "Billetera"*/
        System.out.println(manager.buscarProductosCompatibles("Billetera"));
        System.out.println();

        /* LISTA DE PRECIOS*/
        System.out.println(manager.getPrecios());

        /*ACTUALIZO EL PRECIO DE UNO DE LOS PRODUCTOS*/
        manager.actualizarPrecio(producto3,567);
        System.out.println(manager.buscarProductosCompatibles("Patines"));

        /*ELIMINO UN PRODUCTO*/
        manager.eliminarProducto(producto3);
        System.out.println(manager.buscarProductosCompatibles("Patines"));

        /*ACTUALIZO UN PAR DE VALORES*/
        manager.actualizarImagenes(producto2,"inserte imagen idk");
        manager.actualizarImagenes(producto2,"Otra imagen mas");

        /*OBTENGO EL HISTORIAL DE CAMBIOS DEL CATALOGO*/
        System.out.println("\n");
        System.out.println(manager.getCambios());


        /*CIERRO LA CONEXION CON LA BASE DE DATOS
         (ESTO SE TIENE QUE HACER SI O SI - (si no la
         cierro gasta recursos al pedo))*/
        manager.cerrarConexion();


        /*---------------------------------------- PRUEBAS MANEJO CARRITO ----------------------------------------*/
        CarritoManager carritoManager = new CarritoManager();

        // Crear carrito para el usuario logueado
        carritoManager.crearCarrito(String.valueOf(newUser.dni));

        // Agregar productos al carrito
        System.out.println("--- AGREGANDO PRODUCTOS AL CARRITO ---");
        carritoManager.agregarProducto(producto.getCodigo(), 2);   // 2 zapatillas (PRODU1)
        carritoManager.agregarProducto(producto2.getCodigo(), 1);  // 1 zapatilla (PRODU2)
        carritoManager.agregarProducto(producto4.getCodigo(), 3);  // 3 billeteras (PRODU4)

        // Mostrar carrito actual
        System.out.println("Carrito actual:");
        System.out.println(carritoManager.obtenerCarrito());

        // Modificar cantidad de un producto
        System.out.println("\n--- MODIFICANDO CANTIDAD ---");
        carritoManager.modificarCantidad(producto.getCodigo(), 5); // cambio a 5 zapatillas (PRODU1)
        System.out.println("Carrito actualizado:");
        System.out.println(carritoManager.obtenerCarrito());

        // Eliminar un producto
        System.out.println("\n--- ELIMINANDO PRODUCTO ---");
        carritoManager.eliminarProducto(producto2.getCodigo()); // elimino zapatilla (PRODU2)
        System.out.println("Carrito actualizado:");
        System.out.println(carritoManager.obtenerCarrito());

        // Eliminar el carrito completo
        System.out.println("\n--- ELIMINANDO TODO EL CARRITO ---");
        carritoManager.eliminarCarrito();
        System.out.println("Carrito tras eliminación:");
        System.out.println(carritoManager.obtenerCarrito());


        // Cierre de sesión y conexión usuario
        sessionManager.logout();
        userManager.close();
    }
}
