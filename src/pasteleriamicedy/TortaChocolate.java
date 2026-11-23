package pasteleriamicedy;

import java.math.BigDecimal;

/** Producto concreto: torta de chocolate. */
public class TortaChocolate implements Postre {
    @Override
    public String getNombre() {
        return "Torta de Chocolate Casera";
    }

    @Override
    public BigDecimal getPrecioBase() {
        return BigDecimal.valueOf(45.0);
    }
}
