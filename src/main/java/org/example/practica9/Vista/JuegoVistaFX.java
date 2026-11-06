package org.example.practica9.Vista;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.practica9.Controlador.JuegoControlador;
import org.example.practica9.Modelo.*;

public class JuegoVistaFX extends Application {

    private JuegoControlador controlador;
    private Pane mapa;
    private StackPane exploradorGrafico;
    private TextArea areaEstado;

    private final int TAMANO_CELDA = 30;
    // Offset para que la posición (0,0) esté centrada en el tablero (450x450)
    private final int OFFSET_X = 210;
    private final int OFFSET_Y = 210;

    public JuegoVistaFX() {
        super();
    }

    @Override
    public void start(Stage primaryStage) {
        controlador = new JuegoControlador();
        controlador.iniciarJuego();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1e1e2e;");

        // --- CABECERA ---
        HBox header = new HBox();
        header.setPadding(new Insets(0, 0, 15, 0));
        header.setAlignment(Pos.CENTER);
        Label titulo = new Label("EXPLORADOR");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#f5e0dc"));
        header.getChildren().add(titulo);
        root.setTop(header);

        // --- MAPA DEL JUEGO ---
        mapa = new Pane();
        mapa.setPrefSize(450, 450);
        mapa.setStyle(
                "-fx-background-color: #272a37;" +
                        "-fx-border-color: #45475a;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;"
        );

        dibujarCuadriculaFondo();
        dibujarElementosDelJuego();

        // Crear y posicionar al explorador
        exploradorGrafico = crearAvatarPersonaje();
        actualizarPosicionExplorador();
        mapa.getChildren().add(exploradorGrafico);

        root.setCenter(mapa);

        // --- 3. PANEL DE CONTROLES ---
        VBox panelDerecho = new VBox(12);
        panelDerecho.setPadding(new Insets(0, 0, 0, 15));
        panelDerecho.setPrefWidth(240);

        VBox boxMovimiento = new VBox(8);
        boxMovimiento.setAlignment(Pos.CENTER);
        boxMovimiento.setStyle("-fx-background-color: #313244; -fx-padding: 12; -fx-background-radius: 8;");

        Label lblMover = new Label("PANEL DE CONTROL");
        lblMover.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        lblMover.setTextFill(Color.web("#cdd6f4"));

        GridPane pad = new GridPane();
        pad.setHgap(6);
        pad.setVgap(6);
        pad.setAlignment(Pos.CENTER);

        Button btnNorte = crearBotonDireccion("▲");
        Button btnSur   = crearBotonDireccion("▼");
        Button btnEste  = crearBotonDireccion("►");
        Button btnOeste = crearBotonDireccion("◄");

        pad.add(btnNorte, 1, 0);
        pad.add(btnOeste, 0, 1);
        pad.add(btnEste, 2, 1);
        pad.add(btnSur, 1, 2);

        btnNorte.setOnAction(e -> mover("norte"));
        btnSur.setOnAction(e -> mover("sur"));
        btnEste.setOnAction(e -> mover("este"));
        btnOeste.setOnAction(e -> mover("oeste"));

        boxMovimiento.getChildren().addAll(lblMover, pad);

        Button btnInventario = new Button("Ver Inventario");
        btnInventario.setMaxWidth(Double.MAX_VALUE);
        btnInventario.setStyle(
                "-fx-background-color: #89b4fa; -fx-text-fill: #11111b; " +
                        "-fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 6; -fx-cursor: hand;"
        );
        btnInventario.setOnAction(e -> mostrarInventario());

        areaEstado = new TextArea();
        areaEstado.setEditable(false);
        areaEstado.setWrapText(true);
        areaEstado.setPrefHeight(180);
        areaEstado.setStyle(
                "-fx-control-inner-background: #181825; " +
                        "-fx-text-box-border: transparent; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-text-fill: #a6adc8;"
        );
        areaEstado.setText("¡Aventura iniciada!\nUsa la cruceta para moverte.\n");

        panelDerecho.getChildren().addAll(boxMovimiento, btnInventario, areaEstado);
        root.setRight(panelDerecho);

        Scene scene = new Scene(root, 730, 530);
        primaryStage.setTitle("Juego Explorador - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button crearBotonDireccion(String texto) {
        Button btn = new Button(texto);
        btn.setMinSize(40, 40);
        btn.setStyle(
                "-fx-background-color: #45475a; -fx-text-fill: #cdd6f4; " +
                        "-fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #585b70; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #45475a; -fx-text-fill: #cdd6f4; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;"));
        return btn;
    }

    private StackPane crearAvatarPersonaje() {
        Circle cuerpo = new Circle(13, Color.web("#a6e3a1"));
        cuerpo.setStroke(Color.web("#40a02b"));
        cuerpo.setStrokeWidth(2);

        Text inicial = new Text("E");
        inicial.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        inicial.setFill(Color.web("#11111b"));

        StackPane avatar = new StackPane(cuerpo, inicial);
        avatar.setEffect(new DropShadow(10, Color.web("#a6e3a1")));
        return avatar;
    }

    private void dibujarCuadriculaFondo() {
        for (int i = 0; i <= 450; i += TAMANO_CELDA) {
            Rectangle lineaH = new Rectangle(450, 1, Color.web("#313244"));
            lineaH.setY(i);
            Rectangle lineaV = new Rectangle(1, 450, Color.web("#313244"));
            lineaV.setX(i);
            mapa.getChildren().addAll(lineaH, lineaV);
        }
    }

