package org.example.practica9.Vista;

import org.example.practica9.Controlador.JuegoControlador;

import java.util.Scanner;

public class JuegoVista {
    private JuegoControlador controlador;
    private Scanner sc;

    public JuegoVista(){
        controlador = new JuegoControlador();
        sc = new Scanner(System.in);
    }

    public void iniciar(){
        controlador.iniciarJuego();
        int opc;
        do{
            System.out.println("\nExplorador");
            System.out.println("1. Mostrar nivel");
            System.out.println("2. Mostrar inventario");
            System.out.println("3. Mover explorador");
            System.out.println("4. Salir");
            System.out.println("Seleccione una opción");
            opc = sc.nextInt();
            switch (opc){
                case 1:
                    controlador.mostrarNivel();
                    break;
                case 2:
                    controlador.mostrarInventario();
                    break;
                case 3:
                    System.out.println("Dirección (norte, sur, este, oeste): ");
                    String direccion = sc.next();
                    System.out.println("Distancia: ");
                    int distancia = sc.nextInt();
                    controlador.moverExplorador(direccion, distancia);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no valida");

            }
        } while(opc != 4);
    }
}
