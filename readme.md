USO:
Para levantar las bases de datos es necesario tener instalado Docker: https://docs.docker.com/desktop/setup/install/windows-install/
luego en la terminal de java ejecuta el comando docker-compose up -d o docker compose up -d


## Justificación de Tecnologías Utilizadas

En este proyecto se utilizaron diversas tecnologías de bases de datos, cada una seleccionada en función de su propósito específico dentro del sistema:

### 🗃️ Cassandra
Se utilizó para almacenar la base de datos de usuarios y los métodos de pago (como tarjetas) asociados.  
**Motivos de elección:**
- Modelo basado en familias de columnas.
- Alta eficiencia y escalabilidad para gestionar grandes volúmenes de datos.

### 📦 MongoDB
Se empleó tanto para la base de datos del catálogo de productos como para el almacenamiento de facturas generadas por pedidos.  
**Motivos de elección:**
- Modelo orientado a documentos, ideal para representar información compleja (como imágenes o videos demostrativos).
- Facilidad para almacenar y manipular datos en formato JSON.
- Flexibilidad en la estructura de datos, lo que simplifica el almacenamiento de facturas y productos.

### ⚡ Redis
Se usó para manejar los carritos de compra de los usuarios activos durante la sesión.  
**Motivos de elección:**
- Base de datos en memoria de tipo clave-valor, con alta velocidad de lectura/escritura.
- Ideal para simular el comportamiento en tiempo real de un carrito en un entorno de E-Commerce.
- Sistema de persistencia mediante backups de claves, lo que permite recuperar el estado previo del carrito cuando el usuario retoma su sesión.

---

Estas tecnologías fueron seleccionadas con el objetivo de construir un sistema robusto, eficiente y realista, simulando funcionalidades propias de una plataforma de E-Commerce moderna.
