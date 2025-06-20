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
        mongoClient = MongoClients.create("mongodb://localhost:27017"); // Cambiar si tu URI es distinta
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

    public void cerrarConexion() {
        mongoClient.close();
    }
}
