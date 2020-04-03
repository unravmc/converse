package net.novelmc.world.decor;

import net.novelmc.world.Chunk;

import java.util.List;

public interface Decorator {
    List<Chunk> getCustomBaseDecor(BaseDecorType decorType);

    List<Decor> getCustomDecor(DecorType type);

    void setDefaultBaseDecoratorsEnabled(BaseDecorType type, boolean value);

    void setDefaultDecoratorsEnabled(DecorType type, boolean value);

    default Decorator withCustomBaseDecoration(BaseDecorType type, Chunk decorator) {
        getCustomBaseDecor(type).add(decorator);
        return this;
    }

    default Decorator withCustomDecorations(DecorType type, Decor decorator) {
        getCustomDecor(type).add(decorator);
        return this;
    }

    default Decorator withoutDefaultDecorations() {
        for (DecorType type : DecorType.values()) {
            setDefaultDecoratorsEnabled(type, false);
        }
        for (BaseDecorType type : BaseDecorType.values()) {
            setDefaultBaseDecoratorsEnabled(type, false);
        }
        return this;
    }

    default Decorator withoutBaseDecorations(BaseDecorType type) {
        setDefaultBaseDecoratorsEnabled(type, false);
        return this;
    }

    default Decorator withoutDecorations(DecorType type) {
        setDefaultDecoratorsEnabled(type, false);
        return this;
    }
}
