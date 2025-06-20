package Pedidos;

public class ItemPedido {
    private String codigo;
    private String nombre;
    private double precioUnitario;
    private int cantidad;

    public ItemPedido(String codigo, String nombre, double precioUnitario, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }
}
