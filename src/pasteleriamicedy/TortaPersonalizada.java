package pasteleriamicedy;

import java.math.BigDecimal;

/**
 * Producto inmutable construido con el patrón BUILDER.
 *
 * La torta por encargo tiene cinco atributos, varios opcionales. El Builder
 * permite fijarlos por pasos y conserva valores predeterminados para los que
 * el cliente no indica.
 */
public class TortaPersonalizada implements Postre {
    private final String bizcocho;
    private final String relleno;
    private final String cobertura;
    private final String mensaje;
    private final BigDecimal precio;

    private TortaPersonalizada(Builder builder) {
        bizcocho = builder.bizcocho;
        relleno = builder.relleno;
        cobertura = builder.cobertura;
        mensaje = builder.mensaje;
        precio = builder.precio;
    }

    @Override
    public String getNombre() {
        return "Torta Personalizada";
    }

    @Override
    public BigDecimal getPrecioBase() {
        return precio;
    }

    @Override
    public String toString() {
        return "Torta personalizada [bizcocho=" + bizcocho + ", relleno=" + relleno
                + ", cobertura=" + cobertura + ", mensaje=\"" + mensaje
                + "\", precio=S/ " + String.format(java.util.Locale.US, "%.2f", precio) + "]";
    }

    /**
     * BUILDER: constructor paso a paso de {@link TortaPersonalizada}.
     * Cada método devuelve el propio builder (fluent interface) y valida la
     * entrada antes de guardarla.
     */
    public static class Builder {
        // Valores predeterminados: una torta puede pedirse solo indicando el precio.
        private String bizcocho = "Vainilla clásico";
        private String relleno = "Manjar de la casa";
        private String cobertura = "Buttercream suave";
        private String mensaje = "¡Feliz día!";
        private BigDecimal precio = BigDecimal.valueOf(50.0);

        public Builder conBizcocho(String valor) { bizcocho = textoValido(valor, "bizcocho"); return this; }
        public Builder conRelleno(String valor) { relleno = textoValido(valor, "relleno"); return this; }
        public Builder conCobertura(String valor) { cobertura = textoValido(valor, "cobertura"); return this; }
        public Builder conMensaje(String valor) { mensaje = textoValido(valor, "mensaje"); return this; }

        public Builder conPrecio(BigDecimal valor) {
            if (valor == null || valor.signum() <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor que cero.");
            }
            precio = valor;
            return this;
        }

        private String textoValido(String valor, String campo) {
            if (valor == null || valor.isBlank()) {
                throw new IllegalArgumentException("El campo " + campo + " no puede estar vacío.");
            }
            return valor.trim();
        }

        /** Construye la torta inmutable. */
        public TortaPersonalizada build() { return new TortaPersonalizada(this); }
    }
}
