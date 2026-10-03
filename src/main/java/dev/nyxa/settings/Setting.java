package dev.nyxa.settings;

public abstract class Setting<T> {
    public final String name;
    protected T value;

    protected Setting(String name, T def) {
        this.name = name;
        this.value = def;
    }

    public T getValue() { return value; }
    public abstract void setValue(T v);
    public abstract String display();
}
