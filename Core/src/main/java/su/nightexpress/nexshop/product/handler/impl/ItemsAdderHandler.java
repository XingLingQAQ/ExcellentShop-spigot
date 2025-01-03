package su.nightexpress.nexshop.product.handler.impl;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nexshop.ShopPlugin;
import su.nightexpress.nexshop.api.shop.packer.PluginItemPacker;
import su.nightexpress.nexshop.hook.HookId;
import su.nightexpress.nexshop.product.handler.AbstractPluginItemHandler;
import su.nightexpress.nexshop.product.packer.impl.UniversalPluginItemPacker;

public class ItemsAdderHandler extends AbstractPluginItemHandler {

    public ItemsAdderHandler(@NotNull ShopPlugin plugin) {
        super(plugin);
    }

    @Override
    @NotNull
    public String getName() {
        return HookId.ITEMS_ADDER;
    }

    @Override
    @NotNull
    public PluginItemPacker createPacker(@NotNull String itemId, int amount) {
        return new UniversalPluginItemPacker<>(this, itemId, amount);
    }

    @Override
    @Nullable
    public ItemStack createItem(@NotNull String itemId) {
        CustomStack customStack = CustomStack.getInstance(itemId);
        return customStack == null ? null : customStack.getItemStack();
    }

    @Override
    public boolean canHandle(@NotNull ItemStack item) {
        return CustomStack.byItemStack(item) != null;
    }

    @Override
    public boolean isValidId(@NotNull String itemId) {
        return CustomStack.isInRegistry(itemId);
    }

    @Override
    @Nullable
    public String getItemId(@NotNull ItemStack item) {
        if (item.getType().isAir()) return null;

        CustomStack stack = CustomStack.byItemStack(item);
        return stack == null ? null : stack.getNamespacedID();
    }
}
