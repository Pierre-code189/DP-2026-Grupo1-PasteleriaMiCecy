package pasteleriamicedy;

import java.math.BigDecimal;
import java.util.Locale;

public class MainPasteleriaMiCecy {
    public static void main(String[] args) {
        try {
            System.out.println("=== Bienvenidos a Pastelería Mi Cecy ===");

            // FACTORY METHOD: cada fábrica concreta decide qué tipo de postre crear.
            FabricaPostres fabricaTortas = new FabricaTortas();
            FabricaPostres fabricaBocaditos = new FabricaBocaditos();
            Postre torta = fabricaTortas.prepararPostre();
            Postre bocadito = fabricaBocaditos.prepararPostre();

            // BUILDER: la torta por encargo se arma por pasos y lo no indicado queda con valores por defecto.
            TortaPersonalizada tortaEncargo = new TortaPersonalizada.Builder()
                    .conBizcocho("Red Velvet")
                    .conRelleno("Fresa natural")
                    .conPrecio(BigDecimal.valueOf(68.0))
                    .build();
            System.out.println(tortaEncargo);

            // El pedido depende de una abstracción, no de la clase concreta de la caja.
            RegistradorVentas caja = new CajaPasteleria();
            PedidoPasteleria pedido = new PedidoPasteleria(caja);
            pedido.finalizarCobro(torta.getPrecioBase());
            pedido.finalizarCobro(bocadito.getPrecioBase());
            pedido.finalizarCobro(tortaEncargo.getPrecioBase());

            System.out.printf(Locale.US,
                    "Total acumulado en caja hoy: S/ %.2f%n",
                    caja.getTotalVentasDia());
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo completar la venta: " + e.getMessage());
        }
    }
}
