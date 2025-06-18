package Pedidos;

import java.util.List;

public class Pedido {
    public int numeroPedido;
    public String nombreCliente;
    public String direccion;
    public String condicionIVA;
    public List<ItemPedido> productos;
    public double valorBruto;
    public double descuento;
    public double impuesto;
    public double valorNeto;

    public Pedido(int numeroPedido, String nombreCliente, String direccion, String condicionIVA, List<ItemPedido> productos,
                  double valorBruto, double descuento, double impuesto, double valorNeto) {
        this.numeroPedido = numeroPedido;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.condicionIVA = condicionIVA;
        this.productos = productos;
        this.valorBruto = valorBruto;
        this.descuento = descuento;
        this.impuesto = impuesto;
        this.valorNeto = valorNeto;
    }
}
