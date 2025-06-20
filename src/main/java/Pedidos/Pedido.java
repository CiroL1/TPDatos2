package Pedidos;

import java.util.List;

public class Pedido {
    private int numeroPedido;
    private String nombreCliente;
    private String direccion;
    private String condicionIVA;
    private List<ItemPedido> productos;
    private double valorBruto;
    private double descuento;
    private double impuesto;
    private double valorNeto;

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

    public List<ItemPedido> getProductos() {
        return productos;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getValorBruto() {
        return valorBruto;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public double getValorNeto() {
        return valorNeto;
    }
}
