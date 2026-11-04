package org.example.practica9.Vista;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.practica9.Controlador.JuegoControlador;
import org.example.practica9.Modelo.Inventariable;


public class JuegoVistaFX extends Application{
    private JuegoControlador controlador;
    private Pane mapa;
    private StackPane exploradorGrafico;
    private TextArea areaEstado;

    private final int TAMANO_CELDA = 35;

    public JuegoVistaFX() {
        super();
    }

    @Override
    public void start(Stage primaryStage) {
        controlador = new JuegoControlador();
        controlador.iniciarJuego();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1e1e2e;"); // Fondo oscuro tipo juego

        // --- CABECERA / TÍTULO ---
        HBox header = new HBox();
        header.setPadding(new Insets(0, 0, 15, 0));
        header.setAlignment(Pos.CENTER);
        Label titulo = new Label("EXPLORADOR");
        titulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#f5e0dc"));
        header.getChildren().add(titulo);
        root.setTop(header);

        // --- CENTRO: ÁREA DEL MAPA (GRID RETICULADO) ---
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
        dibujarObstaculosYCheckpoints();

        // Personaje mejorado: Círculo con borde y etiqueta "E"
        exploradorGrafico = crearAvatarPersonaje();
        actualizarPosicionGrafica(
                controlador.getExplorador().getPosicionX(),
                controlador.getExplorador().getPosicionY()
        );
        mapa.getChildren().add(exploradorGrafico);

        root.setCenter(mapa);

        // --- PANEL DERECHO: CONTROLES Y CONSOLA DE EVENTOS ---
        VBox panelDerecho = new VBox(15);
        panelDerecho.setPadding(new Insets(0, 0, 0, 15));
        panelDerecho.setPrefWidth(240);

        // Control Pad (Cruceta de dirección)
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

        // Botón de Inventario
        Button btnInventario = new Button("Ver Inventario");
        btnInventario.setMaxWidth(Double.MAX_VALUE);
        btnInventario.setStyle(
                "-fx-background-color: #89b4fa; -fx-text-fill: #11111b; " +
                        "-fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 6;"
        );
        btnInventario.setOnAction(e -> mostrarInventario());

        // Consola de Texto/Eventos
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
        areaEstado.setText("¡Aventura iniciada!\nUtiliza la cruceta para moverte.\n");

        panelDerecho.getChildren().addAll(boxMovimiento, btnInventario, areaEstado);
        root.setRight(panelDerecho);

        Scene scene = new Scene(root, 730, 520);
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
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #585b70; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-background-radius: 6;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #45475a; -fx-text-fill: #cdd6f4; -fx-font-weight: bold; -fx-background-radius: 6;"));
        return btn;
    }

    private StackPane crearAvatarPersonaje() {
        Circle cuerpo = new Circle(14, Color.web("#a6e3a1"));
        cuerpo.setStroke(Color.web("#40a02b"));
        cuerpo.setStrokeWidth(2);

        Text inicial = new Text("E");
        inicial.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        inicial.setFill(Color.web("#11111b"));

        StackPane avatar = new StackPane(cuerpo, inicial);
        avatar.setEffect(new DropShadow(8, Color.web("#a6e3a1")));
        return avatar;
    }

    private void dibujarCuadriculaFondo() {
        // Lineas tenue para formar la rejilla del mapa
        for (int i = 0; i <= 450; i += TAMANO_CELDA) {
            Rectangle lineaH = new Rectangle(450, 1, Color.web("#313244"));
            lineaH.setY(i);
            Rectangle lineaV = new Rectangle(1, 450, Color.web("#313244"));
            lineaV.setX(i);
            mapa.getChildren().addAll(lineaH, lineaV);
        }
    }

    private void dibujarObstaculosYCheckpoints() {
        // Obstáculo Roca (Cuadrado con estilo)
        StackPane rocaNode = crearElementoMapa("🪨", Color.web("#f38ba8"), 3, 5);
        // Checkpoint Fogata (Círculo con estilo)
        StackPane fogataNode = crearElementoMapa("🔥", Color.web("#fab387"), 10, 10);

        mapa.getChildren().addAll(rocaNode, fogataNode);
    }

    private StackPane crearElementoMapa(String emoji, Color colorBorde, int gridX, int gridY) {
        Rectangle fondo = new Rectangle(28, 28, Color.web("#313244"));
        fondo.setArcWidth(6);
        fondo.setArcHeight(6);
        fondo.setStroke(colorBorde);
        fondo.setStrokeWidth(1.5);

        Text txt = new Text(emoji);
        txt.setFont(Font.font(14));

        StackPane nodo = new StackPane(fondo, txt);
        nodo.setLayoutX(10 + (gridX * TAMANO_CELDA));
        nodo.setLayoutY(10 + (gridY * TAMANO_CELDA));
        return nodo;
    }

    private void mover(String direccion) {
        int distancia = 1;
        controlador.moverExplorador(direccion, distancia);

        actualizarPosicionGrafica(
                controlador.getExplorador().getPosicionX(),
                controlador.getExplorador().getPosicionY()
        );

        areaEstado.appendText("> Movido a (" +
                controlador.getExplorador().getPosicionX() + ", " +
                controlador.getExplorador().getPosicionY() + ")\n"
        );
    }

    private void actualizarPosicionGrafica(int x, int y) {
        exploradorGrafico.setLayoutX(10 + (x * TAMANO_CELDA));
        exploradorGrafico.setLayoutY(10 + (y * TAMANO_CELDA));
    }

    private void mostrarInventario() {
        areaEstado.appendText("\n=== INVENTARIO ===\n");
        for (Inventariable item : controlador.getInventario().getItems()) {
            areaEstado.appendText("• " + item.toString() + "\n");
        }
        areaEstado.appendText("===================\n\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}