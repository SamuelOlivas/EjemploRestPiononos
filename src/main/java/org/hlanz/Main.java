package org.hlanz;

public class Main {
    void main(){
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     API REST DE PASTELES - SERVIDOR INICIADO          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Servidor Tomcat ejecutándose en: http://localhost:8080");

        // Mantener el programa en ejecución
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("Servidor detenido");
        }
    }
}
