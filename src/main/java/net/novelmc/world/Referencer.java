package net.novelmc.world;

import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;

public final class Referencer {
    @NotNull
    @Contract("_ -> new")
    public static Referencer of(@NotNull World world) {
        return new Referencer(world.getName());
    }

    @NotNull
    @Contract("_ -> new")
    public static Referencer ofName(String name) {
        return new Referencer(name);
    }

    private final String name;

    private Referencer(String name) {
        this.name = Objects.requireNonNull(name, "name").toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        Referencer other = (Referencer) object;
        return name.equals(other.name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean matches(@NotNull World world) {
        return world.getName().equalsIgnoreCase(this.name);
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String toString() {
        return "Referencer [name=" + name + "]";
    }
}
