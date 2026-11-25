package org.example.practica9.Controlador;

import org.example.practica9.Modelo.*;

import java.util.Random;
import java.io.*;

public class JuegoControlador {
    private Nivel nivelActual;
    private Personaje explorador;
    private Inventario inventario;
    private boolean juegoTerminado;
    private String mensajeFinal = "";
    private String ultimoEvento = "";

    // guardar posición de reaparición
    private int reaX = 0;
    private int reaY = 0;
    private boolean tieneCheckpointActivo = false;

    public void iniciarJuego(){
        this.juegoTerminado = false;
        this.mensajeFinal = "";

        if (Persistencia.cargarEstadoJuego(this)){
            this.ultimoEvento = "La partida continúa en el ultimo checkpoint activado (" + explorador.getPosicionX() + ", " + explorador.getPosicionY() + ")";
            return;
        }

        // Si no existe archivo previo, es decir que no se guardo ninguna partida, creamos una desde (0,0)
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

        this.reaX = 0;
        this.reaY = 0;
        this.tieneCheckpointActivo = false;

        generarElementosEnElTablero();
        // guardar la configuracion inicial generada
        guardarProgresoEnElArchivo();
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
                explorador.recibirDano(obstaculo.getDano());
                this.ultimoEvento = "¡Chocaste con " + obstaculo.getNombre() + "! (-" + obstaculo.getDano() + " HP";
            }
        }

        // Interacción con Checkpoints
        for (CheckPoint checkPoint : nivelActual.getCheckPoints()) {
            if (checkPoint.getPosicionX() == x && checkPoint.getPosicionY() == y) {
                if (!checkPoint.isActivado()){
                    checkPoint.activar();
                    this.ultimoEvento = "¡Checkpoint '" + checkPoint.getNombre() + "' activado! Posición guardada";
                }else{
                    this.ultimoEvento = "Punto de control '" + checkPoint.getNombre() + "' guardado";
                }
                reaX = x;
                reaY = y;
                this.tieneCheckpointActivo = true;

                // guardar automaticamente la posicion de este checkpoint en el archivo
                guardarProgresoEnElArchivo();
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

        // restaurar vida al morir depues de haber activado checkpoint o derrota si no se activo
        if (explorador.getVida() <= 0){
            if (tieneCheckpointActivo){
                System.out.println("¡Has muerto! Reapareciendo en el último checkpoint activado...");
                explorador.restaurarVida(100);
                explorador.setPosicion(reaX, reaY);
            }else{
                juegoTerminado = true;
                mensajeFinal = "GAME OVER: Moriste antes de alcanzar un punto de control.";
            }
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
            mensajeFinal = "¡VICTORIA! Has activado todos los checkpoints del mapa";
        }
    }

    public boolean isJuegoTerminado(){
        return juegoTerminado;
    }

    public String getMensajeFinal() {
        return mensajeFinal;
    }

    public Obtaculo usarArmaParaDestruir(){
        if(juegoTerminado){
            return null;
        }
        // verificar si el personaje tiene un arma en el inventario
        Arma armaEquipada = null;
        for (Inventariable item : inventario.getItems()){
            if (item instanceof Arma){
                armaEquipada = (Arma) item;
                break;
            }
        }
        if (armaEquipada == null){
            return null; // no hay armas en el inventario
        }

        int px = explorador.getPosicionX();
        int py = explorador.getPosicionY();

        // buscar si hay algun obstaculo en la casilla del personaje o casillas adyacentes
        for (Obtaculo obtaculo : nivelActual.getObtaculos()){
            // si el obstaculo esta en la misma posición o cerca
            if (!obtaculo.isDestruido() && Math.abs(obtaculo.getPosicionX() - px) <= 1 && Math.abs(obtaculo.getPosicionY() - py) <= 1){
                obtaculo.destruye();
                return obtaculo;
            }
        }
        return null;
    }
    // enviara mensaje a la interfaz gráfica para mostrar mensaje del ultimo evento
    public String consumirUltimoEvento(){
        String mensaje = ultimoEvento;
        ultimoEvento = "";
        return mensaje;
    }

    public void guardarProgresoEnElArchivo(){
        Persistencia.guardarEstadoJuego(explorador, nivelActual, inventario, reaX, reaY, tieneCheckpointActivo);
    }

    public void reiniciarPartida(){
        File archivo = new File("estado_juego.txt");
        if(archivo.exists()){
            archivo.delete();
        }
        iniciarJuego();
    }

    public void restaurarDatosCheckpoint(int reaX, int reaY, boolean tieneCheckpoint){
        this.reaX = reaX;
        this.reaY = reaY;
        this.tieneCheckpointActivo = tieneCheckpoint;
    }

    public void setEstadoCargado(Personaje explorador, Nivel nivel, Inventario inventario){
        this.explorador = explorador;
        this.nivelActual = nivel;
        this.inventario = inventario;
    }
}
