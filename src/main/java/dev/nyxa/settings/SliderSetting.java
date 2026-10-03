package dev.nyxa.settings;

public class SliderSetting extends Setting<Double> {
    public final double min, max, step;

    public SliderSetting(String name, double min, double max, double def, double step) {
        super(name, def);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void setValue(Double v) {
        v = Math.max(min, Math.min(max, v));
        value = Math.round(v / step) * step;
    }

    public void setFromNormalized(double t) {
        setValue(min + (max - min) * Math.max(0.0, Math.min(1.0, t)));
    }

    public double normalized() {
        return (value - min) / (max - min);
    }

    @Override
    public String display() {
        double v = Math.round(value * 100.0) / 100.0;
        return (v == Math.floor(v)) ? Integer.toString((int) v) : Double.toString(v);
    }
}
