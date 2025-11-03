package appTareas;

import javax.swing.table.AbstractTableModel;

/**
 * TareasTableModel
 * ----------------
 * Modelo de tabla que “traduce” el contenido de GestionTareas a filas/columnas para la JTable.
 * - Columna 0: Boolean (checkbox) -> "Completada"  (editable).
 * - Columna 1: String            -> "Título"       (NO editable por ahora).
 *
 * La JTable consulta este modelo para pintar las celdas y también lo usa para escribir cambios
 * (p. ej., cuando el usuario marca/desmarca el checkbox).
 */
public class TareasTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L; // evita el warning de serialización

    // Referencia al repositorio que ya gestiona la lista de tareas en memoria.
    private final Tareas_Gestor repo;

    // Nombres de columnas que verá el usuario en el encabezado de la JTable.
    private final String[] cols  = {"Completada", "Título", "Descripción"};

    // Tipos de columna: muy importante devolver Boolean.class para que Swing pinte un checkbox.
    private final Class<?>[] types = {Boolean.class, String.class, String.class};

    // El modelo necesita el repositorio para leer/escribir.
    public TareasTableModel(Tareas_Gestor repo) {
        this.repo = repo;
    }

    // Nº de filas = nº de tareas en el repositorio.
 
    public int getRowCount() {
        return repo.size();
    }

    // Nº de columnas fijas (2).
   
    public int getColumnCount() {
        return cols.length;
    }

    // Nombre visible de cada columna en el header de la JTable.
    
    public String getColumnName(int column) {
        return cols[column];
    }

    // Tipo de cada columna. Esto permite a Swing decidir el render/editor (checkbox para Boolean).
    
    public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    // Solo permitimos editar la columna 0 (checkbox de “Completada”).
   
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }

    // Método de ayuda: obtener la Tarea de una fila concreta.
    private Tareas tareaAt(int row) {
        return repo.getPorIndice(row);
    }

    // Devuelve el valor que se mostrará en la celda [row, col].
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tareas t = tareaAt(rowIndex);
        switch (columnIndex) {
            case 0: return t.isTareaCompletada();
            case 1: return t.getNombreTarea();
            case 2: return t.getDescripcionTarea(); // <- NUEVO
            default: return null;
        }
    }

    // Escribe el valor que el usuario introdujo en la celda (solo usamos col 0).
   
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0 && aValue instanceof Boolean) {
            Tareas t = tareaAt(rowIndex);
            t.setTareaCompletada((Boolean) aValue); // marcamos/desmarcamos la tarea
            // Notificamos a la JTable que esta fila ha cambiado (para que se repinte).
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    // ---- Métodos pensados para la UI (los usaremos cuando añadamos los botones) ----

    /**
     * Añade una nueva tarea y notifica a la tabla que hay una fila nueva.
     * No lo usaremos HOY (porque aún no hay botones), pero lo dejamos listo.
     */
    public void addTarea(String nombre, String descripcion) {
        int oldRowCount = getRowCount();
        if (repo.addTarea(nombre, descripcion) != null) {
            fireTableRowsInserted(oldRowCount, oldRowCount);
        }
    }

    /**
     * Borra la fila indicada (útil cuando tengamos el botón "Borrar").
     */
    public void removeAt(int row) {
        if (row < 0 || row >= getRowCount()) return;
        int id = tareaAt(row).getIdTarea();
        if (repo.removeTareaPorId(id)) {
            fireTableRowsDeleted(row, row);
        }
    }
}
