package Pedidos;

public class ItemPedido {
    public String codigo;
    public String nombre;
    public double precioUnitario;
    public int cantidad;

    public ItemPedido(String codigo, String nombre, double precioUnitario, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }
}
