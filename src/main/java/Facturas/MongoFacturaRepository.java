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
                .append("numeroFactura", factura.numeroFactura)
                .append("nombreCliente", factura.nombreCliente)
                .append("direccion", factura.direccion)
                .append("condicionIva", factura.condicionIva)
                .append("fechaHora", factura.fechaHora)
                .append("empleadoFacturador", factura.empleadoFacturador)
                .append("montoTotal", factura.montoTotal)
                .append("metodoPago", factura.metodoPago.getClass().getSimpleName())
                .append("detallePedido", generarListaProductos(factura.pedido));

        collection.insertOne(facturaDoc);
    }

    private List<Document> generarListaProductos(Pedido pedido) {
        List<Document> productos = new ArrayList<>();
        for (ItemPedido item : pedido.productos) {
            productos.add(new Document()
                    .append("codigo", item.codigo)
                    .append("nombre", item.nombre)
                    .append("cantidad", item.cantidad)
                    .append("precioUnitario", item.precioUnitario)
                    .append("total", item.cantidad * item.precioUnitario));
        }
        return productos;
    }

    public void cerrarConexion() {
        mongoClient.close();
    }
}
