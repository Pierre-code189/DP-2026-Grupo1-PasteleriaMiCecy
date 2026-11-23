package pasteleriamicedy;

import java.math.BigDecimal;

/**
 * PRODUCTO del patrón Factory Method.
 * Contrato común de todos los postres que vende la pastelería.
 */
public interface Postre {
    String getNombre();
    BigDecimal getPrecioBase();
}
