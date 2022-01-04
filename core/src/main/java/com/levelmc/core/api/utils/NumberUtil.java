package com.levelmc.core.api.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class NumberUtil {
    private static Random random = new Random();

    private static DecimalFormat format = new DecimalFormat();

    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {
        format.setRoundingMode(RoundingMode.CEILING);


        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public static int getRandomInRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static double randomDouble(double min, double max) {
        return min + ThreadLocalRandom.current().nextDouble(Math.abs(max - min + 1));
    }

    public static double round(double num, int decimalPlaces) {
        StringBuilder formatString = new StringBuilder("#.");
        for (int i = 0; i < decimalPlaces; i++) {
            formatString.append("#");
        }

        format.applyPattern(formatString.toString());
        return Double.parseDouble(format.format(num));
    }

    public static boolean percentCheck(int percent) {
        return random.nextInt(101) <= percent;
    }

    public static boolean percentCheck(double percent) {
        return random.nextInt(101) <= percent;
    }

    public static float percentOf(int num, int target) {
        return num * 100.0f / target;
    }

    public static boolean isInRange(int num, int min, int max) {
        return num >= min && num <= max;
    }

    public static String toRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }
}
