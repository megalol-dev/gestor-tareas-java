package appTareas;
//Notas:
//Se encagar de gestionar las acciones de guarda tarea, borrar tareas y marcarlas como completadas


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.Base64;


public class Tareas_Gestor {
	
	// Variables
	private final List<Tareas> tareas = new ArrayList<>();
	private int nextId =1; // id auto increment para las tareas
	
	
	

	// Crea y añade una tarea con ID automático; devuelve la tarea creada				
	public Tareas addTarea(String nombre, String descripcion){
		Tareas tareaNueva = new Tareas(nextId++, nombre, descripcion, false);
		tareas.add(tareaNueva);
		return tareaNueva;
		
	}
	
	// Elimiar por ID, devuelve true si se elimino algo
	public boolean removeTareaPorId(int id) {
		return tareas.removeIf(tareaNueva -> tareaNueva.getIdTarea() == id);
		
	}
	
	
	// Marca y desmarca una tarea por ID, devuelve true si la encontro y la cambio
	public boolean toggleCompletada(int id) {
		for (Tareas tareaNueva : tareas) {
			if(tareaNueva.getIdTarea() == id) {
			tareaNueva.toggleCompletada();
			return true;
			}
			
		}
		
		return false;
		
	}
	
	
	
	// Devuelve una vista inmodificable de la lista (para leer sin poder mutar)
	public List<Tareas> getTareas(){
		return Collections.unmodifiableList(tareas);
	}
	
	
	// --- Métodos de apoyo útiles para la tabla ---
	public int size() {
		return tareas.size();
	}
	
	
	public Tareas getPorIndice(int index) {
		return tareas.get(index);
	}
	
    // (Opcional) Buscar por ID si lo necesitas en la UI
    public Tareas findById(int id) {
        for (Tareas t : tareas) {
            if (t.getIdTarea() == id) return t;
        }
        return null;
    }
    
    
 // Guarda todas las tareas en un TXT (1 línea por tarea)
 // Formato: id \t completada(0/1) \t base64(nombre) \t base64(descripcion)
 public void saveToFile(Path path) throws IOException {
	 java.nio.file.Path parent = path.getParent();
	 if (parent != null) {
	     java.nio.file.Files.createDirectories(parent);
	 }
     try (BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
         for (Tareas t : tareas) {
             int id = t.getIdTarea();
             int done = t.isTareaCompletada() ? 1 : 0;
             String nombreB64 = enc(t.getNombreTarea());
             String descB64   = enc(t.getDescripcionTarea() == null ? "" : t.getDescripcionTarea());
             w.write(id + "\t" + done + "\t" + nombreB64 + "\t" + descB64);
             w.newLine();
         }
     }
 }

 // Carga desde TXT. Si no existe, no hace nada.
 // Ajusta nextId al máximo id + 1
 public void loadFromFile(Path path) throws IOException {
     tareas.clear();
     int maxId = 0;
     if (!Files.exists(path)) return;

     try (BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
         String line;
         while ((line = r.readLine()) != null) {
             if (line.trim().isEmpty()) continue;
             String[] parts = line.split("\t", -1);
             if (parts.length < 4) continue; // línea inválida
             int id = Integer.parseInt(parts[0]);
             boolean done = "1".equals(parts[1]) || "true".equalsIgnoreCase(parts[1]);
             String nombre = dec(parts[2]);
             String desc   = dec(parts[3]);
             tareas.add(new Tareas(id, nombre, desc, done));
             if (id > maxId) maxId = id;
         }
     }
     nextId = maxId + 1;
 }

 // Helpers Base64 para evitar líos con tabs/saltos de línea
 private static String enc(String s) {
     return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
 }
 private static String dec(String b64) {
     return new String(Base64.getDecoder().decode(b64), StandardCharsets.UTF_8);
 }

	
	
	
	
}//end class