    private void dibujarElementosDelJuego() {
        // OBSTÁCULOS (Rojo / Marrón)
        // Roca Gigante, Árbol Caído
        // Dibujar Obstáculos desde la lista del Nivel
        for (Obtaculo obstaculos : controlador.getNivelActual().getObtaculos()) {
            Color color = obstaculos.getNombre().startsWith("Roca") ? Color.web("#f38ba8") : Color.web("#eba0ac");
            String figura = obstaculos.getNombre().startsWith("Roca") ? "ROCA" : "TRONCO";
            mapa.getChildren().add(crearFiguraGrafica(figura, color, obstaculos.getPosicionX(), obstaculos.getPosicionY()));
        }

        // CHECKPOINTS (Naranja / Amarillo)
        // Fogata, Campamento
        // Dibujar CheckPoints desde la lista del Nivel
        for (CheckPoint checkpoint : controlador.getNivelActual().getCheckPoints()) {
            Color color = checkpoint.getNombre().equals("Fogata") ? Color.web("#fab387") : Color.web("#f9e2af");
            mapa.getChildren().add(crearFiguraGrafica("FOGATA", color, checkpoint.getPosicionX(), checkpoint.getPosicionY()));
        }

        // UTILERÍA (Verde / Violeta)
        // Árbol Decorativo, Flor
        // Dibujar Utilería desde Elementos Dinámicos
        for (ElementoDinamico elementoDinamico : controlador.getNivelActual().getElementosDinamicos()) {
            if (elementoDinamico instanceof Utileria) {
                Utileria utileria = (Utileria) elementoDinamico; //[cite: 10]
                Color color = utileria.getNombre().startsWith("Arbol") ? Color.web("#94e2d5") : Color.web("#cba6f7");
                String figura = utileria.getNombre().startsWith("Arbol") ? "ARBOL" : "FLOR";
                mapa.getChildren().add(crearFiguraGrafica(figura, color, utileria.getPosicionX(), utileria.getPosicionY()));
            }
        }
    }

    private StackPane crearFiguraGrafica(String tipo, Color color, int gridX, int gridY) {
        StackPane nodo = new StackPane();

        switch (tipo) {
            case "ROCA":
                Rectangle roca = new Rectangle(22, 22, color);
                roca.setArcWidth(6);
                roca.setArcHeight(6);
                nodo.getChildren().add(roca);
                break;

            case "TRONCO":
                Rectangle tronco = new Rectangle(26, 12, color);
                tronco.setArcWidth(4);
                tronco.setArcHeight(4);
                nodo.getChildren().add(tronco);
                break;

            case "FOGATA":
                Polygon fuego = new Polygon(0.0, 0.0, 10.0, -18.0, 20.0, 0.0);
                fuego.setFill(color);
                nodo.getChildren().add(fuego);
                break;

            case "ARBOL":
                Polygon pino = new Polygon(0.0, 0.0, 12.0, -22.0, 24.0, 0.0);
                pino.setFill(color);
                nodo.getChildren().add(pino);
                break;

            case "FLOR":
                Circle flor = new Circle(8, color);
                nodo.getChildren().add(flor);
                break;

            default: // Campamento u otros
                Rectangle base = new Rectangle(20, 20, color);
                nodo.getChildren().add(base);
                break;
        }

        // Convertir coordenadas del modelo a pixeles usando el Offset del mapa
        nodo.setLayoutX(OFFSET_X + (gridX * TAMANO_CELDA) + 3);
        nodo.setLayoutY(OFFSET_Y + (gridY * TAMANO_CELDA) + 3);
        return nodo;
    }

    private void mover(String direccion) {
        if (controlador.isJuegoTerminado()){
            return;
        }
        controlador.moverExplorador(direccion, 1);
        actualizarPosicionExplorador();
        // actualizar historial con posicion y vida actual
        areaEstado.appendText("> " + controlador.getExplorador().getNombre() +
                " en (" + controlador.getExplorador().getPosicionX() + ", " +
                controlador.getExplorador().getPosicionY() + ") | Vida: " +
                controlador.getExplorador().getVida() + "\n"
        );
        // si se ha cumplido la condición se acaba el juego
        if (controlador.isJuegoTerminado()){
            mostrarPantallaFinDelJuego(controlador.getMensajeFinal());
        }
    }

    private void actualizarPosicionExplorador() {
        int x = controlador.getExplorador().getPosicionX();
        int y = controlador.getExplorador().getPosicionY();

        // Se calcula la posición gráfica respetando el centro del mapa
        exploradorGrafico.setLayoutX(OFFSET_X + (x * TAMANO_CELDA) + 2);
        exploradorGrafico.setLayoutY(OFFSET_Y + (y * TAMANO_CELDA) + 2);
    }

    private void mostrarInventario() {
        areaEstado.appendText("\nINVENTARIO\n");
        for (Inventariable item : controlador.getInventario().getItems()) {
            areaEstado.appendText("• " + item.toString() + "\n");
        }
        areaEstado.appendText("...................................\n\n");
    }

    private void mostrarPantallaFinDelJuego(String mensaje){
        areaEstado.appendText("\n................................\n");
        areaEstado.appendText(mensaje + "\n");
        areaEstado.appendText("\n.................................\n");
        boolean esGanador = mensaje.contains("VICTORIA");
        Alert alert = new Alert(esGanador ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(esGanador ? "¡Nivel completado!" : "Juego terminado");
        alert.setHeaderText(esGanador ? "¡Felicidades Exporador!" : "Has muerto en la aventura");
        alert.setContentText(mensaje + "\n\nLa partida ha finalizado.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}