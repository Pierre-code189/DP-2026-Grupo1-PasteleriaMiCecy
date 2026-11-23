package pasteleriamicedy;

import java.math.BigDecimal;

/** Producto concreto: empanada de manjarblanco. */
public class EmpanadaManjar implements Postre {
    @Override
    public String getNombre() {
        return "Empanada de Manjarblanco";
    }

    @Override
    public BigDecimal getPrecioBase() {
        return BigDecimal.valueOf(4.5);
    }
}
