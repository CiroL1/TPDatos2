package Facturas;

import Pedidos.ItemPedido;
import Pedidos.Pedido;
import Pagos.MetodoPago;
import Catalogo.CatalogoManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Factura {
    public static int contador = 1;

    private int numeroFactura;
    private String nombreCliente;
    private String direccion;
    private String condicionIva;
    private Pedido pedido;
    private MetodoPago metodoPago;
    private String fechaHora;
    private double montoTotal;
    private String empleadoFacturador;

    public Factura(String nombreCliente, String direccion, String condicionIva, Pedido pedido, MetodoPago metodoPago, String empleadoFacturador) {
        this.numeroFactura = contador++;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.condicionIva = condicionIva;
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        this.montoTotal = pedido.getValorNeto();
        this.empleadoFacturador = empleadoFacturador;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String generarDetalleProductos() {
        StringBuilder detalle = new StringBuilder();
        for (ItemPedido item : pedido.getProductos()) {
            detalle.append("- ")
                    .append(item.getNombre())
                    .append(" x").append(item.getCantidad())
                    .append(" @ $").append(item.getPrecioUnitario())
                    .append(" = $").append(item.getCantidad() * item.getPrecioUnitario())
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
                "Subtotal: $" + pedido.getValorBruto() + "\n" +
                "Descuento: -$" + pedido.getDescuento() + "\n" +
                "Impuesto (IVA): $" + pedido.getImpuesto() + "\n" +
                "TOTAL A PAGAR: $" + montoTotal + "\n";
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getCondicionIva() {
        return condicionIva;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public String getEmpleadoFacturador() {
        return empleadoFacturador;
    }

    public double getMontoTotal() {
        return montoTotal;
    }
}
