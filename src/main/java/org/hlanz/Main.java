package org.hlanz;

import org.hlanz.repository.PastelRepository;
import org.hlanz.servlets.PastelService;

import java.util.Scanner;

public class Main {
    void main(){
        //Hacemos un objeto service como en acceso a datos
        PastelService service = new PastelService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("|||||||||||| API REST DE PASTELES - PRUEBA CON BASE DE DATOS ||||||||||||\n");

        /*
        Vamos a simular las peticiones HTTP que le hariamos a un servidor web

        Lo siguiente seria hacer esto con servlets
        (clase de Java que se ejecuta en un servidor web)
         */
        while (true) {
            mostrarMenu();
            int opcion = 0;

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes introducir un número válido.\n");
                continue;
            }

            System.out.println();

            switch (opcion) {
                case 0:
                    // Salir del programa
                    System.out.println("Cerrando conexión a la base de datos...");
                    PastelRepository.getInstance().cerrarConexion();
                    System.out.println("Chao Pescao!");
                    scanner.close();
                    return;
                case 1:
                    // 1. GET - Obtener todos los pasteles
                    System.out.println("1. GET /api/pasteles - Obtener todos los pasteles:");
                    System.out.println(formatJson(service.obtenerTodos()));
                    System.out.println("\n" + "=".repeat(60) + "\n");
                    break;
                case 2:
                    // 2. GET - Obtener un pastel específico
                    System.out.print("Introduce el ID del pastel: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    System.out.println("GET /api/pasteles/" + id + " - Obtener pastel con ID " + id + ":");
                    System.out.println(formatJson(service.obtenerPorId(id)));
                    System.out.println("\n" + "=".repeat(60) + "\n");
                    break;
                case 3:
                    // 3. POST - Crear un nuevo pastel
                    System.out.println("3. POST /api/pasteles - Crear nuevo pastel:");
                    System.out.println("\nIntroduce los datos del pastel:");

                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();

                    System.out.print("Sabor: ");
                    String sabor = scanner.nextLine();

                    System.out.print("Precio: ");
                    double precio = Double.parseDouble(scanner.nextLine());

                    System.out.print("Porciones: ");
                    int porciones = Integer.parseInt(scanner.nextLine());

                    String nuevoPastelJson = String.format(
                        "{\"nombre\":\"%s\",\"sabor\":\"%s\",\"precio\":%.2f,\"porciones\":%d}",
                        nombre, sabor, precio, porciones
                    );
                    System.out.println("\nBody enviado: " + formatJson(nuevoPastelJson));
                    String respuestaCrear = service.crear(nuevoPastelJson);
                    System.out.println("Respuesta: " + formatJson(respuestaCrear));
                    System.out.println("\n" + "=".repeat(60) + "\n");
                    break;
                case 4:
                    // 4. PUT - Actualizar pastel
                    System.out.print("Introduce el ID del pastel a actualizar: ");
                    Long idActualizar = Long.parseLong(scanner.nextLine());
                    System.out.println("PUT /api/pasteles/" + idActualizar + " - Actualizar pastel con ID " + idActualizar + ":");
                    System.out.println("\nIntroduce los nuevos datos del pastel:");

                    System.out.print("Nombre: ");
                    String nombreActualizar = scanner.nextLine();

                    System.out.print("Sabor: ");
                    String saborActualizar = scanner.nextLine();

                    System.out.print("Precio: ");
                    double precioActualizar = Double.parseDouble(scanner.nextLine());

                    System.out.print("Porciones: ");
                    int porcionesActualizar = Integer.parseInt(scanner.nextLine());

                    String actualizarJson = String.format(
                        "{\"nombre\":\"%s\",\"sabor\":\"%s\",\"precio\":%.2f,\"porciones\":%d}",
                        nombreActualizar, saborActualizar, precioActualizar, porcionesActualizar
                    );
                    System.out.println("\nBody enviado: " + formatJson(actualizarJson));
                    String respuestaActualizar = service.actualizar(idActualizar, actualizarJson);
                    System.out.println("Respuesta: " + formatJson(respuestaActualizar));
                    System.out.println("\n" + "=".repeat(60) + "\n");
                    break;
                case 5:
                    // 5. DELETE - Eliminar pastel
                    System.out.print("Introduce el ID del pastel a eliminar: ");
                    Long idEliminar = Long.parseLong(scanner.nextLine());
                    System.out.println("DELETE /api/pasteles/" + idEliminar + " - Eliminar pastel con ID " + idEliminar + ":");
                    String respuestaEliminar = service.eliminar(idEliminar);
                    System.out.println("Respuesta: " + formatJson(respuestaEliminar));
                    System.out.println("\n" + "=".repeat(60) + "\n");
                    break;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.\n");
            }
        }//fin while
    }// fin Main

    private void mostrarMenu() {
        System.out.println("========== MENÚ DE OPERACIONES ==========");
        System.out.println("1. GET - Obtener todos los pasteles");
        System.out.println("2. GET - Obtener un pastel por ID");
        System.out.println("3. POST - Crear nuevo pastel");
        System.out.println("4. PUT - Actualizar pastel existente");
        System.out.println("5. DELETE - Eliminar pastel");
        System.out.println("0. Salir del programa");
        System.out.println("==========================================");
        System.out.print("Selecciona una opción: ");
    }

    // Método auxiliar para formatear JSON (indentación simple)
    private String formatJson(String json) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        boolean inString = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{' || c == '[') {
                    formatted.append(c).append('\n');
                    indentLevel++;
                    formatted.append("  ".repeat(indentLevel));
                } else if (c == '}' || c == ']') {
                    formatted.append('\n');
                    indentLevel--;
                    formatted.append("  ".repeat(indentLevel));
                    formatted.append(c);
                } else if (c == ',') {
                    formatted.append(c).append('\n');
                    formatted.append("  ".repeat(indentLevel));
                } else if (c == ':') {
                    formatted.append(c).append(' ');
                } else if (c != ' ') {
                    formatted.append(c);
                }
            } else {
                formatted.append(c);
            }
        }
        return formatted.toString();
    }//Fin metodo auxiliar
}//fin clase
