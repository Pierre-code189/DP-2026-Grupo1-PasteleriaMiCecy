# Guión de exposición — Pastelería Mi Cecy

Curso: Diseño de Patrones (100000SI47) · Grupo 1 · Ciclo 2 – Agosto 2026
Presentación: lunes 07/12/2026, 13:30–15:00 · Bloque grupal 10 min + defensa individual 5 min

---

## Parte grupal (10 minutos)

### 1. Problema y solución (2 min)

> «La pastelería Mi Cecy vende dos líneas de producto: tortas y bocaditos. Cada día atiende
> pedidos normales y también encargos de tortas personalizadas, donde el cliente elige
> bizcocho, relleno, cobertura y mensaje. El sistema tiene que vender cualquiera de esos
> productos por igual y registrar el dinero en caja.
>
> El problema de diseño es doble: por un lado, si el código del cobro conoce las clases
> concretas de cada producto, cada producto nuevo obliga a modificar el cobro; por otro lado,
> armar una torta con cinco atributos, varios opcionales, termina en un constructor con
> muchos parámetros donde no se entiende cuál es cuál.
>
> La solución es una aplicación de consola en Java que crea los productos con **Factory
> Method**, arma los encargos con **Builder** y registra los cobros contra una abstracción,
> `RegistradorVentas`.»

### 2. Patrones aplicados (3 min)

Decir en este orden, y señalar cada clase en el diagrama:

1. **Factory Method (creacional).** `FabricaPostres` es el *Creator*: declara el método
   abstracto `crearPostre()`. `FabricaTortas` y `FabricaBocaditos` son los *ConcreteCreator*:
   cada uno decide qué `Postre` instanciar. `Postre` es el *Product*; `TortaChocolate`,
   `EmpanadaManjar` y `TortaPersonalizada` son los *ConcreteProduct*. `Main` es el *Client*.
   - Problema que resuelve: crear el producto sin que el cliente conozca la clase concreta.
   - Por qué es mejor que sin patrón: sin él, `Main` tendría `if`/`switch` por tipo y quedaría
     acoplado a cada clase nueva. Con el patrón, agregar un producto es agregar un `Postre` y
     una fábrica, sin tocar el cobro.
   - Consecuencia honrosa: cada fábrica concreta produce un solo producto, así que una variante
     nueva de la misma línea exige otra subclase.

2. **Builder (creacional).** `TortaPersonalizada.Builder` es el *Builder*;
   `TortaPersonalizada` es el *Product*; `Main` actúa como *Director* porque arma la receta.
   - Problema que resuelve: construir un objeto de cinco atributos, con varios opcionales, y
     que además debe ser inmutable.
   - Por qué es mejor que sin patrón: evita el constructor telescópico; se indica solo lo que
     el cliente pidió; cada dato se valida al momento de fijarlo; el resultado queda
     inmutable.
   - Consecuencia honrosa: al tener valores por defecto para todo, un encargo puede quedar sin
     personalizar si el vendedor no revisa.

### 3. Recorrido del código (3 min)

Abrir el repositorio y el IDE, y seguir el flujo con el cursor:

```
MainPasteleriaMiCecy.main()
  ├─ new FabricaTortas() / new FabricaBocaditos()      (ConcreteCreator)
  ├─ fabrica.prepararPostre()
  │     └─ crearPostre()  →  new TortaChocolate() / new EmpanadaManjar()   (Factory Method)
  ├─ new TortaPersonalizada.Builder()...build()        (Builder)
  ├─ new CajaPasteleria()  →  RegistradorVentas        (inyección)
  ├─ pedido.finalizarCobro(postre.getPrecioBase())
  │     └─ caja.registrarVenta(monto)                  (doble validación)
  └─ caja.getTotalVentasDia()   →  S/ 117.50
```

### 4. Demostración en vivo (2 min)

1. Compilar y ejecutar `MainPasteleriaMiCecy`; mostrar las dos líneas de «Preparando…», la torta
   personalizada y el total.
2. Ejecutar `PruebasPasteleria` y mostrar `Pruebas ejecutadas: 24 | fallidas: 0`.
3. Si hay tiempo, agregar un producto en vivo (ver «Cambios en vivo»).

---

## Defensa individual (5 min)

El docente puede preguntar sobre **cualquier** clase. Respuestas breves y precisas:

**¿Qué hace esta clase y qué rol cumple?**

