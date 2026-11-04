package org.example.practica9.Controlador;

import org.example.practica9.Modelo.*;

public class JuegoControlador {
    private Nivel nivelActual;
    private Personaje explorador;
    private Inventario inventario;

    public void iniciarJuego(){
        // creando inventario
        this.inventario = new Inventario(10);
        // Creando armas
        Arma hacha = new Arma("Hacha", 20, 2.0);
        Arma arco = new Arma("Arco", 25, 10.0);
        // creando recompensas
        Recompensa moneda = new Recompensa("Moneda", 50, "Oro");
        Recompensa gema = new Recompensa("Gema Verde", 100, "Tesoro");
        // agregando armsa y recompenzas al inventario
        this.inventario.agregarItem(hacha);
        this.inventario.agregarItem(arco);
        this.inventario.agregarItem(moneda);
        this.inventario.agregarItem(gema);
        // creando personaje
        this.explorador = new Personaje("Explorador",100,0,0);
        // creando obstaculos
        Obtaculo roca = new Obtaculo("Roca Gigante",10,3,5);
        Obtaculo arbolCaido = new Obtaculo("Árbol Caído",15,6,2);
        // creando checkPoints
        CheckPoint fogata = new CheckPoint("Fogata",10,10);
        CheckPoint campamento = new CheckPoint("Campamento",20,20);
        // creando utileria
        Utileria arbol = new Utileria("Árbol", "Decoración del bosque", 1, 1);
        Utileria flor = new Utileria("Flor", "Flor silvestre", 2, 4);
        // creando nivel 1
        this.nivelActual = new Nivel("Bosque Verde", 1, "Fácil", this.inventario);
        nivelActual.agregarObstaculo(roca);
        nivelActual.agregarObstaculo(arbolCaido);
        nivelActual.agregarElementoDinamico(explorador);
        nivelActual.agregarElementoDinamico(arbol);
        nivelActual.agregarElementoDinamico(flor);
        nivelActual.agregarCheckPoint(fogata);
        nivelActual.agregarCheckPoint(campamento);

        nivelActual.mostrarEstado();
        System.out.println("\nMoviendo elementos");
        nivelActual.moverElementosDinamicos();
        System.out.println("\nEstado actualizado");
        nivelActual.mostrarEstado();
        System.out.println("\nActivando checkpoint");
        fogata.activar();
        System.out.println("\nUsando utilería");
        arbol.usar();
        System.out.println("\nDestruyendo obstáculo");
        roca.destruye();
        System.out.println("\nDaño al explorador");
        explorador.recibirDano(15);

        /**
        // creando nivel 2
        Nivel nivel2 = new Nivel("Bosque Oscuro", 2, "Media", inventario);
        // creando nivel 3
        Nivel nivel3 = new Nivel("Montaña Encantada", 3, "Difícil", inventario);
        **/
    }
    public void mostrarNivel(){
        nivelActual.mostrarEstado();
    }
    public void moverExplorador(String direccion, int distancia){
        explorador.mover(direccion, distancia);
    }
    public void mostrarInventario(){
        inventario.listarItems();
    }

    public Personaje getExplorador(){
        return explorador;
    }

    public Inventario getInventario(){
        return inventario;
    }
}
