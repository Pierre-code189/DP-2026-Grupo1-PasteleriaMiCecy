package pasteleriamicedy;

import java.math.BigDecimal;

/** Representa un pedido y delega el registro de su cobro en un {@link RegistradorVentas}. */
public class PedidoPasteleria {
    private final RegistradorVentas registrador;

    public PedidoPasteleria(RegistradorVentas registrador) {
        if (registrador == null) {
            throw new IllegalArgumentException("Se requiere un registrador de ventas.");
        }
        this.registrador = registrador;
    }

    /**
     * Registra el cobro y devuelve el total acumulado actualizado.
     * Si el monto es inválido, el acumulador no se altera.
     */
    public BigDecimal finalizarCobro(BigDecimal monto) {
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo.");
        }
        if (monto.signum() < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo: " + monto);
        }
        registrador.registrarVenta(monto);
        return registrador.getTotalVentasDia();
    }
}
