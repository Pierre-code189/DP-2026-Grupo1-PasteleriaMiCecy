package pasteleriamicedy;

/** CONCRETE CREATOR: fabrica tortas de chocolate. */
public class FabricaTortas extends FabricaPostres {
    @Override
    public Postre crearPostre() {
        return new TortaChocolate();
    }
}
