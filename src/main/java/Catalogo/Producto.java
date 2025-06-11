package Catalogo;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private String nombre;
    private double precio;
    private int stock;
    private List<String> atributos;
    private String descripcion;
    private List<String> imagenes;
    private List<String> comentarios;

    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.atributos = new ArrayList<>();
        this.imagenes = new ArrayList<>();
        this.comentarios = new ArrayList<>();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void agregarAtributo(String atributo) {
        this.atributos.add(atributo);
    }

    public void agregarImagen(String imagen) {
        this.imagenes.add(imagen);
    }

    public void agregarComentario(String comentario) {
        this.comentarios.add(comentario);
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public List<String> getAtributos() {
        return atributos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public List<String> getComentarios() {
        return comentarios;
    }
}
