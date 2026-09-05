package com.hogv1.module.setting;

public final class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double step;

    public NumberSetting(String id, String name, double defaultValue, double min, double max, double step) {
        super(id, name, checked(defaultValue, min, max, step));
        this.min = min;
        this.max = max;
        this.step = step;
    }

    private static double checked(double value, double min, double max, double step) {
        if (!Double.isFinite(value) || !Double.isFinite(min) || !Double.isFinite(max)
                || !Double.isFinite(step) || max < min || step <= 0 || value < min || value > max)
            throw new IllegalArgumentException("Invalid number setting bounds or default");
        return value;
    }

    @Override protected Double normalize(Double value) {
        if (!Double.isFinite(value)) return defaultValue();
        double bounded = Math.max(min, Math.min(max, value));
        double snapped = min + Math.rint((bounded - min) / step) * step;
        return Math.max(min, Math.min(max, snapped));
    }
    public void set(double value) { super.set(value); }
    public double min() { return min; }
    public double max() { return max; }
    public double step() { return step; }
    public int intValue() { return (int) Math.round(get()); }
}
