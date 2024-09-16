import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        method method = new method();
        while (true) {
            String[] options = {"Crear Proyecto", "Asignar Tarea", "Completar Tarea", "Listar Proyectos", "Guardar Proyectos", "Cargar Proyectos", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, "Seleceione el gruopo de instrumetos",
                    "Opciones", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    options, options[0]);


            switch (opcion) {
                case 0 -> method.crearProyecto();
                case 1 -> method.asignarTarea();
                case 2 -> method.completarTarea();
                case 3 -> method.listarProyectos();
                case 4 -> method.guaradarProyectos();
                case 5 -> method.cargarProyectos();
                case 6 -> System.exit(0);
                default -> JOptionPane.showMessageDialog(null, "Opción no válida.");
            }
        }

    }
    }
