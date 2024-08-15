# Simulation Of The Insects Moving Game

![Licencia MIT](https://img.shields.io/badge/licencia-MIT-blue.svg)
![Estado del Proyecto](https://img.shields.io/badge/estado-finalizado-green.svg)
![Versión](https://img.shields.io/badge/versión-1.0.0-brightgreen.svg)

Este es un proyecto en Java para simular el movimiento de insectos en un tablero de juego. El sistema permite a distintos tipos de insectos moverse en el tablero siguiendo reglas específicas, y se evalúa su rendimiento en función de la cantidad de comida que logran recolectar.

## 📋 Descripción

El **Simulation Of The Insects Moving Game** es una simulación en la que cuatro tipos de insectos (hormigas, mariposas, arañas y saltamontes) se mueven por un tablero siguiendo reglas predeterminadas. Cada tipo de insecto tiene diferentes capacidades de movimiento, y su objetivo es salir del tablero mientras recolectan la mayor cantidad de comida posible. Los insectos no pueden ver a otros insectos en el tablero, lo que puede llevar a decisiones subóptimas y posibles encuentros mortales.

## Diagrama UML

Tu código debe contener al menos todos los elementos presentados en el Diagrama de Clases UML dado, pero se permite extenderlo con clases y relaciones adicionales.

![Diagrama UML](https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game/assets/118859294/0b2651cc-7f81-48ee-a19c-3d3162b1cf5d)

### Tipos de Insectos:

- **Hormigas**: Pueden moverse en cualquier dirección (vertical, horizontal y diagonal).
- **Mariposas**: Pueden moverse solo vertical y horizontalmente.
- **Arañas**: Pueden moverse solo diagonalmente.
- **Saltamontes**: Pueden saltar solo en direcciones vertical y horizontal, saltando casillas impares.

## 🛠️ Tecnologías Utilizadas

- **Java**: Lenguaje de programación utilizado para implementar la simulación.
- **Orientación a Objetos**: Implementación basada en clases que representan a los diferentes insectos y sus comportamientos.
- **Diagrama UML**: Para la estructura y relación entre las clases.

## 🏗️ Instalación

Para ejecutar este proyecto en tu máquina local, sigue estos pasos:

1. **Clona el repositorio**:
   
   ```bash
   git clone https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game.git
   ```

2. **Navega al directorio del proyecto**:
   
   ```bash
   cd Simulation-Of-The-Insects-Moving-Game
   ```

3. **Compila el archivo Java**:
   
   ```bash
   javac Main.java
   ```

4. **Ejecuta el programa**:
   
   ```bash
   java Main
   ```

   Asegúrate de tener el archivo `input.txt` en el directorio donde ejecutas el programa.


## 🕹️ Reglas del Juego

### Movimientos de los Insectos:

## Movimientos de los Insectos

- **travelDirection()**: Se utiliza para simular el desplazamiento de un insecto en una dirección específica. Devuelve la cantidad de comida consumida durante el viaje y actualiza el tablero.
- **travelDiagonally()**: Se utiliza para simular el desplazamiento diagonal (Noreste, Sureste, Suroeste, Noroeste). Devuelve la cantidad de comida consumida y actualiza el tablero.
- **travelOrthogonally()**: Se utiliza para simular el desplazamiento ortogonal (Norte, Este, Sur, Oeste). Devuelve la cantidad de comida consumida y actualiza el tablero.

### Tipos de Movimiento para Cada Insecto

![Tipos de Movimiento](https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game/assets/118859294/ecf20cc2-1799-45b3-a394-b2baac3b93d3)

- **Hormigas**: pueden elegir las direcciones Norte (N), Este (E), Sur (S), Oeste (W), Noreste (NE), Sureste (SE), Suroeste (SW), y Noroeste (NW).
- **Mariposas**: pueden elegir las direcciones Norte (N), Este (E), Sur (S), Oeste (W).
- **Arañas**: pueden elegir las direcciones Noreste (NE), Sureste (SE), Suroeste (SW), y Noroeste (NW) (ya que solo pueden moverse diagonalmente).
- **Saltamontes**: pueden elegir las direcciones Norte (N), Este (E), Sur (S), Oeste (W).

Además, cada insecto está coloreado en uno de los siguientes colores: Rojo, Verde, Azul, Amarillo.

### Ejemplo de Tablero Inicial

El ejemplo del tablero inicial para nuestro juego se presenta en la Figura 2. Ten en cuenta que los insectos están coloreados y que los campos con números representan los puntos de comida con la cantidad específica de comida.

![Tablero Inicial](https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game/assets/118859294/cf808fd5-3f00-4764-b575-e258c30cef85)

### Reglas del Juego

En este juego, los insectos deben intentar salir del tablero uno por uno en el orden en que fueron ingresados en el archivo de entrada. Por ejemplo, si el saltamontes rojo es el primero en el archivo de entrada, elegirá una dirección y se moverá en esa dirección, mientras que todos los demás insectos no se moverán hasta que el saltamontes rojo salga del tablero o muera en el intento.

Las reglas del juego son las siguientes:

1. Cada insecto puede ver todos los puntos de comida en el tablero, pero no puede ver a otros insectos (ni siquiera en celdas vecinas). Por lo tanto, los insectos tomarán decisiones basadas únicamente en las posiciones de los puntos de comida, ignorando las posiciones de otros insectos. Esto puede causar decisiones subóptimas (muertes inesperadas).
2. Una vez que un insecto elige una dirección, comenzará a moverse en esa dirección (sin cambiar de dirección) y comerá toda la comida en su camino hasta que salga del tablero o sea asesinado.
3. Si un insecto se encuentra con otro insecto del mismo color en su camino, simplemente se ignorarán. Sin embargo, si visita la celda de insectos de diferente color, el primero será asesinado por el segundo. Nota que un saltamontes no será asesinado si salta sobre el insecto de un color diferente.
4. La parte difícil del juego es que un insecto elegirá la dirección de movimiento solo basándose en la maximización de la comida consumida a partir de las posiciones de los puntos de comida. Sin embargo, dado que los insectos no pueden ver a otros insectos, puede suceder que un insecto visite la celda de un insecto de diferente color y sea asesinado sin haber comido toda la comida en las celdas no visitadas del camino restante.
5. Si hay dos o más direcciones con la misma cantidad de comida, un insecto priorizará las direcciones en el siguiente orden:
   - Norte (N)
   - Este (E)
   - Sur (S)
   - Oeste (W)
   - Noreste (NE)
   - Sureste (SE)
   - Suroeste (SW)
   - Noroeste (NW)

Por ejemplo, si una hormiga roja ve la misma cantidad de comida en las direcciones Sureste (SE) y Noroeste (NW), elegirá la dirección Sureste (SE) porque tiene una mayor prioridad.

### Ejemplo de Simulación del Juego

Supongamos que tenemos la siguiente configuración inicial del tablero:

**Insectos**:
- [5, 4] Saltamontes Rojo
- [8, 7] Mariposa Verde
- [5, 5] Hormiga Azul
- [3, 7] Araña Azul
- [1, 6] Saltamontes Verde
- [2, 1] Araña Amarilla

**Puntos de Comida**:
- 5 [1, 4]
- 9 [1, 7]
- 6 [2, 2]
- 3 [2, 6]
- 1 [3, 1]
- 7 [4, 5]
- 5 [4, 7]
- 4 [5, 3]
- 1 [7, 4]
- 2 [7, 6]
- 6 [8, 1]

La configuración del tablero dada se representa en la siguiente imagen:

![Configuración Inicial del Tablero](https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game/assets/118859294/45a152e5-da8b-44ac-9d1f-4902c13e5139)

### Movimientos Ejemplo

**Movimiento 1**: Como el Saltamontes Rojo es el primero en el archivo de entrada, se moverá primero. Después de verificar todas las direcciones posibles, decidirá elegir la dirección norte (N) porque contiene la mayor cantidad de comida (5). Luego, se moverá hacia el norte hasta el final del tablero.

![Movimiento 1](https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game/assets/118859294/eab6d41d-b3ec-4e86-bffe-f91cba84a1e2)

**Movimiento 2**: El siguiente en el archivo de entrada es la Mariposa Verde. Después de comparar todas las direcciones, decidirá elegir la dirección norte (N) porque contiene la mayor cantidad de comida (14). Sin embargo, en su camino, la Mariposa Verde visitará la celda de la Araña Azul y será asesinada. Por lo tanto, terminará el juego comiendo solo 5 unidades de comida (en lugar de las 14 planeadas).

![Movimiento 2](https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game/assets/118859294/dc61faff-d34d-486e-8624-8014e2df1f5d)

**Movimiento 3**: El siguiente en el archivo de entrada es la Hormiga Azul. Después de comparar todas las direcciones, decidirá elegir la dirección norte (N) porque contiene la mayor cantidad de comida (7).

![Movimiento 3](https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game/assets/118859294/2aae0e40-08c7-437a-bccf-c3d6493f30bc)

**Movimiento 4**: El siguiente en el archivo de entrada es la Araña Azul. Después de comparar todas las direcciones, decidirá elegir la dirección noroeste (NW) porque contiene la mayor cantidad de comida (3).

![Movimiento 4](https://github.com/LatinGladiador/Simulation-Of-The-Insects-Moving-Game/assets/118859294/ba0000f0-2038-4fd6-9e49-5459e8ec4421)

**Movimiento 5**: El siguiente en el archivo de entrada es el Saltamontes Verde. Después de comparar todas las direcciones, decidirá elegir la dirección sur

### Tipos de Movimiento para Cada Insecto:

- **Hormigas**: Norte (N), Este (E), Sur (S), Oeste (W), Noreste (NE), Sureste (SE), Suroeste (SW), Noroeste (NW).
- **Mariposas**: Norte (N), Este (E), Sur (S), Oeste (W).
- **Arañas**: Noreste (NE), Sureste (SE), Suroeste (SW), Noroeste (NW).
- **Saltamontes**: Norte (N), Este (E), Sur (S), Oeste (W).

## 🖥️ Uso

1. **Prepara el archivo de entrada `input.txt`** con las especificaciones de los insectos y los puntos de comida en el tablero.
2. **Ejecuta el programa**.
3. **Consulta el archivo de salida `output.txt`** para ver los resultados de la simulación.

### Formato de Entrada

El archivo de entrada (`input.txt`) debe contener las siguientes líneas:

- La primera línea debe contener un entero D (4 ≤ D ≤ 1000), que representa el tamaño del tablero (el tablero es de tamaño D × D).
- La segunda línea debe contener un entero N (1 ≤ N ≤ 16), que representa el número de insectos en el tablero.
- La tercera línea debe contener un entero M (1 ≤ M ≤ 200), que representa el número de puntos de alimento en el tablero.
- Las siguientes N líneas deben contener cuatro valores separados por un espacio en el siguiente formato: `Color TipoInsecto CoordenadaX CoordenadaY`.
- Las siguientes M líneas deben contener tres valores separados por un espacio en el siguiente formato: `CantidadAlimento CoordenadaX CoordenadaY`.


### Formato de Salida

Primero, debes verificar los datos de entrada en busca de posibles violaciones a las reglas mencionadas. Si se encuentra un error, imprime uno de los siguientes mensajes en el archivo de salida (`output.txt`) y termina el programa:

- `Invalid board size`: Si el tamaño del tablero D está fuera de los límites.
- `Invalid number of insects`: Si el número de insectos N está fuera de los límites.
- `Invalid number of food points`: Si el número de puntos de alimento M está fuera de los límites.
- `Invalid insect color`: Si el color del insecto es diferente de Rojo, Verde, Azul, y Amarillo.
- `Invalid insect type`: Si el tipo de insecto es diferente de Hormiga, Mariposa, Araña, y Saltamontes.
- `Invalid entity position`: Si el insecto o punto de alimento está fuera del tablero.
- `Duplicate insects`: Si hay más de un insecto del mismo color y tipo en el tablero.
- `Two entities in the same position`: Si hay más de un tipo de entidad en la misma celda.

Si no hay errores, el archivo de salida (`output.txt`) debe contener N líneas (una para cada insecto del archivo de entrada) en el siguiente formato:

`Color TipoInsecto Dirección CantidadDeAlimentoConsumido`

### Ejemplo_1 de `input.txt`:

```plaintext
8
6
11
Red Grasshopper 5 4
Green Butterfly 8 7
Blue Ant 5 5
Blue Spider 3 7
Green Grasshopper 1 6
Yellow Spider 2 1
5 1 4
9 1 7
6 2 2
3 2 6
1 3 1
7 4 5
5 4 7
4 5 3
1 7 4
2 7 6
6 8 1
```

### Ejemplo_1 de `output.txt`:

```plaintext
Red Grasshopper North 5
Green Butterfly North 5
Blue Ant North 7
Blue Spider North-West 3
Green Grasshopper South 2
Yellow Spider North-East 0
```

### Ejemplo_2 de `input.txt`:

```plaintext

4
2
2
Red Grasshopper 3 2
Green Spider 3 3
100 3 4
50 1 2
```

### Ejemplo_2 de `output.txt`:

```plaintext
Red Grasshopper East 100
Green Spider North-East 0
```

### Ejemplo_3 de `input.txt`:

```plaintext

5
4
6
Red Ant 1 1
Red Spider 4 4
Red Butterfly 4 3
Green Butterfly 5 4
3 1 4
10 2 2
9 3 3
1 4 2
1 5 3
7 5 5
```

### Ejemplo_3 de `output.txt`:

```plaintext
Red Ant South-East 26
Red Spider South-West 1
Red Butterfly West 1
Green Butterfly North 3
```

### Ejemplo_4 de `input.txt`:

```plaintext
5
4
3
Orange Ant 2 1
Red Spider 4 4
Red Butterfly 4 3
Green Butterfly 5 4
3 1 4
10 2 2
9 3 3
```

### Ejemplo_4 de `output.txt`:

```plaintext
Invalid insect color
```

### Ejemplo_5 de `output.txt`:

```plaintext
5
2
3
Red Ant 2 1
Red Bug 4 3
5 1 4
6 2 3
9 3 3
```

### Ejemplo_5 de `output.txt`:

```plaintext
Invalid insect type
```


## 🤝 Contribuciones

Las contribuciones son bienvenidas. Si deseas contribuir, sigue estos pasos:

1. **Haz un fork del repositorio**.
2. **Crea una nueva rama** para tus cambios:
   
   ```bash
   git checkout -b feature/nueva-caracteristica
   ```

3. **Realiza tus cambios y haz commit**:
   
   ```bash
   git commit -m "Añadida nueva característica"
   ```

4. **Empuja tu rama**:
   
   ```bash
   git push origin feature/nueva-caracteristica
   ```

5. **Abre un Pull Request** en GitHub.

## 👤 Autor

Este proyecto fue creado y es mantenido por [LatinGladiador](https://github.com/LatinGladiador).

## 📜 Licencia

Este proyecto está licenciado bajo la [Licencia MIT](LICENSE).

---

[![MIT License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)



































