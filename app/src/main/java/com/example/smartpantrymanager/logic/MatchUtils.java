package com.example.smartpantrymanager.logic;

import java.util.Locale;

public class MatchUtils {

    // makes names comparable "Tomatoes " and "tomato" both become "tomato"
    public static String normalizeName(String name) {
        String n = name.trim().toLowerCase(Locale.ROOT);

        if (n.endsWith("ies") && n.length() > 3) {
            // berries -> berry
            return n.substring(0, n.length() - 3) + "y";
        }
        if (n.endsWith("oes") && n.length() > 3) {
            // tomatoes -> tomato, potatoes -> potato
            return n.substring(0, n.length() - 2);
        }
        if (n.endsWith("s") && !n.endsWith("ss") && !n.endsWith("us") && n.length() > 2) {
            // eggs -> egg, beans -> bean
            return n.substring(0, n.length() - 1);
        }
        return n;
    }

    // turns a quantity into a base unit: kg -> g, l -> ml
    public static double toBaseAmount(double quantity, String unit) {
        String u = unit.trim().toLowerCase(Locale.ROOT);

        switch (u) {
            case "g":
            case "gram":
            case "grams":
                return quantity;
            case "kg":
            case "kilogram":
            case "kilograms":
                return quantity * 1000;
            case "ml":
            case "milliliter":
            case "milliliters":
                return quantity;
            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return quantity * 1000;
            case "pcs":
            case "pc":
            case "piece":
            case "pieces":
                return quantity;
            default:
                return -1;
        }
    }

    // tells us what kind of unit it is, so we never compare grams to pieces
    public static String unitType(String unit) {
        String u = unit.trim().toLowerCase(Locale.ROOT);

        switch (u) {
            case "g":
            case "gram":
            case "grams":
            case "kg":
            case "kilogram":
            case "kilograms":
                return "weight";
            case "ml":
            case "milliliter":
            case "milliliters":
            case "l":
            case "litre":
            case "litres":
            case "liter":
            case "liters":
                return "volume";
            case "pcs":
            case "pc":
            case "piece":
            case "pieces":
                return "count";
            default:
                return "unknown";
        }
    }
}