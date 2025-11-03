package appTareas;

import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.io.IOException;


/**
 * TareaInterface
 * --------------
 * Ventana principal con:
 * - Tabla de tareas (JTable + TareasTableModel)
 * - Campos de entrada (título y descripción)
 * - Botones "Añadir" y "Borrar"
 *
 * Flujo:
 *  - Escribe un título (y opcionalmente descripción) y pulsa "Añadir" -> aparece una fila nueva en la tabla.
 *  - Selecciona una fila y pulsa "Borrar" -> elimina esa tarea.
 *  - El checkbox "Completada" se marca/desmarca directamente en la tabla.
 */
public class Tareas_Interface extends JFrame {

    // --- Datos y modelo ---
    private final Tareas_Gestor repo;          // Tu repositorio (lista en memoria)
    private final TareasTableModel model;      // Puente entre repo y JTable

    // --- Componentes de UI ---
    private final JTable table;                // La tabla visible
    private final JTextField txtTitulo;        // Campo: título de la tarea
    private final JTextField txtDescripcion;   // Campo: descripción (opcional)
    private final JButton btnAñadir;           // Botón: añadir tarea
    private final JButton btnBorrar;           // Botón: borrar tarea seleccionada
    
  
    private static final java.nio.file.Path SAVE_DIR  = java.nio.file.Paths.get("datos");
    private static final java.nio.file.Path SAVE_FILE = SAVE_DIR.resolve("tareas.txt");

    public Tareas_Interface() {

        super("Gestor de tareas simple, creado por José Luis Escudero Polo"); // Título de la ventana
        
    	// Asegura la carpeta y crea el archivo vacío si no existe
    	try {
    	    java.nio.file.Files.createDirectories(SAVE_DIR);
    	    if (java.nio.file.Files.notExists(SAVE_FILE)) {
    	        java.nio.file.Files.createFile(SAVE_FILE);
    	    }
    	} catch (java.io.IOException ex) {
    	    JOptionPane.showMessageDialog(this,
    	        "No se pudo preparar la carpeta/archivo de datos:\n" + ex.getMessage(),
    	        "Aviso", JOptionPane.WARNING_MESSAGE);
    	}
    	// DONDE SE CREO LA CARPETA??? (para comprobar la ruta sin o encuentas la carpetas de datos)
    	//System.out.println("Guardando en: " + SAVE_FILE.toAbsolutePath());

        // 1) Crear repositorio y modelo de la tabla
        // -------------------------------------------------
        
        this.repo = new Tareas_Gestor();
        
        try {
            java.nio.file.Files.createDirectories(SAVE_DIR);
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo crear la carpeta de datos:\n" + ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        
        try {
            repo.loadFromFile(SAVE_FILE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el archivo de tareas:\n" + ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        this.model = new TareasTableModel(repo);

        // 2) Crear la JTable basada en el modelo
        // -------------------------------------------------
        this.table = new JTable(model);
        table.setFillsViewportHeight(true);                        // que use todo el alto disponible
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // seleccionamos solo una fila
        // (Opcional) Si algún día activas ordenación:
        // table.setAutoCreateRowSorter(true);

        // Ajustar ancho de la primera columna (checkbox)
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMaxWidth(120);
            table.getColumnModel().getColumn(0).setMinWidth(90);
            table.getColumnModel().getColumn(2).setPreferredWidth(300); // Descripción más ancha
        }

        // 3) Crear campos de entrada y botones
        // -------------------------------------------------
        this.txtTitulo = new JTextField(18);
        txtTitulo.setToolTipText("Título de la tarea (obligatorio)");

        this.txtDescripcion = new JTextField(24);
        txtDescripcion.setToolTipText("Descripción (opcional)");

        this.btnAñadir = new JButton("Añadir");
        this.btnBorrar = new JButton("Borrar");

        // 4) Listeners (comportamiento de los botones)
        // -------------------------------------------------

        // Añadir: valida el título, añade al modelo y limpia campos
        btnAñadir.addActionListener(e -> {
            String nombre = txtTitulo.getText().trim();
            String desc   = txtDescripcion.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Escribe un título para la tarea.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                txtTitulo.requestFocusInWindow();
                return;
            }

            model.addTarea(nombre, desc);     // esto inserta en el repo y notifica a la tabla
            try {
                repo.saveToFile(SAVE_FILE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar tras añadir:\n" + ex.getMessage(),
                        "Error de guardado", JOptionPane.ERROR_MESSAGE);
            }
            txtTitulo.setText("");            // limpiar campos
            txtDescripcion.setText("");
            txtTitulo.requestFocusInWindow(); // volver a enfocar el título para escribir rápido
        });

        // Borrar: elimina la fila seleccionada (si hay selección)
        btnBorrar.addActionListener(e -> {
            int viewRow = table.getSelectedRow(); // índice de fila en la vista (puede cambiar si hay ordenación)
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecciona una tarea para borrar.",
                        "Sin selección",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Convertir a índice del modelo (por si activas sorter en el futuro)
            int modelRow = table.convertRowIndexToModel(viewRow);
            model.removeAt(modelRow); // borra del repo y notifica a la tabla
            try {
                repo.saveToFile(SAVE_FILE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar tras borrar:\n" + ex.getMessage(),
                        "Error de guardado", JOptionPane.ERROR_MESSAGE);
            }
        });

        // (Calidad de vida) Pulsar Enter en el campo de título actúa como "Añadir"
        txtTitulo.addActionListener(e -> btnAñadir.doClick());
        // Y también desde descripción si quieres flujo rápido
        txtDescripcion.addActionListener(e -> btnAñadir.doClick());

        // (Calidad de vida) Define el botón por defecto (Enter en la ventana)
        getRootPane().setDefaultButton(btnAñadir);

        // 5) Componer la interfaz (layouts y paneles)
        // -------------------------------------------------

        // Panel superior (NORTH) con los campos y el botón Añadir
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Nueva tarea"));
        panelSuperior.add(new JLabel("Título:"));
        panelSuperior.add(txtTitulo);
        panelSuperior.add(new JLabel("Descripción:"));
        panelSuperior.add(txtDescripcion);
        panelSuperior.add(btnAñadir);

        // Panel central: la tabla dentro de un scroll
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Tareas"));

        // Panel inferior (SOUTH) con el botón Borrar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnBorrar);

        // Layout principal de la ventana
        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(panelInferior, BorderLayout.SOUTH);

        // 6) Configuración de la ventana
        // -------------------------------------------------
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 420);
        setLocationRelativeTo(null); // centrar
        setVisible(true);
    }

    // Punto de entrada para probar la app completa (tabla + botones)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Tareas_Interface::new);
    }
}
