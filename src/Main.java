import java.util.List;
import java.util.Map;

/**
 * Clase principal de ejecución para la plataforma TecnoMóvil Data.
 * Nivel: 4to Semestre - Tecnología en Desarrollo de Software.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("========================================================");
        System.out.println("   TECNOMÓVIL DATA - MÓDULO ANALÍTICO DE TRANSPORTE     ");
        System.out.println("========================================================\n");

        // 1. Generar dataset de prueba (100 registros simulación)
        int cantidadRegistros = 100;
        List<RegistroTransporte> registros = GeneradorDatos.generar(cantidadRegistros);
        System.out.println(">>> Se han generado " + registros.size() + " registros sintéticos para el análisis.\n");


        // a) Cálculo de afluencia por estación (solo entradas)
        System.out.println("--- a) AFLUENCIA POR ESTACIÓN (Ingresos) ---");
        Map<String, Long> afluencia = ProcesadorTransporte.afluenciaPorEstacion(registros);
        afluencia.forEach((estacion, total) -> 
            System.out.println("  Estación " + estacion + ": " + total + " pasajeros")
        );
        System.out.println();


        // b) Hora de mayor afluencia (Hora Pico)
        System.out.println("--- b) HORA PICO / MAYOR AFLUENCIA ---");
        int horaPico = ProcesadorTransporte.horaDeMayorAfluencia(registros);
        if (horaPico != -1) {
            System.out.println("  La hora con mayor movimiento del día es las: " + horaPico + ":00 hrs");
        } else {
            System.out.println("  No hay datos registrados.");
        }
        System.out.println();


        // c) Rutas más utilizadas
        System.out.println("--- c) RUTAS MÁS UTILIZADAS (Orden Descendente) ---");
        List<Map.Entry<String, Long>> rutasOrdenadas = ProcesadorTransporte.rutasMasUtilizadas(registros);
        rutasOrdenadas.forEach(entrada -> 
            System.out.println("  Ruta " + entrada.getKey() + ": " + entrada.getValue() + " eventos")
        );
        System.out.println();


        // d) Patrones de viaje por usuario
        System.out.println("--- d) PATRONES DE VIAJE POR USUARIO (Muestra de usuarios) ---");
        Map<String, List<String>> patrones = ProcesadorTransporte.patronesPorUsuario(registros);
        patrones.entrySet().stream().limit(5).forEach(entrada -> 
            System.out.println("  Usuario " + entrada.getKey() + " -> Recorrido: " + entrada.getValue())
        );
        System.out.println();


        // e) Tiempo promedio entre estaciones (en minutos)
        System.out.println("--- e) TIEMPO PROMEDIO ENTRE ESTACIONES (Por usuario) ---");
        Map<String, Double> tiemposPromedio = ProcesadorTransporte.tiempoPromedioEntreEstaciones(registros);
        tiemposPromedio.entrySet().stream().limit(5).forEach(entrada -> 
            System.out.printf("  Usuario %s: %.2f minutos promedio entre validaciones\n", entrada.getKey(), entrada.getValue())
        );
        System.out.println();


        // f) Detección de sobrecarga en rutas (con umbral simulado de 18 eventos)
        long umbralSobreocupacion = 18;
        System.out.println("--- f) DETECCIÓN DE SOBRECARGA EN RUTAS (Umbral: " + umbralSobreocupacion + " eventos) ---");
        List<String> reporteSobrecarga = ProcesadorTransporte.deteccionSobrecarga(registros, umbralSobreocupacion);
        reporteSobrecarga.forEach(linea -> System.out.println("  " + linea));

        System.out.println("\n========================================================");
        System.out.println("   PROCESAMIENTO FUNCIONAL FINALIZADO CON ÉXITO        ");
        System.out.println("========================================================");
    }
}