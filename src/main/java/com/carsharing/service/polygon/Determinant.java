package com.carsharing.service.polygon;

public class Determinant {
    public static boolean determine(final Points points, double x, double y) {

        boolean result = false;
        int count = points.count();
        double[] xp = points.getXArray();
        double[] yp = points.getYArray();


        for (int i = 0, j = count - 1; i < count; j = i++) {
            if (xp[j] == xp[i] || yp[j] == yp[i]) // Деление на ноль.
                continue;

            if (((yp[i] <= y && y < yp[j]) || (yp[j] <= y && y < yp[i]))) {
                double x1 = xp[i];
                double y1 = yp[i];
                double x2 = xp[j];
                double y2 = yp[j];

                double k = (y2 - y1) / (x2 - x1);
                double b = y1 - k * x1;
                double cross_x = (y - b) / k;

                if ((float) x > cross_x)
                    result = !result;
            }
        }
        return result;
    }
}