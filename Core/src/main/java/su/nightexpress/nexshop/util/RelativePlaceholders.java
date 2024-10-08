package su.nightexpress.nexshop.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.util.Pair;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class RelativePlaceholders<T> {

    private final List<Pair<String, Function<T, String>>> keys;

    public RelativePlaceholders() {
        this(new ArrayList<>());
    }

    public RelativePlaceholders(@NotNull RelativePlaceholders<T> other) {
        this(other.getKeys());
    }

    public RelativePlaceholders(@NotNull List<Pair<String, Function<T, String>>> keys) {
        this.keys = new ArrayList<>(keys);
    }

    @NotNull
    public List<Pair<String, Function<T, String>>> getKeys() {
        return keys;
    }

    @NotNull
    public RelativePlaceholders<T> add(@NotNull RelativePlaceholders<T> other) {
        this.keys.addAll(other.getKeys());
        return this;
    }

    @NotNull
    public RelativePlaceholders<T> add(@NotNull String key, @NotNull Function<T, String> replacer) {
        this.getKeys().add(Pair.of(key, replacer));
        return this;
    }

    public void clear() {
        this.getKeys().clear();
    }

    @NotNull
    public PlaceholderMap toNormal(@Nullable T object) {
        List<Pair<String, Supplier<String>>> list = new ArrayList<>();
        this.keys.forEach(pair -> {
            list.add(Pair.of(pair.getFirst(), () -> pair.getSecond().apply(object)));
        });

        return new PlaceholderMap(list);
    }
}
