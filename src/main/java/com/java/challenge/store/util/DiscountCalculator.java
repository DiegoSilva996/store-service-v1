package com.java.challenge.store.util;

import java.math.BigDecimal;

public class DiscountCalculator {

    public static BigDecimal applyDiscount(
            BigDecimal total,
            int cantidadProductosDiferentes
    ) {
        BigDecimal descuento = BigDecimal.ZERO;

        if (total.compareTo(BigDecimal.valueOf(1000)) > 0) {
            descuento = descuento.add(BigDecimal.valueOf(0.10));
        }

        if (cantidadProductosDiferentes > 5) {
            descuento = descuento.add(BigDecimal.valueOf(0.05));
        }

        return total.multiply(BigDecimal.ONE.subtract(descuento));
    }
}
