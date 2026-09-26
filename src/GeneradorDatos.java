import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class GeneradorDatos {

    static String[] rutas = {"R1", "R2", "R3", "R4", "R5"};
    static String[] estaciones = {"E1", "E2", "E3", "E4", "E5", "E6"};
    static String[] usuarios = {"U1", "U2", "U3", "U4", "U5", "U6", "U7", "U8", "U9", "U10"};

    public static List<RegistroTransporte> generar(int cantidad) {
        Random random = new Random(42); // semilla fija para que siempre salga igual
        List<RegistroTransporte> lista = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            String usuario = usuarios[random.nextInt(usuarios.length)];
            String ruta = rutas[random.nextInt(rutas.length)];
            String estacion = estaciones[random.nextInt(estaciones.length)];
            String accion = random.nextBoolean() ? "entrada" : "salida";

            int hora = 5 + random.nextInt(18); // entre las 5am y las 10pm
            int minuto = random.nextInt(60);
            LocalDateTime fecha = LocalDateTime.of(2026, 9, 21, hora, minuto).plusSeconds(i);

            lista.add(new RegistroTransporte(usuario, ruta, estacion, accion, fecha));
        }

        // devolvemos la lista como inmutable para que nadie la pueda modificar despues
        return Collections.unmodifiableList(lista);
    }
}