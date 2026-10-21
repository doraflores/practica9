package org.example.practica9;
;

public class Utileria implements ElementoDinamico{
    private String nombre;
    private String descripcion;
    private int posicionX;
    private int posicionY;
    private boolean usada;

    public Utileria(String nombre, String descripcion, int x, int y){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.posicionX = x;
        this.posicionY = y;
        this.usada = false;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public boolean isUsada(){
        return usada;
    }

    public void usar(){
        // ejecuta la accion del objeto
        usada = true;
        System.out.println("Utilería " + nombre + " utilizada: " + descripcion);
    }

    @Override
    public void mover(String direccion, int distancia) {
        // desplaza la utileria según dirección y distancia
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
}
