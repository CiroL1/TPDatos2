package Pagos;

public class MetodoPagoFactory {

    public static MetodoPago crearMetodoPago(String tipo, PagoManager pagoManager, Integer numeroTarjeta) {
        switch (tipo.toLowerCase()) {
            case "tarjeta_credito":
            case "tarjeta_debito":
                if (numeroTarjeta == null) {
                    throw new IllegalArgumentException("Número de tarjeta requerido para tarjeta");
                }
                return new PagoTarjeta(pagoManager, numeroTarjeta);

            case "efectivo":
                return new PagoEfectivo();

            case "punto_retiro":
                return new PagoPuntoRetiro();

            default:
                throw new IllegalArgumentException("Tipo de pago desconocido: " + tipo);
        }
    }
}
