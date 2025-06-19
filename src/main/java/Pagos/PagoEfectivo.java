package Pagos;

public class PagoEfectivo implements MetodoPago {

    @Override
    public boolean procesarPago(double monto) {
        // Como es efectivo, asumimos que el pago siempre se realiza con éxito
        System.out.println("Pago en efectivo procesado por $" + monto);
        return true;
    }
}
