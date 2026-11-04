package org.example.practica9.Modelo;


public class Arma implements Inventariable {
    private String nombre;
    private int dano;
    private double alcance;

    public Arma(String nombre, int dano, double alcance){
        this.nombre = nombre;
        this.dano = dano;
        this.alcance = alcance;
    }

    public String getNombre(){
        return nombre;
    }

    public int getDano() {
        return dano;
    }

    public double getAlcance() {
        return alcance;
    }

    @Override
    public String toString() {
        return "Arma [Nombre: " + nombre + ", Daño: " + dano + ", Alcance: " + alcance + "]";
    }

    @Override
    public void registrar() {
        // metodo que registra el elemento en el inventario del jugador
        System.out.println("Arma '" + nombre + "' registrada en el inventario.");
    }

    @Override
    public void borrar() {
        // metodo que elimina el elemento del inventario
        System.out.println("Arma '" + nombre + "' eliminada del inventario.");
    }
}
