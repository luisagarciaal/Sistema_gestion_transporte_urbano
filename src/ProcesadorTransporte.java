import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ProcesadorTransporte {

    //  Cuantos usuarios entraron a cada estacion
    public static Map<String, Long> afluenciaPorEstacion(List<RegistroTransporte> registros) {

        // primero nos quedamos solo con los registros de "entrada"
        List<RegistroTransporte> soloEntradas = registros.stream()
                .filter(r -> r.getAccion().equals("entrada"))
                .collect(Collectors.toList());

        // despues agrupamos por estacion y contamos cuantos hay en cada una
        Map<String, Long> conteo = soloEntradas.stream()
                .collect(Collectors.groupingBy(RegistroTransporte::getEstacion, Collectors.counting()));

        return conteo;
    }

    //  que hora del dia hay mas movimiento
    public static Map<Integer, Long> horasPico(List<RegistroTransporte> registros) {
        Map<Integer, Long> conteoPorHora = registros.stream()
                .collect(Collectors.groupingBy(r -> r.getTimestamp().getHour(), Collectors.counting()));

        return conteoPorHora;
    }

    public static int horaDeMayorAfluencia(List<RegistroTransporte> registros) {
        Map<Integer, Long> horas = horasPico(registros);

        Map.Entry<Integer, Long> maxEntry = horas.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .orElse(null);

        if (maxEntry == null) {
            return -1;
        }
        return maxEntry.getKey();
    }

    //  Que rutas se usan mas (de mayor a menor)
    public static List<Map.Entry<String, Long>> rutasMasUtilizadas(List<RegistroTransporte> registros) {

        Map<String, Long> conteoPorRuta = registros.stream()
                .collect(Collectors.groupingBy(RegistroTransporte::getRuta, Collectors.counting()));

        // pasamos el mapa a una lista para poder ordenarla de mayor a menor
        List<Map.Entry<String, Long>> listaOrdenada = new ArrayList<>(conteoPorRuta.entrySet());
        listaOrdenada.sort((entradaA, entradaB) -> Long.compare(entradaB.getValue(), entradaA.getValue()));

        return listaOrdenada;
    }

    //  Por donde se mueve cada usuario, en orden
    public static Map<String, List<String>> patronesPorUsuario(List<RegistroTransporte> registros) {

        Map<String, List<RegistroTransporte>> registrosPorUsuario = registros.stream()
                .collect(Collectors.groupingBy(RegistroTransporte::getIdUsuario));

        Map<String, List<String>> resultado = registrosPorUsuario.entrySet().stream()
                .collect(Collectors.toMap(
                        entrada -> entrada.getKey(),
                        entrada -> entrada.getValue().stream()
                                .sorted(Comparator.comparing(RegistroTransporte::getTimestamp))
                                .map(RegistroTransporte::getEstacion)
                                .collect(Collectors.toList())
                ));

        return resultado;
    }

    // Cuanto tiempo pasa en promedio entre un evento y el siguiente, por usuario
    public static Map<String, Double> tiempoPromedioEntreEstaciones(List<RegistroTransporte> registros) {

        Map<String, List<RegistroTransporte>> registrosPorUsuario = registros.stream()
                .collect(Collectors.groupingBy(RegistroTransporte::getIdUsuario));

        Map<String, Double> resultado = registrosPorUsuario.entrySet().stream()
                .collect(Collectors.toMap(
                        entrada -> entrada.getKey(),
                        entrada -> {
                            List<LocalDateTime> fechas = entrada.getValue().stream()
                                    .map(RegistroTransporte::getTimestamp)
                                    .sorted()
                                    .collect(Collectors.toList());

                            if (fechas.size() < 2) {
                                return 0.0;
                            }

                            return IntStream.range(1, fechas.size())
                                    .mapToLong(i -> Duration.between(fechas.get(i - 1), fechas.get(i)).toMinutes())
                                    .average()
                                    .orElse(0.0);
                        }
                ));

        return resultado;
    }

    //  Marcar como "critica" cualquier ruta que pase de cierto numero de eventos
    public static List<String> deteccionSobrecarga(List<RegistroTransporte> registros, long umbral) {

        List<Map.Entry<String, Long>> rutas = rutasMasUtilizadas(registros);

        List<String> resultado = rutas.stream()
                .map(entrada -> {
                    String ruta = entrada.getKey();
                    long total = entrada.getValue();
                    boolean esCritica = total > umbral;
                    return ruta + " -> " + total + " eventos | critica: " + esCritica;
                })
                .collect(Collectors.toList());

        return resultado;
    }
}