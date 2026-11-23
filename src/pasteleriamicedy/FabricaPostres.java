package pasteleriamicedy;

/**
 * CREATOR del patrón Factory Method.
 * Declara el método de fábrica y el esqueleto de preparación del producto.
 */
public abstract class FabricaPostres {

    /** FACTORY METHOD: cada subclase decide qué producto concreto instanciar. */
    public abstract Postre crearPostre();

    /**
     * Crea el postre, lo anuncia en consola y devuelve la misma instancia
     * creada, de modo que el cliente trabaje con el producto que se preparó.
     */
    public Postre prepararPostre() {
        Postre postre = crearPostre();
        System.out.println("Preparando en Pastelería Mi Cecy: "
                + postre.getNombre() + " (S/ " + postre.getPrecioBase() + ")");
        return postre;
    }
}
