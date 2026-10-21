package org.example.practica9;

public class Main {
    public static void main(String[] args) {
        Inventario inventarioJugador = new Inventario(10);
        Arma espada = new Arma("Espada de Fuego", 50, 2.5);
        Recompensa moneda = new Recompensa("Moneda de oro", 100, "Moneda");
        inventarioJugador.agregarItem(espada);
        inventarioJugador.agregarItem(moneda);
        Nivel nivel1 = new Nivel("Cueva de Lava", 1, "Fácil", inventarioJugador);
        nivel1.agregarObstaculo(new Obtaculo("Volcan de Lava", 20, 5, 10));
        nivel1.agregarCheckPoint(new CheckPoint("Checkpoint 1", 0, 0));
        Personaje heroe = new Personaje("Superviviente", 100, 2, 3);
        Utileria antorcha = new Utileria("Antorcha", "Ilumina el camino", 3, 4);
        nivel1.agregarElementoDinamico(heroe);
        nivel1.agregarElementoDinamico(antorcha);
        nivel1.mostrarEstado();
        System.out.println("\n\n");
        nivel1.moverElementosDinamicos();
    }
}
