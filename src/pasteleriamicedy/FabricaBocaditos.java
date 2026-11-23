package pasteleriamicedy;

/** CONCRETE CREATOR: fabrica bocaditos (empanadas de manjarblanco). */
public class FabricaBocaditos extends FabricaPostres {
    @Override
    public Postre crearPostre() {
        return new EmpanadaManjar();
    }
}
