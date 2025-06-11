package Catalogo;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

public class CatalogoManager {
    MongoManager mongo;

    public CatalogoManager() {
        this.mongo = new MongoManager();
    }

    public void agregarProducto(Producto producto) {
        Document doc = new Document("nombre", producto.getNombre())
                .append("descripcion", producto.getDescripcion())
                .append("precio", producto.getPrecio())
                .append("stock", producto.getStock())
                .append("imagenenes", producto.getImagenes())
                .append("atributos", producto.getAtributos())
                .append("comentarios", producto.getComentarios());
        mongo.getProductos().insertOne(doc); /*Me devuelve la coleccion de productos y yo inserto un nuevo producto en ella*/
    }


    public String buscarProductoPorNombre(String nombre) {
        String producto="Este producto no se encuentra registrado";
        Bson filtro = Filters.eq("nombre", nombre);
        Document doc= this.mongo.getProductos().find(filtro).first();
        if (doc!=null) {
            producto=doc.toJson();
        }
        return producto;
    }


}


