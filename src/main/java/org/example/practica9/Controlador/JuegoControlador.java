package org.example.practica9.Controlador;

import org.example.practica9.Modelo.*;

import java.util.Random;

public class JuegoControlador {
    private Nivel nivelActual;
    private Personaje explorador;
    private Inventario inventario;
    private boolean juegoTerminado;
    private String mensajeFinal = "";

    public void iniciarJuego(){
        // creando inventario
        this.inventario = new Inventario(10);
        // Creando armas
        Arma hacha = new Arma("Hacha", 20, 2.0);
        Arma arco = new Arma("Arco", 25, 10.0);
        // creando recompensas
        Recompensa moneda = new Recompensa("Moneda", 50, "Oro");
        Recompensa gema = new Recompensa("Gema Verde", 100, "Tesoro");
        // agregando armas y recompenzas al inventario
        this.inventario.agregarItem(hacha);
        this.inventario.agregarItem(arco);
        this.inventario.agregarItem(moneda);
        this.inventario.agregarItem(gema);
        // creando personaje
        this.explorador = new Personaje("Explorador",100,0,0);

        // creando nivel 1
        this.nivelActual = new Nivel("Bosque Verde", 1, "Fácil", this.inventario);
        nivelActual.agregarElementoDinamico(explorador);

        this.juegoTerminado = false;
        this.mensajeFinal = "";

        generarElementosEnElTablero();
    }

    private void generarElementosEnElTablero() {
        Random rmd = new Random();
        java.util.Set<String> posicionesOcupadas = new java.util.HashSet<>();
        posicionesOcupadas.add("0,0"); // posicion donde aparecec el personaje

        // agregar los checkpoints con la condicion que para ganar hay que activar todos
        int [][] posCheckpoints = {{-4,3}, {5, -4}};
        for (int i = 0; i < posCheckpoints.length; i++){
            int checkpointX = posCheckpoints[i][0];
            int checkpointY = posCheckpoints[i][1];
            posicionesOcupadas.add(checkpointX + "," + checkpointY);
            nivelActual.agregarCheckPoint(new CheckPoint("Campamento" + (i + 1), checkpointX, checkpointY));
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
            nivelActual.agregarObstaculo(new Obtaculo("Roca" + i, 15, x, y));
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
            nivelActual.agregarObstaculo(new Obtaculo("Árbol caído" + i, 10, x, y));
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
            nivelActual.agregarElementoDinamico(new Utileria("Arbol" + i, "Decoración", x, y));
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
            nivelActual.agregarElementoDinamico(new Utileria("Flor" + i, "Flor silvestre", x, y));
        }
    }

    public void mostrarNivel(){
        nivelActual.mostrarEstado();
    }

    // limitar el movimiento del personaje dentro del tablero
    public void moverExplorador(String direccion, int distancia){
        if (juegoTerminado){
            return;
        }

        int actualX = explorador.getPosicionX();
        int actualY = explorador.getPosicionY();

        int proximoX = actualX;
        int proximoY = actualY;

        switch (direccion.toLowerCase()){
            case "norte":
                proximoY -= distancia;
                break;
            case "sur":
                proximoY += distancia;
                break;
            case "este":
                proximoX += distancia;
                break;
            case "oeste":
                proximoX -= distancia;
                break;
        }
        // verificar que la direccion a la que se dirige esta dentro del mapa
        if(proximoX >= -7 && proximoX <= 7 && proximoY >= -7 && proximoY <= 7){
            explorador.mover(direccion,distancia);
            verificarInteraccionConCasilla(proximoX, proximoY);
        }
    }
    public void mostrarInventario(){
        inventario.listarItems();
    }

    public Nivel getNivelActual(){
        return nivelActual;
    }

    public Personaje getExplorador(){
        return explorador;
    }

    public Inventario getInventario(){
        return inventario;
    }

    private void verificarInteraccionConCasilla(int x, int y) {
        // Interacción con Obstáculos
        for (Obtaculo obstaculo : nivelActual.getObtaculos()) {
            if (obstaculo.getPosicionX() == x && obstaculo.getPosicionY() == y) {
                obstaculo.destruye();
                explorador.recibirDano(obstaculo.getDano());
                System.out.println("¡Colisionaste con " + obstaculo.getNombre() + "! Recibiste " + obstaculo.getDano() + " de daño.");
            }
        }

        // Interacción con Checkpoints
        for (CheckPoint checkPoint : nivelActual.getCheckPoints()) {
            if (checkPoint.getPosicionX() == x && checkPoint.getPosicionY() == y && !checkPoint.isActivado()) {
                checkPoint.activar();
                System.out.println("¡Has llegado al checkpoint: " + checkPoint.getNombre() + "!");
            }
        }

        /// //////////////////
        // Interacción con Utilerías
        for (ElementoDinamico elementoDinamico : nivelActual.getElementosDinamicos()) {
            if (elementoDinamico instanceof Utileria) {
                Utileria utileria = (Utileria) elementoDinamico;
                if (utileria.getPosicionX() == x && utileria.getPosicionY() == y && !utileria.isUsada()) {
                    utileria.usar();
                }
            }
        }

        // condicion de derrota
        if(explorador.getVida() <= 0){
            juegoTerminado = true;
            mensajeFinal = "GAME OVER";
            return;
        }

        // condicion de victoria
        boolean todosCheckpointsActivados = true;
        for(CheckPoint checkPoint : nivelActual.getCheckPoints()){
            if (!checkPoint.isActivado()){
                todosCheckpointsActivados = false;
                break;
            }
        }
        if (todosCheckpointsActivados){
            juegoTerminado = true;
            mensajeFinal = "¡VICTORIA!";
        }
    }

    public boolean isJuegoTerminado(){
        return juegoTerminado;
    }

    public String getMensajeFinal() {
        return mensajeFinal;
    }

}
