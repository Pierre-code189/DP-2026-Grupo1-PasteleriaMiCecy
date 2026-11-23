package pasteleriamicedy;

import java.math.BigDecimal;

/**
 * Abstracción que usa el pedido para registrar el cobro sin acoplarse a la
 * clase concreta de la caja (principio de inversión de dependencias).
 */
public interface RegistradorVentas {

    /** Registra un cobro. El monto debe ser no negativo y no nulo. */
    void registrarVenta(BigDecimal monto);

    /** Total acumulado del día. */
    BigDecimal getTotalVentasDia();

    /** Reinicia el acumulador (cierre de caja). */
    void cerrarCaja();
}
