# TecnoMóvil Data - Módulo Analítico Funcional

Módulo de procesamiento y agregación declarativa de datos de movilidad urbana para el sistema de transporte de la ciudad de TecnoValle. Proyecto desarrollado para la asignatura de Programación Avanzada / Paradigmas de Programación (Unidad 2) de la Institución Universitaria Digital de Antioquia.

---

## Contexto del Proyecto

El sistema tradicional de TecnoValle procesaba entre 6 y 12 millones de registros diarios mediante programación imperativa secuencial, lo que ocasionaba:
* Tiempos de procesamiento de hasta 2 horas por lote diario.
* Vulnerabilidades por condiciones de carrera (race conditions) debido al acceso mutable a datos compartidos.
* Alto costo de mantenimiento por código acoplado e imperativo.

TecnoMóvil Data reingeniería el módulo de analítica migrando a un enfoque declarativo y funcional en Java 8+, utilizando objetos inmutables, la API de Streams y expresiones Lambda para posibilitar análisis en tiempo real y ejecución en paralelo segura.

---

## Requerimientos e Implementación Funcional

El sistema resuelve 6 métricas analíticas de negocio mediante pipelines de Streams sin efectos secundarios:

| Requerimiento | Descripción | Método en ProcesadorTransporte | Operadores Funcionales |
|---|---|---|---|
| a) Afluencia por Estación | Conteo de entradas de usuarios por estación. | afluenciaPorEstacion | filter, groupingBy, counting |
| b) Identificación de Hora Pico | Hora del día con mayor volumen de movimiento. | horaDeMayorAfluencia | groupingBy, max, Comparator |
| c) Rutas Más Utilizadas | Clasificación de rutas de mayor a menor uso. | rutasMasUtilizadas | groupingBy, counting, sort |
| d) Patrones de Viaje | Secuencia cronológica de estaciones por usuario. | patronesPorUsuario | groupingBy, sorted, map, collect |
| e) Tiempo Promedio | Duración promedio (min) entre validaciones por usuario. | tiempoPromedioEntreEstaciones | IntStream.range, Duration.between, average |
| f) Detección de Sobrecarga | Marcado booleano de rutas que superan el umbral crítico. | deteccionSobrecarga | stream, map (transformación condicional) |

---

## Principios Funcionales Aplicados

1. Inmutabilidad: La clase RegistroTransporte encapsula campos private final sin métodos modificadores (setters). Los registros de origen nunca cambian.
2. Funciones Puras: Métodos estáticos sin estado mutable compartido. Un mismo set de datos produce siempre exactamente el mismo resultado.
3. Colecciones Declarativas: Uso de Collectors.groupingBy, Collectors.toMap y Collectors.toList para transformar datos sin recurrir a bucles tradicionales (for/while).
4. Soporte Concurrente: El diseño sin efectos secundarios permite alternar a .parallelStream() en datasets masivos sin necesidad de bloques de sincronización manual (synchronized).

---

## Estructura del Proyecto

```text
src/
├── RegistroTransporte.java   # Modelo de datos inmutable (DTO)
├── GeneradorDatos.java       # Generación sintética determinista (Random Seed 42)
├── ProcesadorTransporte.java # Capa analítica funcional (Streams & Lambdas)
└── Main.java                 # Clase principal de ejecución y renderizado
```

---

## Compilación y Ejecución

### Requisitos previos
* JDK 8 o superior instalado.
* Consola de comandos / Terminal.

### Ejecución desde consola con Bash

```bash
# 1. Clonar el repositorio
git clone https://github.com/luisagarciaal/Sistema_gestion_transporte_urbano
# 2. Entrar al directorio del proyecto
cd Sistema_gestion_transporte_urbano

# 3. Compilar todos los archivos Java hacia la carpeta de salida 'out'
javac *.java -d out

# 4. Ejecutar la clase principal Main
java -cp out Main
```

### Ejecución desde consola con PowerShell / Command Prompt

```powershell
# 1. Compilar los archivos Java en el directorio 'bin'
javac -d bin *.java

# 2. Ejecutar la clase Main desde 'bin'
java -cp bin Main
```

---
Institución Universitaria Digital de Antioquia  
Tecnología en Desarrollo de Software - 2026