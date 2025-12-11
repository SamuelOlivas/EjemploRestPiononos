package org.hlanz.mensaje;

import org.hlanz.entity.Pastel;

import java.util.List;
import java.util.Locale;

public class JsonUtil {
    public static String pastelToJson(Pastel p) {
        return String.format(Locale.US,
                "{\"id\":%d,\"nombre\":\"%s\",\"sabor\":\"%s\",\"precio\":%.2f,\"porciones\":%d}",
                p.getId(), p.getNombre(), p.getSabor(), p.getPrecio(), p.getPorciones()
        );
    }

    public static String pastelesListToJson(List<Pastel> pasteles) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < pasteles.size(); i++) {
            json.append(pastelToJson(pasteles.get(i)));
            if (i < pasteles.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    // Convertir JSON simple a objeto Pastel (sin librerías externas)
    public static Pastel jsonToPastel(String json) {
        Pastel pastel = new Pastel();

        // Extraer valores del JSON de forma simple
        pastel.setNombre(extraerValor(json, "nombre"));
        pastel.setSabor(extraerValor(json, "sabor"));

        String precioStr = extraerValor(json, "precio");
        if (precioStr != null) {
            pastel.setPrecio(Double.parseDouble(precioStr));
        }

        String porcionesStr = extraerValor(json, "porciones");
        if (porcionesStr != null) {
            pastel.setPorciones(Integer.parseInt(porcionesStr));
        }

        return pastel;
    }

    private static String extraerValor(String json, String clave) {
        String patron = "\"" + clave + "\"";
        int inicio = json.indexOf(patron);
        if (inicio == -1) return null;

        inicio = json.indexOf(":", inicio) + 1;
        int fin;

        json = json.substring(inicio).trim();

        if (json.startsWith("\"")) {
            inicio = 1;
            fin = json.indexOf("\"", inicio);
            return json.substring(inicio, fin);
        } else {
            fin = json.indexOf(",");
            if (fin == -1) fin = json.indexOf("}");
            return json.substring(0, fin).trim();
        }
    }
}
