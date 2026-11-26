package org.example.practica9.Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class Nivel {
    private String nombre;
    private int numero;
    private String dificultad;
    private List<Obtaculo> obtaculos;
    private List<CheckPoint> checkPoints;
    private List<ElementoDinamico> elementosDinamicos;
    private Inventario inventario;

    public Nivel(String nombre, int numero, String dificultad, Inventario inventario){
        this.nombre = nombre;
        this.numero = numero;
        this.dificultad = dificultad;
        this.inventario = inventario;
        this.obtaculos = new ArrayList<>();
        this.checkPoints = new ArrayList<>();
        this.elementosDinamicos = new ArrayList<>();
    }

    // metodo extraido de controlador
    public void generarElementosEnElTablero() {
        Random rmd = new Random();
        Set<String> posicionesOcupadas = new HashSet<>();
        posicionesOcupadas.add("0,0"); // posicion donde aparecec el personaje

        // agregar los checkpoints con la condicion que para ganar hay que activar todos
        int [][] posCheckpoints = {{-4,3}, {5, -4}};
        for (int i = 0; i < posCheckpoints.length; i++){
            int checkpointX = posCheckpoints[i][0];
            int checkpointY = posCheckpoints[i][1];
            posicionesOcupadas.add(checkpointX + "," + checkpointY);
            agregarCheckPoint(new CheckPoint("Campamento " + (i + 1), checkpointX, checkpointY));
        }

        // colocacion de obtaculos
        // rocas
        for (int i = 1; i <= 4; i++){
            int x;
            int y;
            do {
                x = rmd.nextInt(13) - 6;
                y = rmd.nextInt(13) - 6;
            } while (posicionesOcupadas.contains(x + "," + y));
            posicionesOcupadas.add(x + "," + y);
            agregarObstaculo(new Obtaculo("Roca" + i, 15, x, y));
        }
        // arboles caidos
        for (int i = 1; i <= 4; i++){
            int x;
            int y;
            do {
                x = rmd.nextInt(13) - 6;
                y = rmd.nextInt(13) - 6;
            } while (posicionesOcupadas.contains(x + "," + y));
            posicionesOcupadas.add(x + "," + y);
            agregarObstaculo(new Obtaculo("Árbol caído" + i, 10, x, y));
        }
        // colocacion de utileria
        // arboles decorativos
        for (int i = 1; i <= 4; i++){
            int x;
            int y;
            do {
                x = rmd.nextInt(13) - 6;
                y = rmd.nextInt(13) - 6;
            } while (posicionesOcupadas.contains(x + "," + y));
            posicionesOcupadas.add(x + "," + y);
            agregarElementoDinamico(new Utileria("Arbol" + i, "Decoración", x, y));
        }
        // flores
        for (int i = 1; i <= 4; i++){
            int x;
            int y;
            do {
                x = rmd.nextInt(13) - 6;
                y = rmd.nextInt(13) - 6;
            } while (posicionesOcupadas.contains(x + "," + y));
            posicionesOcupadas.add(x + "," + y);
            agregarElementoDinamico(new Utileria("Flor" + i, "Flor silvestre", x, y));
        }
    }

    // metodo basado en la condicion de victoria del controlador
    public boolean todosCheckpointsActivados(){
        for (CheckPoint checkPoint : checkPoints){
            if (!checkPoint.isActivado()){
                return false;
            }
        }
        return true;
    }

    // metodo basado en verificar interaccionConCasillas del controlador
    public CheckPoint obtenerCheckPoint(int x, int y){
        for (CheckPoint checkPoint : checkPoints){
            if (checkPoint.getPosicionX() == x && checkPoint.getPosicionY() == y){
                return checkPoint;
            }
        }
        return null;
    }

    // metodo basado en verificar interaccionesConCasillas del controlador
    public Utileria interactuarConUtileria(int x, int y){
        for (ElementoDinamico elementoDinamico : elementosDinamicos){
            if (elementoDinamico instanceof Utileria){
                Utileria utileria = (Utileria) elementoDinamico;
                if (utileria.getPosicionX() == x && utileria.getPosicionY() == y && !utileria.isUsada()){
                    utileria.usar();
                    return utileria;
                }
            }
        }
        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNumero() {
        return numero;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void agregarObstaculo(Obtaculo obtaculo){
        // añade un obstaculo al nivel
        obtaculos.add(obtaculo);
    }

    public void agregarCheckPoint(CheckPoint checkPoint){
        // Añade un checkPoint al nivel
        checkPoints.add(checkPoint);

    }

    public void agregarElementoDinamico(ElementoDinamico elementoDinamico){
        // añade un elemento dinámico
        elementosDinamicos.add(elementoDinamico);
    }

    public void moverElementosDinamicos(){
        // item sobre todos los elementos dinámicos e invoca
        // su método mover con parámetros aleatorios
        String[] direcciones = {"norte", "sur", "este", "oeste"};
        Random rmd = new Random();

        System.out.println("Moviendo elementos dinámicos del nivel " + nombre);
        for(ElementoDinamico elementoDinamico : elementosDinamicos){
            String direccionAleatoria = direcciones[rmd.nextInt(direcciones.length)];
            int distanciaAleatoria = rmd.nextInt(10) + 1;
            elementoDinamico.mover(direccionAleatoria, distanciaAleatoria);
        }

    }

    public void mostrarEstado(){
        // muestra el estado actual del nivel
        System.out.println("Nivel " + numero + ": " + nombre + "(Dificultad: " + dificultad + ")");
        System.out.println("Obstáculos en el nivel: " + obtaculos.size());
        System.out.println("Checkpoints dinámicos: " + checkPoints.size());
        System.out.println("Elementos dinámicos: " + elementosDinamicos.size());
        if(inventario != null){
            inventario.listarItems();
        }
    }

    // getters añadidos
    public List<Obtaculo> getObtaculos(){
        return obtaculos;
    }

    public List<CheckPoint> getCheckPoints(){
        return checkPoints;
    }

    public List<ElementoDinamico> getElementosDinamicos(){
        return elementosDinamicos;
    }
}
