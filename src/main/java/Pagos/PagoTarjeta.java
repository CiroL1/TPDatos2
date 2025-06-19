package Pagos;

public class PagoTarjeta implements MetodoPago {
    private PagoManager pagoManager;
    private int numeroTarjeta;

    public PagoTarjeta(PagoManager pagoManager, int numeroTarjeta) {
        this.pagoManager = pagoManager;
        this.numeroTarjeta = numeroTarjeta;
    }

    @Override
    public boolean procesarPago(double monto) {
        return pagoManager.pagarConTarjeta(numeroTarjeta, monto);
    }
}
