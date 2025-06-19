package Pagos;

import Users.SessionManager;
import Pagos.Tarjeta;
import Pagos.TarjetaManager;

import java.time.LocalDate;

public class PagoManager {

    private final TarjetaManager tarjetaManager;

    public PagoManager(TarjetaManager tarjetaManager) {
        this.tarjetaManager = tarjetaManager;
    }
    public boolean pagarConTarjeta(int numeroTarjeta, double monto) {
        Tarjeta tarjeta = tarjetaManager.getOne(numeroTarjeta);
        if (tarjeta == null) {
            System.out.println("Tarjeta no encontrada");
            return false;
        }

        // Chequear que la tarjeta pertenece al usuario activo
        int dniActivo = (int) SessionManager.getDniUsuarioActivo();
        if (tarjeta.getDni() != dniActivo) {
            System.out.println("La tarjeta no pertenece al usuario activo");
            return false;
        }

        // Chequear saldo
        if (tarjeta.getSaldo() < monto) {
            System.out.println("Saldo insuficiente");
            return false;
        }

        // Restar saldo y actualizar
        tarjeta.setSaldo(tarjeta.getSaldo() - monto);
        tarjetaManager.update(tarjeta);

        System.out.println("Pago exitoso con tarjeta " + numeroTarjeta);
        return true;
    }
}
