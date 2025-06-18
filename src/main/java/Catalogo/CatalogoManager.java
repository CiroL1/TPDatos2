package Catalogo;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import java.time.LocalDate;

import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.push;

public class CatalogoManager {
    private MongoManager mongo;
    private JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();


    public CatalogoManager() {
        this.mongo = new MongoManager();
    }

    public void agregarProducto(Producto producto) {
        Document doc = new Document("codigo", producto.getCodigo())
                .append("nombre", producto.getNombre())
                .append("descripcion", producto.getDescripcion())
                .append("precio", producto.getPrecio())
                .append("stock", producto.getStock())
                .append("imagenes", producto.getImagenes())
                .append("atributos", producto.getAtributos())
                .append("comentarios", producto.getComentarios());
        mongo.getProductos().insertOne(doc);
        /*Me devuelve la coleccion de productos y yo inserto un nuevo producto en ella*/
    }

    private void registrarCambio(String producto, String accion, String valorAnterior, String valorNuevo) {
        Document doc= new Document("timeStamp", LocalDate.now())
                .append("producto", producto)
                .append("accion", accion)
                .append("valorAnterior", valorAnterior)
                .append("valorNuevo", valorNuevo);
        mongo.getCambiosCatalogo().insertOne(doc);
    }

    /*Devuelve un solo  producto en particular*/
    public String buscarProductoPorNombre(String nombre) {
        String producto = "Este producto no se encuentra registrado";
        Bson filtro = Filters.eq("nombre", nombre);
        Document doc = this.mongo.getProductos().find(filtro).first();
        if (doc != null) {
            producto = doc.toJson(this.prettyPrint);
        }
        return producto;
    }

    /*Devuelve todos los productos que coincidan con la busqueda*/
    public String buscarProductosCompatibles(String nombre) {
        String productos = "Los siguientes productos coinciden con su busqueda: " + "\n";
        Bson filtro = Filters.eq("nombre", nombre);
        for (Document doc : mongo.getProductos().find(filtro)) {
            productos += doc.toJson(this.prettyPrint);
            productos += "\n";
        }
        return productos;
    }

    public String getPrecios() {
        String mensaje = "***** Lista de Precios *****" + "\n";
        MongoCursor<Document> cursor = this.mongo.getProductos().find()
                .projection(fields(include("codigo","nombre", "precio"),excludeId()))
                .iterator();
        try{
            while (cursor.hasNext()) {
                mensaje += cursor.next().toJson(this.prettyPrint) + "\n";
            }
        } finally{
            cursor.close();
        }
        return mensaje;
    }

    public String getCambios(){
        String mensaje="***** Registro de Cambios del Catalogo *****" + "\n";
        MongoCursor<Document> cursor = this.mongo.getCambiosCatalogo().find().iterator();
        try{
            while (cursor.hasNext()) {
                mensaje += cursor.next().toJson(this.prettyPrint) + "\n";
            }
        } finally{
            cursor.close();
        }
        return mensaje;
    }


    public void eliminarProducto(Producto producto) {
        Document filtro = new Document("nombre", producto.getNombre());
        DeleteResult deleteResult = this.mongo.getProductos().deleteOne(filtro);
        if (deleteResult.getDeletedCount() > 0) {
            System.out.println("El producto ha sido eliminado exitosamente.");
            this.registrarCambio(producto.getCodigo(), "Se ha eliminado un producto", producto.getNombre(), "");
        } else {
            System.out.println("El producto no existe");
        }
    }

    public void eliminarCatalogo(){
        this.mongo.getProductos().deleteMany(new Document());
    }


    private void actualizarValor(String producto, String campo, String valorNuevo) {
        Document filtro = new Document("codigo", producto);
        Document actualizacion = new Document()
                .append(campo, valorNuevo);
        this.mongo.getProductos().updateOne(filtro, new Document("$set", actualizacion));
    }


    private void actualizarLista(String producto, String campo, String valorNuevo) {
        this.mongo.getProductos().updateOne(
                eq("codigo",producto),
                push(campo,valorNuevo)
        );
    }


    public void actualizarPrecio(Producto producto, double precio) {
        String valorAnterior=this.getValor("precio",producto.getCodigo());
        this.registrarCambio(producto.getCodigo(),"Se ha actualizado el precio",valorAnterior,Double.toString(precio));
        this.actualizarValor(producto.getCodigo(), "precio", Double.toString(precio));

    }

    public void actualizarDescripcion(Producto producto, String descripcion) {
        String valorAnterior=this.getValor("descripcion",producto.getCodigo());
        this.registrarCambio(producto.getCodigo(),"Se ha actualizado la descripcion",valorAnterior,descripcion);
        this.actualizarValor(producto.getCodigo(), "descripcion", descripcion);
    }

    public void actualizarStock(Producto producto, int stock) {
        String valorAnterior=this.getValor("stock",producto.getCodigo());
        this.registrarCambio(producto.getCodigo(),"Se ha actualizado el stock",valorAnterior,Integer.toString(stock));
        this.actualizarValor(producto.getCodigo(), "stock", Integer.toString(stock));
    }

    public void actualizarImagenes(Producto producto, String imagen) {
        String valorAnterior=this.getValor("imagenes",producto.getCodigo());
        this.registrarCambio(producto.getCodigo(),"Se ha agregado una imagen",valorAnterior,imagen);
        this.actualizarLista(producto.getCodigo(), "imagenes", imagen);
    }

    public void actualizarAtributos(Producto producto, String atributo) {
        String valorAnterior=this.getValor("atributos",producto.getCodigo());
        this.registrarCambio(producto.getCodigo(),"Se ha agregado un atributo",valorAnterior,atributo);
        this.actualizarLista(producto.getCodigo(), "atributos", atributo);
    }

    public void actualizarComentarios(Producto producto, String comentario) {
        String valorAnterior=this.getValor("comentarios",producto.getCodigo());
        this.registrarCambio(producto.getCodigo(),"Se ha agregado un comentario",valorAnterior,comentario);
        this.actualizarLista(producto.getCodigo(), "comentarios", comentario);
    }

    private String getValor(String campo, String producto){
        String valor="";
        Document produc= this.mongo.getProductos().find(eq("codigo",producto))
                .projection(include(campo))
                .first();
        if (produc != null && produc.containsKey(campo)) {
            valor=produc.get(campo).toString();
        }
        return valor;
    }

    public void cerrarConexion() {
        this.mongo.close();
    }

    public Producto getProductoPorCodigo(String codigo) {
        Document doc = mongo.getProductos().find(eq("codigo", codigo)).first();
        if (doc == null) return null;

        return new Producto(
                doc.getString("codigo"),
                doc.getString("nombre"),
                doc.getDouble("precio"),
                doc.getInteger("stock")
        );
    }

}






