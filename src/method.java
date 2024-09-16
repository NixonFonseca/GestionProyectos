import org.w3c.dom.Text;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class method {

    private List<Proyecto> listaProyectos = new ArrayList<>();
    private List<Tarea> listaTareas = new ArrayList<>();
    private int idTareaCounter = 1;

    public String Text(String texto) {
        return JOptionPane.showInputDialog(texto);
    }

    public void crearProyecto() {
        String Proyec = Text("Nombre Proyecto: ");
        for (Proyecto proyecto : listaProyectos) {
            if (proyecto.name().equalsIgnoreCase(Proyec)) {

                JOptionPane.showMessageDialog(null, "Proyecto existente");
                return;
            }
        }
        int id = Integer.parseInt(Text("id del proyecto: "));
        String name = Text("Ingrese el nombre del proyecto: ");
        String tareasStr = Text("Ingrese las tareas (separadas por comas): ");
        List<String> listarTareas = Arrays.asList(tareasStr.split("\\s*,\\s*"));
        String lider = Text("Ingrese el nombre del lider del proyecto: ");
        Proyecto proyectos = new Proyecto(id, name, listarTareas, lider);
        listaProyectos.add(proyectos);
        JOptionPane.showMessageDialog(null, "Se agrego el Proyeto correctamente");
    }

    public void asignarTarea() {
        String idTareaStr = Text("Ingrese el ID de la tarea a asignar: ");
        int idTarea;
        try {
            idTarea = Integer.parseInt(idTareaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de la tarea no válido. Debe ser un número.");
            return;
        }


        Optional<Tarea> tareaOpt = listaTareas.stream()
                .filter(t -> t.id() == idTarea)
                .findFirst();

        if (tareaOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tarea no encontrada.");
            return;
        }


        JOptionPane.showMessageDialog(null, "Tarea asignada correctamente al proyecto.");
    }

    public void agregarTarea() {
        String Proyec = Text("id de la Tarea: ");
        int idTarea;

        try {
            idTarea = Integer.parseInt(Proyec);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ID de la tarea no válido. Debe ser un número.");
            return;
        }

        Optional<Tarea> tareas = listaTareas.stream()
                .filter(t -> t.id() == idTarea)
                .findFirst();
        if (tareas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Proyecto existente");
            return;

        }
        int id = Integer.parseInt(Text("id de la tarea: "));
        String descripcion = Text("Ingrese la descripción de la tarea: ");
        String estado = Text("Ingrese el estado de la tarea (Pendiente, Completada): ");
        String empleado = Text("Ingrese el nombre del empleado asignado: ");
        Tarea nuevaTarea = new Tarea(id, descripcion, estado, empleado);
        listaTareas.add(nuevaTarea);
        JOptionPane.showMessageDialog(null, "Tarea asignada correctamente");
    }

    public void completarTarea() {
        String idTareaStr = JOptionPane.showInputDialog("Ingrese el ID de la tarea a completar: ");
        int idTarea;
        try {
            idTarea = Integer.parseInt(idTareaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de tarea no válido. Debe ser un número.");
            return;
        }
        Optional<Tarea> tareas = listaTareas.stream()
                .filter(t -> t.id() == idTarea)
                .findFirst();
        if (tareas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Tarea no encontrada.");
            return;
        }

        Tarea tarea = tareas.get();
        Tarea tareaCompleta = new Tarea(tarea.id(), tarea.descripcion(), "Completada", tarea.EmpeladoAsig());
        listaTareas = listaTareas.stream()
                .map(t -> t.id() == idTarea ? tareaCompleta : t)
                .collect(Collectors.toList());

        JOptionPane.showMessageDialog(null, "Tarea marcada como completada.");
    }

    public void listarProyectos() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de los proyectos:\n");
        for (Proyecto listProyect : listaProyectos) {
            sb.append(" ID: ").append(listProyect.id()).append("\n");
            sb.append("  name: ").append(listProyect.name()).append(")\n");
            sb.append(" lista Tareas: ").append(listProyect.listTareas()).append("\n");
            sb.append("  Lider Equipo: ").append(listProyect.liderEquipo()).append("\n");
            sb.append("--------------------------------------------------\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void guaradarProyectos() {
        String nombreArchivo = JOptionPane.showInputDialog("Ingrese el nombre del archivo para guardar los proyectos:");
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nombre de archivo no válido.");
            return;
        }

        try {

            List<String> datosProyectos = listaProyectos.stream()
                    .map(proyecto -> String.format("%d,%s,%s,%s",
                            proyecto.id(),
                            proyecto.name(),
                            String.join(";", proyecto.listTareas()),
                            proyecto.liderEquipo()))
                    .collect(Collectors.toList());


            exportarCSV(nombreArchivo, datosProyectos);
            JOptionPane.showMessageDialog(null, "Proyectos guardados correctamente en " + nombreArchivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los proyectos: " + e.getMessage());
        }
    }

    public void cargarProyectos() {
        String nombreArchivo = JOptionPane.showInputDialog("Ingrese el nombre del archivo para cargar los proyectos:");
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nombre de archivo no válido.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            listaProyectos.clear();
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length < 4) {
                    continue;
                }
                int id = Integer.parseInt(datos[0]);
                String name = datos[1];
                List<String> listTareas = List.of(datos[2].split(";"));
                String liderEquipo = datos[3];
                Proyecto proyecto = new Proyecto(id, name, listTareas, liderEquipo);
                listaProyectos.add(proyecto);
            }


            StringBuilder sb = new StringBuilder("Proyectos cargados:\n\n");
            for (Proyecto proyecto : listaProyectos) {
                sb.append(String.format("ID: %d\nNombre: %s\nTareas: %s\nLíder: %s\n\n",
                        proyecto.id(),
                        proyecto.name(),
                        String.join(", ", proyecto.listTareas()),
                        proyecto.liderEquipo()));
            }

            JOptionPane.showMessageDialog(null, sb.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los proyectos: " + e.getMessage());
        }
    }

    public void exportarCSV(String nombreArchivo, List<String> datos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (String dato : datos) {
                writer.write(dato);
                writer.newLine();
            }
        }
    }
}
