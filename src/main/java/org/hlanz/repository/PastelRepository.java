package org.hlanz.repository;

import org.hlanz.entity.Pastel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class PastelRepository {
    private static PastelRepository instance;
    private Map<Long, Pastel> pasteles = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    /*
        Vamos a simular una BBDD con este repository
    */
    // Singleton
    //TAREA: CAMBIAR ESTO POR JDBC Y POSTGRES
    private PastelRepository() {
        // Datos de ejemplo
        crear(new Pastel(null, "Tres Leches", "Vainilla", 25.50, 12));
        crear(new Pastel(null, "Selva Negra", "Chocolate", 30.00, 10));
        crear(new Pastel(null, "Red Velvet", "Chocolate Rojo", 28.75, 14));
        crear(new Pastel(null, "Zanahoria", "Especias", 22.00, 8));
    }

    public static PastelRepository getInstance() {
        if (instance == null) {
            instance = new PastelRepository();
        }
        return instance;
    }

    public List<Pastel> obtenerTodos() {
        return new ArrayList<>(pasteles.values());
    }

    public Pastel obtenerPorId(Long id) {
        return pasteles.get(id);
    }

    public Pastel crear(Pastel pastel) {
        Long id = idGenerator.getAndIncrement();
        pastel.setId(id);
        pasteles.put(id, pastel);
        return pastel;
    }

    public Pastel actualizar(Long id, Pastel pastel) {
        if (pasteles.containsKey(id)) {
            pastel.setId(id);
            pasteles.put(id, pastel);
            return pastel;
        }
        return null;
    }

    public boolean eliminar(Long id) {
        return pasteles.remove(id) != null;
    }
}
