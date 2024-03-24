## Intérprete de Lisp en Java

Este proyecto es un intérprete simple para el lenguaje Lisp implementado en Java. Permite a los usuarios ingresar expresiones Lisp y evaluarlas en secuencia. El intérprete consta de varios componentes, incluido un analizador léxico para la tokenización, un analizador para construir un Árbol de Sintaxis Abstracta (AST) y un evaluador para interpretar las expresiones Lisp.

### Funcionamiento

El intérprete admite expresiones básicas similares a Lisp, que incluyen operaciones aritméticas, asignaciones de variables y llamadas a funciones. Los usuarios pueden ingresar expresiones Lisp a través de una interfaz de línea de comandos, y el intérprete tokeniza, analiza y evalúa las expresiones, devolviendo el resultado al usuario. También, se pueden evaluar funciones como atom, =, list, quote o ', <, >, y cond.

### Propósito

El propósito de este proyecto es demostrar los principios de análisis léxico, análisis sintáctico e interpretación de lenguajes de programación. Al implementar un intérprete simple de Lisp en Java, mostramos cómo se pueden analizar e interpretar lenguajes como Lisp utilizando técnicas de programación estándar. Asimismo, mediante el desarrollo del interprete, se mejora la comprensión del funcionamiento de las diferentes JCF que existen, y cómo implementarlas.

### Componentes

- Lexer (Tokenización): El lexer convierte cadenas de entrada en tokens, como paréntesis, números y cadenas. Itera a través de la cadena de entrada carácter por carácter, identificando tokens según reglas predefinidas.

- Parser (Construcción de AST): El analizador construye un Árbol de Sintaxis Abstracta (AST) a partir de los tokens generados por el lexer. Analiza recursivamente expresiones y construye nodos para átomos (números, cadenas) y listas, representando la estructura de las expresiones Lisp de entrada.

- Evaluator: El evaluador interpreta las expresiones Lisp recorriendo el AST y ejecutando las operaciones correspondientes.

### Uso de Java Collections Framework

Este proyecto utiliza las siguientes clases del Java Collections Framework:

- List: Utilizado en las clases Lexer y Parser para almacenar tokens y nodos, respectivamente. Las listas proporcionan un tamaño dinámico, fácil iteración y manipulación eficiente de elementos, lo que las hace adecuadas para almacenar secuencias de longitud variable de tokens o nodos.

- ArrayList: Específicamente utilizado en las clases Lexer y Parser para representar listas de tokens y nodos. ArrayLists ofrecen acceso rápido aleatorio y redimensionamiento dinámico, lo que los hace ideales para almacenar tokens y nodos durante los procesos de tokenización y análisis.

- HashMap: Utilizado en la clase Evaluator para almacenar variables y sus valores durante la evaluación de expresiones. También, se utilizó para que cuando se definan funciones, se pueda tener un mapa para almacenar sus parametros, y otro para el cuerpo del método. HashMap proporciona un mapeo eficiente de claves y valores, lo que permite un acceso rápido a las variables y una fácil asignación de valores durante la evaluación.

Los Frameworks de Colecciones de Java (especialmente List, ArrayList y HashMap) se utilizan en este proyecto para administrar colecciones de tokens, nodos y variables de manera eficiente. Al utilizar estos frameworks, aseguramos una gestión robusta de datos, una iteración fácil y una manipulación flexible de tokens, nodos y variables a lo largo de los procesos de tokenización, análisis y evaluación.

### Referencias para uso de JCF

- ArrayList: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html
- HashMap: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashMap.html
- List: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/List.html

### ¿Cómo iniciarlo?

Para usar el intérprete de Lisp, simplemente clona el repositorio y compila los archivos Java usando un compilador Java. Luego, ejecuta la clase App y sigue las instrucciones proporcionadas en la interfaz de línea de comandos para ingresar expresiones Lisp para su evaluación.



