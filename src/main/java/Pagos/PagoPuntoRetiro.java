package Pagos;

public class PagoPuntoRetiro implements MetodoPago {

    @Override
    public boolean procesarPago(double monto) {
        System.out.println("Pago en punto de retiro agendado por $" + monto);
        return true;
    }
}
