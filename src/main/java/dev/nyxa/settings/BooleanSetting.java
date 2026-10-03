package dev.nyxa.settings;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, boolean def) { super(name, def); }

    public void toggle() { setValue(!getValue()); }

    @Override
    public void setValue(Boolean v) { value = v; }

    @Override
    public String display() { return getValue() ? "ON" : "OFF"; }
}
