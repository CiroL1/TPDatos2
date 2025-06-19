package Pagos;

import java.time.LocalDate;

public class Tarjeta {

    public enum Tipo {
        CREDITO,
        DEBITO
    }

    private int numeroTarjeta;
    private Tipo tipo;
    private int dni;
    private LocalDate fechaVencimiento;
    private String numeroSeguridad;
    private String nombreDueño;
    private double saldo;

    public Tarjeta() {}

    public Tarjeta(Tipo tipo, int dni, LocalDate fechaVencimiento, String numeroSeguridad, String nombreDueño, double saldo) {
        this.tipo = tipo;
        this.dni = dni;
        this.fechaVencimiento = fechaVencimiento;
        this.numeroSeguridad = numeroSeguridad;
        this.nombreDueño = nombreDueño;
        this.saldo = saldo;
    }

    public int getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(int numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNumeroSeguridad() {
        return numeroSeguridad;
    }

    public void setNumeroSeguridad(String numeroSeguridad) {
        this.numeroSeguridad = numeroSeguridad;
    }

    public String getNombreDueño() {
        return nombreDueño;
    }

    public void setNombreDueño(String nombreDueño) {
        this.nombreDueño = nombreDueño;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Tarjeta{" +
                "numeroTarjeta=" + numeroTarjeta +
                ", tipo=" + tipo +
                ", dni=" + dni +
                ", fechaVencimiento=" + fechaVencimiento +
                ", numeroSeguridad='" + numeroSeguridad + '\'' +
                ", nombreDueño='" + nombreDueño + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}
