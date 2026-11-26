package org.example.practica9.Modelo;

import java.util.List;

public class Personaje implements Destruible, ElementoDinamico {
    private String nombre;
    private int vida;
    private int posicionX;
    private int posicionY;
    // atributos de reaparicion e inventario propios
    private int reaX = 0;
    private int reaY = 0;
    private boolean tieneCheckpointActivo;
    private Inventario inventario;

    public Personaje(String nombre, int vida, int x, int y, Inventario inventario){
        this.nombre = nombre;
        this.vida = vida;
        this.posicionX = x;
        this.posicionY = y;
        this.reaX = x;
        this.reaY = y;
        this.tieneCheckpointActivo = false;
        this.inventario = inventario;
    }

    public String getNombre() {
        return nombre;
    }
    public int getVida() {
        return vida;
    }
    public void setVida(int vida) {
        this.vida = vida;
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

    // nuevos sets y gets
    public int getReaX() {
        return reaX;
    }
    public void setReaX(int reaX) {
        this.reaX = reaX;
    }
    public int getReaY() {
        return reaY;
    }
    public void setReaY(int reaY) {
        this.reaY = reaY;
    }
    public boolean isTieneCheckpointActivo() {
        return tieneCheckpointActivo;
    }
    public void setTieneCheckpointActivo(boolean tieneCheckpointActivo) {
        this.tieneCheckpointActivo = tieneCheckpointActivo;
    }
    public Inventario getInventario() {
        return inventario;
    }
    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
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
                posicionY -= distancia;
                break;
            case "sur":
                posicionY += distancia;
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

    public boolean estaVivo(){
        return vida > 0;
    }

    public void restaurarVida(int cantidad){
        this.vida = cantidad;
    }

    // metodo basado en verificarEstadoCasilla del controlador
    public Obstaculo verificarColisionObstaculos(List<Obstaculo> obstaculos){
        for (Obstaculo obstaculo : obstaculos){
            if (!obstaculo.isDestruido() && obstaculo.getPosicionX() == posicionX && obstaculo.getPosicionY() == posicionY){
                recibirDano(obstaculo.getDano());
                return obstaculo; // devuelve el obstaculo con el que choco para informar a la vista
            }
        }
        return null; // devuelve que no choco con ningun obstaculo
    }

    // metodo basado en atacar los obstaculos del controlador
    public Obstaculo atacarObstaculoCercano(List<Obstaculo> obstaculos){
        if (inventario == null){
            return null; // inventario vacio
        }
        Arma arma = inventario.obtenerArmaDisponible();
        if (arma == null){
            return null; // no hay arma
        }
        for (Obstaculo obstaculo : obstaculos){
            if (!obstaculo.isDestruido() && Math.abs(obstaculo.getPosicionX() - posicionX) <= 1 &&
            Math.abs(obstaculo.getPosicionY() - posicionY) <= 1){
                obstaculo.destruye();
                return obstaculo;
            }
        }
        return null;
    }

    // que personaje guarde sus propio checkpoint
    public void guardarCheckpoint(int x, int y){
        reaX = x;
        reaY = y;
        tieneCheckpointActivo = true;
    }
    // metodo extraido de controlador
    public void restaurarDatosCheckpoint(int reaX, int reaY, boolean tieneCheckpoint){
        this.reaX = reaX;
        this.reaY = reaY;
        this.tieneCheckpointActivo = tieneCheckpoint;
    }

    // metodo en el que el personaje intenta reaparecer
    public boolean intentarReaparecer(){
        if (tieneCheckpointActivo) {
            System.out.println("¡Has muerto! Reapareciendo en el último checkpoint activado...");
            restaurarVida(100);
            setPosicion(reaX, reaY);
            return true; // tenia checkpoint activo: logro reaparecer
        }
        return false; // no tenía checkpoint: muerte definitiva
    }
}
