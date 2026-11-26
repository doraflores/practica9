package org.example.practica9.Controlador;

import org.example.practica9.Modelo.*;

public class JuegoControlador {
    private Nivel nivelActual;
    private Personaje explorador;
    private boolean juegoTerminado;
    private String mensajeFinal = "";
    private String ultimoEvento = "";

    public void iniciarJuego(){
        this.juegoTerminado = false;
        this.mensajeFinal = "";

        if (Persistencia.cargarEstadoJuego(this)){
            this.ultimoEvento = "La partida continúa en el ultimo checkpoint activado (" + explorador.getPosicionX() + ", " + explorador.getPosicionY() + ")";
            return;
        }

        // Si no existe archivo previo, es decir que no se guardo ninguna partida, creamos una desde (0,0)
        // creando inventario y asignandolo a personaje
        Inventario inventarioInicial = new Inventario(10);
        // Creando armas y agregandolas al inventario
        inventarioInicial.agregarItem(new Arma("Hacha", 20, 2.0));
        inventarioInicial.agregarItem(new Arma("Arco", 25, 10.0));
        // creando recompensas y agregandolas al inventario
        inventarioInicial.agregarItem(new Recompensa("Moneda", 50, "Oro"));
        inventarioInicial.agregarItem(new Recompensa("Gema Verde", 100, "Tesoro"));

        // creando personaje con su propio inventario y checkpoints
        this.explorador = new Personaje("Explorador",100,0,0, inventarioInicial);

        // creando nivel 1
        this.nivelActual = new Nivel("Bosque Verde", 1, "Fácil", inventarioInicial);
        nivelActual.agregarElementoDinamico(explorador);
        this.nivelActual.generarElementosEnElTablero();
        // guardar la configuracion inicial generada
        guardarProgresoEnElArchivo();
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
        explorador.getInventario().listarItems();
    }

    public Nivel getNivelActual(){
        return nivelActual;
    }

    public Personaje getExplorador(){
        return explorador;
    }

    public Inventario getInventario(){
        return explorador.getInventario();
    }

    private void verificarInteraccionConCasilla(int x, int y) {
        // Interacción con Obstáculos (el personaje verififica la colision)
        Obstaculo obstaculoChocado = explorador.verificarColisionObstaculos(nivelActual.getObstaculos());
        if (obstaculoChocado != null) {
            this.ultimoEvento = "¡Chocaste con " + obstaculoChocado.getNombre() + "! (-" + obstaculoChocado.getDano() + " HP";
        }

        // Interacción con Checkpoints (responsabilidad del nivel)
        // el nivel busca checkpoints y el personaje gusrda su punto de reaparicion
        CheckPoint checkPoint = nivelActual.obtenerCheckPoint(x, y);
        if (checkPoint != null) {
            if (!checkPoint.isActivado()){
                checkPoint.activar();
                this.ultimoEvento = "¡Checkpoint '" + checkPoint.getNombre() + "' activado! Posición guardada";
            }else{
                this.ultimoEvento = "Punto de control '" + checkPoint.getNombre() + "' guardado";
            }
            explorador.guardarCheckpoint(x, y);
            // guardar automaticamente la posicion de este checkpoint en el archivo
            guardarProgresoEnElArchivo();
        }

        // Interacción con Utilerías
        nivelActual.interactuarConUtileria(x, y);

        // restaurar vida al morir depues de haber activado checkpoint o derrota si no se activo
        // el personaje gestiona su propia muerte y reaparicion
        if (!explorador.estaVivo()){
            if (!explorador.intentarReaparecer()){
                juegoTerminado = true;
                mensajeFinal = "GAME OVER: Moriste antes de alcanzar un punto de control.";
            }
            return;
        }

        // condicion de victoria
        if (nivelActual.todosCheckpointsActivados()){
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

    public Obstaculo usarArmaParaDestruir(){
        if(juegoTerminado){
            return null;
        }

        return explorador.atacarObstaculoCercano(nivelActual.getObstaculos());
    }
    // enviara mensaje a la interfaz gráfica para mostrar mensaje del ultimo evento
    public String consumirUltimoEvento(){
        String mensaje = ultimoEvento;
        ultimoEvento = "";
        return mensaje;
    }

    public void guardarProgresoEnElArchivo(){
        Persistencia.guardarEstadoJuego(explorador, nivelActual);
    }

    public void reiniciarPartida(){
        Persistencia.eliminarArchivoGuardado();
        iniciarJuego();
    }

    public void setEstadoCargado(Personaje explorador, Nivel nivel){
        this.explorador = explorador;
        this.nivelActual = nivel;
    }
}
