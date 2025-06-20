package Facturas;

import Pedidos.ItemPedido;
import Pedidos.Pedido;
import Pagos.MetodoPago;
import Catalogo.CatalogoManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Factura {
    public static int contador = 1;

    public int numeroFactura;
    public String nombreCliente;
    public String direccion;
    public String condicionIva;
    public Pedido pedido;
    public MetodoPago metodoPago;
    public String fechaHora;
    public double montoTotal;
    public String empleadoFacturador;

    public Factura(String nombreCliente, String direccion, String condicionIva, Pedido pedido, MetodoPago metodoPago, String empleadoFacturador) {
        this.numeroFactura = contador++;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.condicionIva = condicionIva;
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        this.montoTotal = pedido.valorNeto;
        this.empleadoFacturador = empleadoFacturador;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String generarDetalleProductos() {
        StringBuilder detalle = new StringBuilder();
        for (ItemPedido item : pedido.productos) {
            detalle.append("- ")
                    .append(item.nombre)
                    .append(" x").append(item.cantidad)
                    .append(" @ $").append(item.precioUnitario)
                    .append(" = $").append(item.cantidad * item.precioUnitario)
                    .append("\n");
        }
        return detalle.toString();
    }

    @Override
    public String toString() {
        return "Factura #" + numeroFactura + "\n" +
                "Cliente: " + nombreCliente + "\n" +
                "Dirección: " + direccion + "\n" +
                "Condición IVA: " + condicionIva + "\n" +
                "Fecha y Hora: " + fechaHora + "\n" +
                "Método de pago: " + metodoPago.getClass().getSimpleName() + "\n" +
                "Empleado que facturó: " + empleadoFacturador + "\n" +
                "-----------------------------\n" +
                "Detalle del pedido:\n" + generarDetalleProductos() +
                "Subtotal: $" + pedido.valorBruto + "\n" +
                "Descuento: -$" + pedido.descuento + "\n" +
                "Impuesto (IVA): $" + pedido.impuesto + "\n" +
                "TOTAL A PAGAR: $" + montoTotal + "\n";
    }
}
