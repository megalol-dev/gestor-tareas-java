package appTareas;

// Notas:
// Se encagar de almacenar los datos que puede tener una tarea.
// Además tiene los metodos set y get de todas los datos de las tareas.
public class Tareas {
	
	// Variables de una tarea
	
	private int idTarea;
	private String nombreTarea;
	private String descripcionTarea;
	private boolean tareaCompletada;
	
	// Constructor
	public Tareas (int idTarea, String nombreTarea, String descripcionTarea, boolean tareaCompletada){
		this.idTarea = idTarea;
		this.nombreTarea = nombreTarea;
		this.descripcionTarea = descripcionTarea;
		this.tareaCompletada = tareaCompletada;
		
	}//end
	
	// Métodos
	
	// Get -> para obtener un dato
	public int getIdTarea(){
		return idTarea;
	}//end
	
	
	public String  getNombreTarea() {
		return nombreTarea;
	}//end
	
	
	public String getDescripcionTarea() {
		return descripcionTarea;
	}//end
	
	
	public boolean isTareaCompletada() {
		return tareaCompletada;
	}//end
	
	
	
	// Set -> para cambiar un dato
	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}//end
	
	
	public void setNombreTarea(String nombreTarea) {
		this.nombreTarea = nombreTarea;
	}//end
	

	public void setDescripcionTarea(String descripcionTarea) {
		this.descripcionTarea = descripcionTarea;
	}//end
	
	
	public void setTareaCompletada(boolean tareaCompletada) {
		this.tareaCompletada = tareaCompletada;
	}//end
	
	
	// Metodo opcinal para marcar / desmarcar  tareas como completas
	public void toggleCompletada() {
	    this.tareaCompletada = !this.tareaCompletada;
	}



}//end class
