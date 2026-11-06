package org.example.practica9.Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
