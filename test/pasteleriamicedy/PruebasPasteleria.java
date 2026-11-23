package pasteleriamicedy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Batería de verificación del proyecto, sin dependencias externas.
 * Uso: java -cp build/classes pasteleriamicedy.PruebasPasteleria
 */
public class PruebasPasteleria {
    private static int ejecutadas = 0;
    private static int fallidas = 0;

    /** Registrador alternativo para demostrar que el pedido no depende de la caja concreta. */
    private static class RegistradorFalso implements RegistradorVentas {
        BigDecimal total = BigDecimal.ZERO;
        int llamadas = 0;

        @Override
        public void registrarVenta(BigDecimal monto) {
            llamadas++;
            total = total.add(monto);
        }

        @Override
        public BigDecimal getTotalVentasDia() {
            return total;
        }

        @Override
        public void cerrarCaja() {
            total = BigDecimal.ZERO;
        }
    }

    public static void main(String[] args) throws Exception {
        new CajaPasteleria().cerrarCaja();

        probarRegistroYTotal();
        probarCierreDeCaja();
        probarValidacionDeMontos();
        probarInyeccionDeDependencia();
        probarBuilder();
        probarFabricas();
        probarRegistroConcurrente();

        System.out.println();
        System.out.println("Pruebas ejecutadas: " + ejecutadas + " | fallidas: " + fallidas);
        if (fallidas > 0) {
            System.exit(1);
        }
        System.out.println("TODAS LAS PRUEBAS PASARON");
    }

    private static void probarRegistroYTotal() {
        System.out.println("[Registro y total]");
        CajaPasteleria caja = new CajaPasteleria();
        PedidoPasteleria pedido = new PedidoPasteleria(caja);

        BigDecimal total1 = pedido.finalizarCobro(new BigDecimal("45.0"));
        comprobar(total1.compareTo(new BigDecimal("45.0")) == 0,
                "el primer cobro devuelve el total acumulado");

        pedido.finalizarCobro(new BigDecimal("4.5"));
        pedido.finalizarCobro(new BigDecimal("68.00"));

        comprobar(caja.getTotalVentasDia().compareTo(new BigDecimal("117.50")) == 0,
                "total 45.0 + 4.5 + 68.00 = 117.50");
    }

    private static void probarCierreDeCaja() {
        System.out.println("[Cierre de caja]");
        CajaPasteleria caja = new CajaPasteleria();
        caja.registrarVenta(new BigDecimal("10.00"));
        caja.cerrarCaja();
        comprobar(caja.getTotalVentasDia().compareTo(BigDecimal.ZERO) == 0,
                "cerrarCaja() reinicia el acumulador");
    }

    private static void probarValidacionDeMontos() {
        System.out.println("[Validación de montos]");
        CajaPasteleria caja = new CajaPasteleria();
        PedidoPasteleria pedido = new PedidoPasteleria(caja);

        comprobarExcepcion(() -> pedido.finalizarCobro(new BigDecimal("-1")),
                "rechaza monto negativo");
        comprobarExcepcion(() -> pedido.finalizarCobro(null),
                "rechaza monto nulo");
        comprobarExcepcion(() -> caja.registrarVenta(new BigDecimal("-0.01")),
                "la caja también rechaza montos negativos");

        comprobar(caja.getTotalVentasDia().compareTo(BigDecimal.ZERO) == 0,
                "un monto inválido no altera el total");
    }

    private static void probarInyeccionDeDependencia() {
        System.out.println("[Inversión de dependencias]");
        RegistradorFalso falso = new RegistradorFalso();
        PedidoPasteleria pedido = new PedidoPasteleria(falso);

        pedido.finalizarCobro(new BigDecimal("12.75"));

        comprobar(falso.llamadas == 1, "el pedido usa el registrador inyectado");
        comprobar(falso.total.compareTo(new BigDecimal("12.75")) == 0,
                "funciona con una implementación distinta de la caja");
        comprobarExcepcion(() -> new PedidoPasteleria(null),
                "rechaza un registrador nulo");
    }

