package org.example.practica9;

public class Recompensa implements Inventariable{
    private String nombre;
    private int valor;
    private String tipo;

    public Recompensa(String nombre, int valor, String tipo){
        this.nombre = nombre;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Recompensa [Nombre: " + nombre + ", valor: " + valor + ", tipo: " + tipo + "]";
    }

    @Override
    public void registrar() {
        // metodo que registra el elemento en el inventario del jugador
        System.out.println("Recompensa ´" + nombre + "´ registrada en el inventario.");
    }

    @Override
    public void borrar() {
        // metodo que elimina el elemento del inventario
        System.out.println("Recompensa ´" + nombre + "´ eliminada del inventario.");
    }
}
