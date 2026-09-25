import java.time.LocalDateTime;

// Esta clase representa un solo evento del sistema de transporte.
// Los datos no se pueden cambiar despues de crear el objeto (son "final"),
// eso es lo que en el curso llaman inmutabilidad.
public class RegistroTransporte {

    private final String idUsuario;
    private final String ruta;
    private final String estacion;
    private final String accion; // puede ser "entrada" o "salida"
    private final LocalDateTime timestamp;

    public RegistroTransporte(String idUsuario, String ruta, String estacion,
                               String accion, LocalDateTime timestamp) {
        this.idUsuario = idUsuario;
        this.ruta = ruta;
        this.estacion = estacion;
        this.accion = accion;
        this.timestamp = timestamp;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getRuta() {
        return ruta;
    }

    public String getEstacion() {
        return estacion;
    }

    public String getAccion() {
        return accion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}