    private static void probarBuilder() {
        System.out.println("[Builder]");

        TortaPersonalizada porDefecto = new TortaPersonalizada.Builder().build();
        comprobar(porDefecto.getPrecioBase().compareTo(new BigDecimal("50.0")) == 0,
                "precio por defecto 50.0");
        comprobar("Torta Personalizada".equals(porDefecto.getNombre()),
                "la torta personalizada es un Postre con nombre propio");

        TortaPersonalizada encargo = new TortaPersonalizada.Builder()
                .conBizcocho("Red Velvet")
                .conRelleno("Fresa natural")
                .conPrecio(new BigDecimal("68.00"))
                .build();
        comprobar(encargo.toString().contains("bizcocho=Red Velvet"), "aplica el bizcocho indicado");
        comprobar(encargo.toString().contains("cobertura=Buttercream suave"),
                "conserva la cobertura por defecto");
        comprobar(encargo.getPrecioBase().compareTo(new BigDecimal("68.00")) == 0,
                "aplica el precio indicado");

        comprobarExcepcion(() -> new TortaPersonalizada.Builder().conBizcocho("  "),
                "rechaza campo en blanco");
        comprobarExcepcion(() -> new TortaPersonalizada.Builder().conPrecio(new BigDecimal("0")),
                "rechaza precio cero");
        comprobarExcepcion(() -> new TortaPersonalizada.Builder().conPrecio(new BigDecimal("-5")),
                "rechaza precio negativo");
        comprobarExcepcion(() -> new TortaPersonalizada.Builder().conPrecio(null),
                "rechaza precio nulo");
    }

    private static void probarFabricas() {
        System.out.println("[Factory Method]");
        FabricaPostres fabricaTortas = new FabricaTortas();
        FabricaPostres fabricaBocaditos = new FabricaBocaditos();

        Postre torta = fabricaTortas.prepararPostre();
        Postre bocadito = fabricaBocaditos.prepararPostre();

        comprobar(torta instanceof TortaChocolate, "FabricaTortas crea TortaChocolate");
        comprobar(bocadito instanceof EmpanadaManjar, "FabricaBocaditos crea EmpanadaManjar");
        comprobar(torta.getPrecioBase().compareTo(new BigDecimal("45.0")) == 0,
                "el postre devuelto conserva su precio");
        comprobar(bocadito.getPrecioBase().compareTo(new BigDecimal("4.5")) == 0,
                "el bocadito devuelto conserva su precio");
    }

    private static void probarRegistroConcurrente() throws Exception {
        System.out.println("[Concurrencia]");
        final CajaPasteleria caja = new CajaPasteleria();

        final int hilos = 8;
        final int cobrosPorHilo = 500;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < hilos; i++) {
            Thread t = new Thread(() -> {
                PedidoPasteleria p = new PedidoPasteleria(caja);
                for (int j = 0; j < cobrosPorHilo; j++) {
                    p.finalizarCobro(new BigDecimal("0.01"));
                }
            });
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        BigDecimal esperado = new BigDecimal("0.01").multiply(new BigDecimal(hilos * cobrosPorHilo));
        comprobar(caja.getTotalVentasDia().compareTo(esperado) == 0,
                hilos + " hilos x " + cobrosPorHilo + " cobros de 0.01 = " + esperado);
    }

    private static void comprobar(boolean condicion, String nombre) {
        ejecutadas++;
        if (condicion) {
            System.out.println("  OK    " + nombre);
        } else {
            fallidas++;
            System.out.println("  FALLO " + nombre);
        }
    }

    private static void comprobarExcepcion(Runnable accion, String nombre) {
        ejecutadas++;
        try {
            accion.run();
            fallidas++;
            System.out.println("  FALLO " + nombre + " (no lanzó IllegalArgumentException)");
        } catch (IllegalArgumentException e) {
            System.out.println("  OK    " + nombre + " -> " + e.getMessage());
        }
    }
}