| Clase | Respuesta |
| --- | --- |
| `Postre` | Interfaz *Product* del Factory Method: nombre y precio base de todo lo que se vende. |
| `TortaChocolate`, `EmpanadaManjar` | *ConcreteProduct*: implementan `Postre` con su nombre y precio. |
| `FabricaPostres` | *Creator*: declara `crearPostre()` y el esqueleto `prepararPostre()`. No crea productos por sí sola. |
| `FabricaTortas`, `FabricaBocaditos` | *ConcreteCreator*: deciden el tipo concreto que devuelven. |
| `TortaPersonalizada.Builder` | *Builder*: acumula valores, conserva defaults, valida y construye. |
| `TortaPersonalizada` | *Product* del Builder y también *ConcreteProduct* del Factory Method. Es inmutable. |
| `PedidoPasteleria` | Cliente del cobro: delega en `RegistradorVentas`, no conoce la caja concreta. |
| `CajaPasteleria` / `RegistradorVentas` | Soporte del cobro: acumulan y consultan el total del día. |

**¿Qué pasaría si se elimina esta interfaz o clase abstracta?**

- **Sin `Postre`:** las fábricas devolverían tipos concretos, `Main` se acoplaría a cada clase y
  perderíamos la sustitución de Liskov y la extensión sin modificar.
- **Sin `FabricaPostres`:** habría que elegir el producto con `if`/`switch` en `Main`; el patrón
  desaparecería y el cliente quedaría acoplado a las clases concretas.
- **Sin `TortaPersonalizada.Builder`:** vuelve el constructor de cinco parámetros, sin defaults
  claros ni validación por campo, y el objeto quedaría expuesto a quedar mal armado.

**¿Cómo agregarías un nuevo tipo de producto sin modificar el código existente?**

> «Creo una clase que implementa `Postre` con su nombre y precio, y una fábrica que extienda
> `FabricaPostres` devolviéndolo. `Postre`, `PedidoPasteleria` y `CajaPasteleria` no se tocan.
> Lo único que cambia es `Main`, que es quien elige qué fábrica usar.»

**¿Qué principio SOLID se cumple en esta parte?**

- **S:** cada clase tiene una responsabilidad (`CajaPasteleria` total, `PedidoPasteleria` cobro,
  fábricas creación, builder construcción).
- **O:** se añaden productos y fábricas sin modificar `Postre`, `PedidoPasteleria` ni
  `CajaPasteleria`.
- **L:** los tres productos se usan indistintamente donde se espera un `Postre`.
- **I:** `Postre` (2 métodos) y `RegistradorVentas` (3) son contratos mínimos.
- **D:** `PedidoPasteleria` depende de `RegistradorVentas`, una abstracción que se le inyecta.

**Traza el flujo desde `Main` hasta que se ejecuta el método del patrón.**

> «`Main` crea `FabricaTortas`, llama `prepararPostre()`, que invoca `crearPostre()`; esa
> llamada polimórfica ejecuta el `new TortaChocolate()` de la subclase. Con el `Postre`
> devuelto, `PedidoPasteleria.finalizarCobro()` llama `registrarVenta()` de `CajaPasteleria`,
> que acumula el total.»

### Cambios en vivo probables

**A. Agregar un producto nuevo** (el más pedido):

```java
// src/pasteleriamicedy/TortaTresLeches.java
public class TortaTresLeches implements Postre {
    public String getNombre() { return "Torta Tres Leches"; }
    public java.math.BigDecimal getPrecioBase() { return java.math.BigDecimal.valueOf(52.0); }
}

// src/pasteleriamicedy/FabricaTresLeches.java
public class FabricaTresLeches extends FabricaPostres {
    @Override public Postre crearPostre() { return new TortaTresLeches(); }
}
```

y en `Main`: `FabricaPostres fabricaTresLeches = new FabricaTresLeches();`

**B. Agregar un campo al Builder:** agregar el atributo en `TortaPersonalizada`, su valor por
defecto y su método `conX` en el `Builder`, y actualizar `toString()`.

**C. Nuevo observador o estado:** no aplica, el proyecto usa patrones creacionales.

### Frases que conviene evitar

- Decir que `FabricaPostres` «crea» los postres: ella solo **declara** el método; quienes crean
  son las subclases.
- Mencionar un Singleton: ya no existe en esta versión (queda `CajaPasteleria` como clase
  instanciada e inyectada).
- Llamar `Abstract Factory` a `FabricaPostres`: no hay familias de productos, es Factory Method.
- Decir que el Builder requiere llenar todos los campos: al contrario, sus valores por defecto
  son su ventaja.

---

## Reparto sugerido (3 integrantes)

- **Integrante 1:** problema y solución (2 min) + ficha del Factory Method.
- **Integrante 2:** ficha del Builder + diagrama de clases.
- **Integrante 3:** recorrido del código + demostración en vivo.

Cada integrante debe poder responder de **todas** las clases, no solo de las que expuso.

## Lista de verificación final

- [ ] Enlace enviado al docente (`c31105@utep.edu.pe`) antes del 06/12/2026, 23:59.
- [ ] Tag `v1.0` creado en el repositorio.
- [ ] Integrantes cargados en el README con nombre, código y usuario de GitHub.
- [ ] Cada integrante con commits propios en el repo.
- [ ] Programa probado en la computadora donde se va a exponer.
