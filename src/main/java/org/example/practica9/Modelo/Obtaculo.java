package org.example.practica9.Modelo;

public class Obtaculo implements Destruible {
    private String nombre;
    private int dano;
    private int posicionX;
    private int posicionY;
    private boolean destruido;

    public Obtaculo(String nombre, int dano, int x, int y){
        this.nombre = nombre;
        this.dano = dano;
        this.posicionX = x;
        this.posicionY = y;
        this.destruido = false;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDano() {
        return dano;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    @Override
    public void destruye() {
        // metodo que ejecuta la logica de destruccion del elemento
        destruido = true;
        System.out.println("El obstáculo " + nombre + " ha sido destruido.");
    }
}
