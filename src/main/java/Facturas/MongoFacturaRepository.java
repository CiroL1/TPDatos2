package Facturas;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import Pedidos.ItemPedido;
import Pedidos.Pedido;

import java.util.ArrayList;
import java.util.List;

public class MongoFacturaRepository {

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public MongoFacturaRepository() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("facturacion");
        collection = database.getCollection("facturas");
    }

    public void guardarFactura(Factura factura) {
        Document facturaDoc = new Document()
                .append("_id", new ObjectId())
                .append("numeroFactura", factura.getNumeroFactura())
                .append("nombreCliente", factura.getNombreCliente())
                .append("direccion", factura.getDireccion())
                .append("condicionIva", factura.getCondicionIva())
                .append("fechaHora", factura.getFechaHora())
                .append("empleadoFacturador", factura.getEmpleadoFacturador())
                .append("montoTotal", factura.getMontoTotal())
                .append("metodoPago", factura.getMetodoPago().getClass().getSimpleName())
                .append("impuestos", factura.getPedido().getImpuesto())
                .append("descuentos", factura.getPedido().getDescuento())
                .append("detallePedido", generarListaProductos(factura.getPedido()));

        collection.insertOne(facturaDoc);
    }

    private List<Document> generarListaProductos(Pedido pedido) {
        List<Document> productos = new ArrayList<>();
        for (ItemPedido item : pedido.getProductos()) {
            productos.add(new Document()
                    .append("codigo", item.getCodigo())
                    .append("nombre", item.getNombre())
                    .append("cantidad", item.getCantidad())
                    .append("precioUnitario", item.getPrecioUnitario())
                    .append("total", item.getCantidad() * item.getPrecioUnitario()));
        }
        return productos;
    }

    public void obtenerTodasLasFacturas() {
        FindIterable<Document> docs = collection.find();

        for (Document doc : docs) {
            System.out.println("Factura #" + doc.getInteger("numeroFactura"));
            System.out.println("Cliente: " + doc.getString("nombreCliente"));
            System.out.println("Dirección: " + doc.getString("direccion"));
            System.out.println("Condición IVA: " + doc.getString("condicionIva"));
            System.out.println("Fecha y Hora: " + doc.getString("fechaHora"));
            System.out.println("Método de pago: " + doc.getString("metodoPago"));
            System.out.println("Empleado que facturó: " + doc.getString("empleadoFacturador"));
            System.out.println("Monto Total: $" + doc.getDouble("montoTotal"));

            System.out.println("Detalle del pedido:");
            List<Document> detallePedido = (List<Document>) doc.get("detallePedido");
            System.out.println("Impuestos Aplicados: " + doc.getDouble(("impuestos")));
            System.out.println("Descuentos Aplicados: " + doc.getDouble(("descuentos")));
            for (Document item : detallePedido) {
                System.out.println("- " + item.getString("nombre") +
                        " x" + item.getInteger("cantidad") +
                        " @ $" + item.getDouble("precioUnitario") +
                        " = $" + item.getDouble("total"));
            }
            System.out.println("-----------------------------");
        }
    }

    public void borrarTodo(){
        collection.deleteMany(new Document());
    }

    public void cerrarConexion() {
        mongoClient.close();
    }
}
