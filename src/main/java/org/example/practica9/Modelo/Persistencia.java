package org.example.practica9.Modelo;

import org.example.practica9.Controlador.JuegoControlador;

import java.io.*;

public class Persistencia {
    private static final String ARCHIVO_CONFIGURACION = "estado_juego.txt";

    public static boolean existeArchivoGuardado() {
        File archivo = new File(ARCHIVO_CONFIGURACION);
        return archivo.exists() && archivo.length() > 0;
    }

    // guardar la configuración actual del juego en el archivo de texto
    public static void guardarEstadoJuego(Personaje explorador, Nivel nivel) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_CONFIGURACION))) {
            // guarda los datos del explorador posicionado en el ultimo checkpoint que activo
            writer.write("Explorador; " + explorador.getNombre() + ";" + explorador.getVida() + ";" + explorador.getReaX() + ";" + explorador.getReaY() + ";" + explorador.getReaX() + ";" + explorador.getReaY() + ";" + explorador.isTieneCheckpointActivo());
            writer.newLine();

            //guardar inventariables (armas y recompenzas)
            for (Inventariable item : explorador.getInventario().getItems()) {
                if (item instanceof Arma) {
                    Arma arma = (Arma) item;
                    writer.write("Arma;" + arma.getNombre() + ";" + arma.getDano() + ";" + arma.getAlcance());
                    writer.newLine();
                } else if (item instanceof Recompensa) {
                    Recompensa recompensa = (Recompensa) item;
                    writer.write("Recompensa;" + recompensa.getNombre() + ";" + recompensa.getValor() + ";" + recompensa.getTipo());
                    writer.newLine();
                }
            }
            // guardar checkpoints
            for (CheckPoint checkPoint : nivel.getCheckPoints()) {
                writer.write("Checkpoint;" + checkPoint.getNombre() + ";" + checkPoint.getPosicionX() + ";" + checkPoint.getPosicionY() + ";" + checkPoint.isActivado());
                writer.newLine();
            }

            // guardar obstaculos
            for (Obtaculo obtaculo : nivel.getObtaculos()) {
                writer.write("Obstaculo;" + obtaculo.getNombre() + ";" + obtaculo.getDano() + ";" + obtaculo.getPosicionX() + ";" + obtaculo.getPosicionY() + ";" + obtaculo.isDestruido());
                writer.newLine();
            }

            //guardar utilerias
            for (ElementoDinamico elementoDinamico : nivel.getElementosDinamicos()) {
                if (elementoDinamico instanceof Utileria) {
                    Utileria utileria = (Utileria) elementoDinamico;
                    writer.write("Utileria;" + utileria.getNombre() + ";" + utileria.getDescripcion() + ";" + utileria.getPosicionX() + ";" + utileria.getPosicionY() + ";" + utileria.isUsada());
                    writer.newLine();
                }
            }
            System.out.println("Estado del juego guardado en: " + ARCHIVO_CONFIGURACION);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo" + e.getMessage());
        }
    }

    // leer el archivo de la configuracion y reconstruir el nivel guardado
    public static boolean cargarEstadoJuego (JuegoControlador controlador){
        if (!existeArchivoGuardado()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_CONFIGURACION))) {
            Inventario inventario1 = new Inventario(10);
            Nivel nivel = new Nivel("Bosque verde", 1, "Fácil", inventario1);
            Personaje explorador = null;
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                String tipo = datos[0];
                switch (tipo) {
                    case "Explorador":
                        String nombre = datos[1];
                        int vida = Integer.parseInt(datos[2]);
                        int posX = Integer.parseInt(datos[3]);
                        int posY = Integer.parseInt(datos[4]);
                        int reaX = Integer.parseInt(datos[5]);
                        int reaY = Integer.parseInt(datos[6]);
                        boolean tieneCP = Boolean.parseBoolean(datos[7]);
                        explorador = new Personaje(nombre, vida, posX, posY, inventario1);
                        explorador.restaurarDatosCheckpoint(reaX, reaY, tieneCP);
                        nivel.agregarElementoDinamico(explorador);
                        break;
                    case "Arma":
                        inventario1.agregarItem(new Arma(datos[1], Integer.parseInt(datos[2]), Double.parseDouble(datos[3])));
                        break;
                    case "Recompensa":
                        inventario1.agregarItem(new Recompensa(datos[1], Integer.parseInt(datos[2]), datos[3]));
                        break;
                    case "Checkpoint":
                        CheckPoint checkPoint = new CheckPoint(datos[1], Integer.parseInt(datos[2]), Integer.parseInt(datos[3]));
                        checkPoint.setActivado(Boolean.parseBoolean(datos[4]));
                        nivel.agregarCheckPoint(checkPoint);
                        break;
                    case "Obstaculo":
                        Obtaculo obtaculo = new Obtaculo(datos[1], Integer.parseInt(datos[2]), Integer.parseInt(datos[3]), Integer.parseInt(datos[4]));
                        obtaculo.setDestruido(Boolean.parseBoolean(datos[5]));
                        nivel.agregarObstaculo(obtaculo);
                        break;
                    case "Utileria":
                        Utileria utileria = new Utileria(datos[1], datos[2], Integer.parseInt(datos[3]), Integer.parseInt(datos[4]));
                        utileria.setUsada(Boolean.parseBoolean(datos[5]));
                        nivel.agregarElementoDinamico(utileria);
                        break;
                }
            }
            controlador.setEstadoCargado(explorador, nivel);
            System.out.println("Configuración leida y restaurada desde " + ARCHIVO_CONFIGURACION);
            return true;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo" + e.getMessage());
            return false;
        }
    }

    // eliminar archivo
    public static void eliminarArchivoGuardado(){
        File archivo = new File(ARCHIVO_CONFIGURACION);
        if (archivo.exists()){
            archivo.delete();
        }
    }
}