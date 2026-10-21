package org.example.practica9;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private int capacidadMaxima;
    private List<Inventariable> items;

    public Inventario(int capacidadMaxima){
        this.capacidadMaxima = capacidadMaxima;
        this.items = new ArrayList<>();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public List<Inventariable> getItems() {
        return items;
    }

    public boolean agregarItem(Inventariable item){
        // Agrega un item, retorna true si fue exitoso
        if(items.size() < capacidadMaxima){
            items.add(item);
            item.registrar();
            return true;
        }
        return false;
    }

    public boolean eliminarItem(Inventariable item){
        // Elimina un item, retorna true si fue exitoso
        if(items.remove(item)){
            item.borrar();
            return true;
        }
        return false;
    }

    public void listarItems(){
        // muestra todos los items en consola
        System.out.println("Inventario (Capacidad: " + items.size() + "/" + capacidadMaxima + ")");
        for(Inventariable item : items){
            System.out.println("- " + item.toString());
        }
    }
}
