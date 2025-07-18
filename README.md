#  Gestor de Gastos Personales

Aplicación móvil para Android desarrollada como parte del proyecto final del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM). 
Su objetivo es ayudar a cualquier persona a registrar y controlar sus gastos del día a día de forma sencilla, sin complicaciones y sin necesidad de conexión a Internet.

---

##  Descripción

Esta app está pensada para quienes buscan algo rápido, útil y directo para gestionar su economía personal. No requiere registros, ni conexión a la nube, ni pasos innecesarios. Simplemente la instalas, y ya puedes comenzar a añadir tus gastos.

Algunas situaciones típicas donde resulta útil:

- Anotar pequeñas compras que luego se olvidan.
- Saber cuánto se gasta cada mes en ocio, comida o transporte.
- Controlar gastos sin necesidad de apps complejas o con anuncios.

---

##  Funcionalidades

- ➕ Añadir un gasto con descripción, cantidad, categoría y fecha.
- 📋 Ver todos los gastos en una lista ordenada por fecha.
- 🔎 Filtrar gastos por mes o por categoría.
- ✏️ Editar o eliminar un gasto manteniendo pulsado sobre él.
- 🌗 Activar/desactivar el **modo oscuro** con un solo botón.
- 💾 Los datos se guardan en el dispositivo (SQLite), no se pierden al cerrar la app.

---

##  Tecnologías utilizadas

- **Android Studio** como entorno de desarrollo.
- **Java** como lenguaje principal.
- **XML** para el diseño de la interfaz.
- **SQLite** para guardar los datos de forma local.
- **Material Design** para mantener un estilo visual claro y moderno.

---

##  Estructura general

- `MainActivity.java`: Lógica principal de la aplicación y gestión de eventos.
- `Gasto.java`: Clase modelo para representar los gastos.
- `GastoDBHelper.java`: Encargada de la gestión de la base de datos.
- `GastosAdapter.java`: Adaptador para mostrar los gastos en un RecyclerView.
- `XMLs de diseño`: activity_main.xml`, `dialog_agregar_gasto.xml`, `item_gasto.xml`.

---


##  Mejoras futuras

Aunque la aplicación ya es funcional, hay algunas ideas para futuras versiones:

- 📊 Añadir estadísticas o gráficos de gastos por mes/categoría.
- 🔔 Alertas cuando se supera un límite de gasto mensual.
- ☁️ Copia de seguridad en la nube o exportación a PDF/Excel.
- 🔐 Sistema de usuarios o PIN para mayor privacidad.
- 🧾 Permitir personalizar categorías.

---

## 🧑‍💻 Autor

Daniel Marinho Martínez  
Proyecto realizado como parte del TFG del ciclo DAM – IES Matemático Puig Adam (Madrid)



