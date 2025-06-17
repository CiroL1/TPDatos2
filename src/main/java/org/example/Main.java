//package org.example;
//import Catalogo.*;
//import Users.User;
//import Users.UserManager;
//
//public class Main {
//    public static void main(String[] args) {
//        // Step 1: Connect user manager and create table
//        UserManager userManager = new UserManager();
//        userManager.connect();
//
//        // Step 2: Create and insert a new user
//        User newUser = new User();
//        newUser.dni = 12345678;
//        newUser.name = "John Doe";
//        newUser.address = "123 Main St";
//        newUser.sessionTime = 0;
//        newUser.userType = "LOW";
//        newUser.password = "password123";
//
//        // Insert user into the DB (if not already there)
//        if (userManager.getUserByDni(newUser.dni) == null) {
//            userManager.insertUser(newUser);
//            System.out.println("User created.");
//        } else {
//            System.out.println("User already exists.");
//        }
//
//        // Step 3: Manage session
//        UserSessionManager sessionManager = new UserSessionManager(userManager);
//
//        boolean loggedIn = sessionManager.login(newUser.dni, "password123");
//        if (loggedIn) {
//            try {
//                // Simulate activity for ~5 minutes
//                System.out.println("Simulating session for 1 minute...");
//                Thread.sleep(1 * 60 * 1000); // 1 minutes in milliseconds
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            // Step 4: Logout
//            sessionManager.logout();
//        }
//
//        // Step 5: Close connection
//        userManager.close();
//
//
//
//        /*---------------------------------------- PRUEBAS MANEJO CATALOGO ----------------------------------------*/
//
//        CatalogoManager manager = new CatalogoManager();
//
//        /*GENERO PRODUCTOS NUEVOS (hice que el constructor solo tuviera que recibir los
//        valores mas basicos - asi era mas rapido de manejar)*/
//        Producto producto=new Producto("PRODU1","Zapatillas",345.3,8);
//        Producto producto2=new Producto("PRODU2","Zapatillas",129,17);
//        Producto producto3=new Producto("PRODU3","Patines",789,78);
//        Producto producto4=new Producto("PRODU4","Billetera",345.3,8);
//
//        /*agrego un par de valores iniciales mas al producto4*/
//        producto4.agregarComentario("Muy bonita");
//        producto4.agregarComentario("Me encanta el color");
//        producto4.agregarAtributo("Disponible en color rojo, azul y marron");
//
//
//        /*AGREGO LOS PRODUCTOS AL CATALOGO*/
//        manager.agregarProducto(producto);
//        manager.agregarProducto(producto2);
//        manager.agregarProducto(producto3);
//        manager.agregarProducto(producto4);
//        System.out.println();
//
//        /*BUSCO TODOS LOS PRODUCTOS QUE TENGAN EL NOMBRE "Billetera"*/
//        System.out.println(manager.buscarProductosCompatibles("Billetera"));
//        System.out.println();
//
//        /* LISTA DE PRECIOS*/
//        System.out.println(manager.getPrecios());
//
//        /*ACTUALIZO EL PRECIO DE UNO DE LOS PRODUCTOS*/
//        manager.actualizarPrecio(producto3,567);
//        System.out.println(manager.buscarProductosCompatibles("Patines"));
//
//        /*ELIMINO UN PRODUCTO*/
//        manager.eliminarProducto(producto3);
//        System.out.println(manager.buscarProductosCompatibles("Patines"));
//
//        /*ACTUALIZO UN PAR DE VALORES*/
//        manager.actualizarImagenes(producto2,"inserte imagen idk");
//        manager.actualizarImagenes(producto2,"Otra imagen mas");
//
//        /*OBTENGO EL HISTORIAL DE CAMBIOS DEL CATALOGO*/
//        System.out.println("\n");
//        System.out.println(manager.getCambios());
//
//
//        /*CIERRO LA CONEXION CON LA BASE DE DATOS
//         (ESTO SE TIENE QUE HACER SI O SI - (si no la
//         cierro gasta recursos al pedo))*/
//        manager.cerrarConexion();
//
//    }
//
//}


package org.example;
import Users.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        userManager.connect();

        // Crear usuario de prueba
        User testUser = new User();
        testUser.dni = 12345678;
        testUser.name = "Lucas Insaurralde";
        testUser.address = "Calle Falsa 123";
        testUser.sessionTime = 0;
        testUser.userType = "LOW";
        testUser.password = "secret123";

        // Insertar usuario (si no existe)
        User existing = userManager.getOne(testUser.dni);
        if (existing == null) {
            userManager.insert(testUser);
            System.out.println("Usuario insertado.");
        } else {
            System.out.println("Usuario ya existe.");
        }

        // Crear SessionManager y probar login/logout
        SessionManager sessionManager = new SessionManager(userManager);

        // Login
        boolean loggedIn = sessionManager.login(testUser.dni, "secret123");
        if (!loggedIn) {
            System.out.println("Error en login");
            userManager.close();
            return;
        }

        // Simular sesión activa (esperar 3 segundos)
        try {
            System.out.println("Sesión activa... (esperando 3 segundos)");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Logout
        sessionManager.logout();

        // Cerrar conexión a Cassandra del UserManager
        userManager.close();


        // --- Ahora probamos TarjetaManager ---

        TarjetaManager tarjetaManager = new TarjetaManager();
        tarjetaManager.connect();

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(98765432);
        tarjeta.setTipo(Tarjeta.Tipo.CREDITO);
        tarjeta.setDni(testUser.dni);  // asociamos la tarjeta al usuario creado
        tarjeta.setFechaVencimiento(LocalDate.of(2027, 12, 31));
        tarjeta.setNumeroSeguridad("321");
        tarjeta.setNombreDueño(testUser.name);
        tarjeta.setSaldo(10000.0);

        // Insertar tarjeta (solo si no existe)
        Tarjeta tarjetaExistente = tarjetaManager.getOne(tarjeta.getNumeroTarjeta());
        if (tarjetaExistente == null) {
            tarjetaManager.insert(tarjeta);
            System.out.println("Tarjeta insertada.");
        } else {
            System.out.println("Tarjeta ya existe.");
        }

        // Obtener y mostrar la tarjeta
        Tarjeta tarjetaEncontrada = tarjetaManager.getOne(tarjeta.getNumeroTarjeta());
        System.out.println("Tarjeta encontrada:");
        System.out.println(tarjetaEncontrada);

        tarjetaManager.close();

        System.out.println("Fin del programa.");
    }
}

