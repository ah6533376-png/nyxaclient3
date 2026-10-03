package dev.nyxa.settings;

import java.util.List;

public class ModeSetting extends Setting<String> {
    public final List<String> modes;
    private int index;

    public ModeSetting(String name, int def, String... modes) {
        super(name, modes[def]);
        this.modes = List.of(modes);
        this.index = def;
    }

    public void cycle() { setIndex(index + 1); }

    public int getIndex() { return index; }

    public void setIndex(int i) {
        index = Math.floorMod(i, modes.size());
        value = modes.get(index);
    }

    @Override
    public void setValue(String v) {
        int i = modes.indexOf(v);
        if (i >= 0) { index = i; value = v; }
    }

    @Override
    public String display() { return value; }
}
