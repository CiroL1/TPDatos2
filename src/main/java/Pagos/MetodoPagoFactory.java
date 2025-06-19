package Pagos;

public class MetodoPagoFactory {

    private PagoManager pagoManager;

    public MetodoPagoFactory(PagoManager pagoManager) {
        this.pagoManager = pagoManager;
    }

    public MetodoPago crearMetodoPago(String tipo, Integer numeroTarjeta) {
        switch (tipo.toLowerCase()) {
            case "tarjeta_credito":
            case "tarjeta_debito":
                if (numeroTarjeta == null) throw new IllegalArgumentException("Número de tarjeta requerido");
                return new PagoTarjeta(pagoManager, numeroTarjeta);

            case "efectivo":
                return new PagoEfectivo();

            case "punto_retiro":
                return new PagoPuntoRetiro();

            default:
                throw new IllegalArgumentException("Tipo de pago desconocido");
        }
    }
}
