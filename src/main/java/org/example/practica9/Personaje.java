package org.example.practica9;

public class Personaje implements Destruible, ElementoDinamico{
    private String nombre;
    private int vida;
    private int posicionX;
    private int posicionY;

    public Personaje(String nombre, int vida, int x, int y){
        this.nombre = nombre;
        this.vida = vida;
        this.posicionX = x;
        this.posicionY = y;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVida() {
        return vida;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    public void setPosicion(int x, int y){
        this.posicionX = x;
        this.posicionY = y;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    public void recibirDano(int dano){
        // reduce vida segun daño recibido
        vida -= dano;
        if(vida < 0){
            vida = 0;
        }
        System.out.println(nombre + " recibió " + dano + " puntos de daño. Vida actual: " + vida);
    }

    @Override
    public void mover(String direccion, int distancia) {
        // mueve el personaje según dirección(norte, sur, este, oeste) y distancia
        switch (direccion.toLowerCase()){
            case "norte":
                posicionY += distancia;
                break;
            case "sur":
                posicionY -= distancia;
                break;
            case "este":
                posicionX += distancia;
                break;
            case "oeste":
                posicionX -= distancia;
                break;
            default:
                System.out.println("Dirección no válida.");
        }
        System.out.println(nombre + " se movió " + distancia + " unidades hacia el " + direccion + ".");
    }

    @Override
    public void destruye() {
        // metodo que ejecuta la logica de destruccion del elemento
        vida = 0;
        System.out.println("El personaje " + nombre + " ha sido destruido.");
    }
}
