package pasteleriamicedy;

import java.math.BigDecimal;

/** Caja de la pastelería: concentra el registro y la consulta de las ventas del día. */
public class CajaPasteleria implements RegistradorVentas {
    private BigDecimal totalVentasDia = BigDecimal.ZERO;

    @Override
    public synchronized void registrarVenta(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo.");
        }
        if (monto.signum() < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo: " + monto);
        }
        totalVentasDia = totalVentasDia.add(monto);
    }

    @Override
    public synchronized BigDecimal getTotalVentasDia() {
        return totalVentasDia;
    }

    @Override
    public synchronized void cerrarCaja() {
        totalVentasDia = BigDecimal.ZERO;
    }
}
