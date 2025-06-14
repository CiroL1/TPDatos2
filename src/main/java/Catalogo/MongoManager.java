package Catalogo;

import com.mongodb.client.*;
import org.bson.Document;

//Otorga una conexion a MongoDB para manejar lo que seria el catalogo de productos
public class MongoManager{
    private final MongoClient cliente;
    private final MongoDatabase baseDeDatos;
    private final MongoCollection<Document> productos;
    private final MongoCollection<Document> cambiosCatalogo;

    public MongoManager(){
        cliente=MongoClients.create("mongodb://localhost:27017");
        baseDeDatos=cliente.getDatabase("Catalogo");
        productos=baseDeDatos.getCollection("Productos");
        cambiosCatalogo=baseDeDatos.getCollection("cambiosCatalogo");
    }

    public MongoCollection<Document> getProductos() {
        return productos;
    }

    public MongoCollection<Document> getCambiosCatalogo() {
        return cambiosCatalogo;
    }

    public void close(){
        cliente.close();
    }

}
