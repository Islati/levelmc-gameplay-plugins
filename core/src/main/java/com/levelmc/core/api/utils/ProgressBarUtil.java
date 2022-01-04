package com.levelmc.core.api.utils;

public class ProgressBarUtil {

    public static final String DEFAULT_BAR = "▌";

    // ▌

    public static String renderProgressBar(String filledColor, String unfilledColor, String character, double progress) {
        //20 bars
        //out of 100 each bar is 5%

        double progressPercent = progress;

        StringBuilder builder = new StringBuilder(filledColor);

        int greenBarCount = 0;
        int grayBarCount = 0;

        boolean firstRed = false;

        /*
        Progress "loses" 5% each time, which is equal to 1 'Green' bar.
         */

        for (int i = 20; i > 0; i -= 1) {
            if (progressPercent < 5) {
                if (!firstRed) {
                    builder.append(unfilledColor);
                    firstRed = true;
                }

                builder.append(character);
                continue;
            }

            builder.append(character);
            progressPercent -= 5;
        }

        return builder.toString();
    }
}
