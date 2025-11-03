# 🗂️ Gestor de Tareas en Java

Aplicación de escritorio desarrollada en **Java + Swing** que permite crear, organizar y gestionar tus tareas de forma sencilla y persistente.  
Fue uno de mis primeros proyectos prácticos para poner en práctica la **programación orientada a objetos**, la **gestión de datos** y el diseño de interfaces con **Swing**.

---

## 🧭 Descripción general

El **Gestor de Tareas** permite:

- ✏️ **Añadir tareas** con un nombre y una descripción.  
- ✅ **Marcar tareas como completadas**.  
- ❌ **Eliminar tareas** cuando ya no sean necesarias.  
- 💾 **Guardar automáticamente** todas las tareas para no perderlas al cerrar la aplicación.

La interfaz está diseñada para ser **ajustable**:  
puedes **maximizar la ventana** y cambiar el tamaño de las barras de título y descripción para adaptar el espacio a tus necesidades.

---

## 🧱 Arquitectura del proyecto

El programa está compuesto por **4 clases principales**, cada una con una función clara:

1. **`Tareas`**  
   Define qué es una tarea: su nombre, descripción, estado (realizada o pendiente) y demás propiedades.  
   Es la base de datos interna del programa.

2. **`TareasGestor`**  
   Gestiona las operaciones principales del sistema:
   - Añadir tareas  
   - Eliminar tareas  
   - Marcar tareas como completadas  
   Además, controla la **persistencia de datos** guardando la información en un archivo.

3. **`TareasTableModeloI`**  
   Maneja la parte visual del programa.  
   Define la **tabla**, las **columnas** y los **botones** de interacción, funcionando como un **modelo de tabla Swing (JTable)** que podría reutilizarse en otras aplicaciones similares.

4. **`Tareas_Interface`**  
   Es la **clase principal**, encargada de:
   - Iniciar la interfaz gráfica  
   - Cargar las tareas guardadas  
   - Coordinar la lógica general de la aplicación

---

## 📁 Estructura del proyecto

- `src/` → código fuente con las clases del programa  
- `datos/` → carpeta donde se guarda el archivo `.txt` con las tareas registradas  
  - El contenido está **codificado** para evitar su lectura directa desde fuera de la aplicación.  

---

## 💾 Persistencia de datos

Una de las características más útiles de este proyecto es la **persistencia**.  
Las tareas se guardan automáticamente cada vez que se crean, eliminan o marcan como completadas.  
Cuando se vuelve a abrir la aplicación, las tareas aparecen exactamente como estaban antes de cerrarla.

---

## ⚙️ Tecnologías utilizadas

- ☕ **Java**
- 🪟 **Swing (javax.swing.\*)**
- 🎨 **AWT (java.awt.\*)**
- 📂 **NIO Files (java.nio.file.\*)**
- 💡 **I/O Files (java.io.\*)**
- 🧩 **Programación orientada a objetos (POO)**

```java
import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.io.IOException;

---

## Cómo ejecutar el proyecto
1 Clona el repositorio:
git clone https://github.com/megalol-dev/gestor-tareas-java.git

2 Abre el proyecto en Eclipse o en cualquier IDE compatible con Java.

3 Ejecuta la clase principal:
Tareas_Interface.java

---

👨‍💻 Autor

José Luis Escudero Delv
📧 escuderopolojoseluis@gmail.com

