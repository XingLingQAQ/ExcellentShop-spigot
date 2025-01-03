package su.nightexpress.nexshop.shop.virtual;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nexshop.ShopPlugin;
import su.nightexpress.economybridge.api.Currency;
import su.nightexpress.nexshop.Placeholders;
import su.nightexpress.nexshop.api.shop.type.PriceType;
import su.nightexpress.nexshop.api.shop.type.TradeType;
import su.nightexpress.nexshop.product.ProductHandlerRegistry;
import su.nightexpress.nexshop.product.price.AbstractProductPricer;
import su.nightexpress.nexshop.shop.impl.AbstractVirtualShop;
import su.nightexpress.nexshop.product.handler.impl.BukkitItemHandler;
import su.nightexpress.nexshop.product.packer.impl.BukkitItemPacker;
import su.nightexpress.nexshop.shop.virtual.impl.RotatingProduct;
import su.nightexpress.nexshop.shop.virtual.impl.RotatingShop;
import su.nightexpress.nexshop.shop.virtual.impl.StaticProduct;
import su.nightexpress.nexshop.shop.virtual.impl.StaticShop;
import su.nightexpress.nexshop.shop.virtual.menu.ShopLayout;
import su.nightexpress.nexshop.shop.virtual.type.RotationType;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.wrapper.UniDouble;
import su.nightexpress.nightcore.util.wrapper.UniInt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class ShopCreator {

    private final ShopPlugin plugin;
    private final VirtualShopModule module;
    private final Currency currency;

    public ShopCreator(@NotNull ShopPlugin plugin, @NotNull VirtualShopModule module) {
        this.plugin = plugin;
        this.module = module;
        this.currency = this.module.getDefaultCurrency();
    }

    public void createDefaults() {
        this.createLayouts();
        this.createVirtual();
        this.createRotating();
    }

    private void createLayouts() {
        File dir = new File(this.module.getAbsolutePath(), VirtualShopModule.DIR_LAYOUTS);
        if (dir.exists()) return;

        dir.mkdirs();

        File file = new File(dir.getAbsolutePath(), Placeholders.DEFAULT + ".yml");
        FileUtil.create(file);
        FileConfig config = new FileConfig(file);

        new ShopLayout(this.plugin, this.module, config);
    }

    private void createVirtual() {
        File dir = new File(this.module.getAbsolutePath(), VirtualShopModule.DIR_SHOPS);
        if (dir.exists()) return;

        dir.mkdirs();

        this.createBlockShop();
        this.createIngredientsShop();
        this.createFarmerShop();
        this.createFishShop();
        this.createFoodShop();
        this.createHostileLootShop();
        this.createPeacefulLoot();
        this.createToolShop();
        this.createWeaponShop();
        this.createWoolShop();
    }

    private void createRotating() {
        File dir = new File(this.module.getAbsolutePath(), VirtualShopModule.DIR_ROTATING_SHOPS);
        if (dir.exists()) return;

        dir.mkdirs();

        this.createTravellerShop();
    }

    private void createBlockShop() {
        String name = YELLOW.enclose(BOLD.enclose("Block Market"));
        List<String> desc = List.of(GRAY.enclose("Building blocks and resources."));

        StaticShop shop = this.createStaticShop("blocks", name, desc, new ItemStack(Material.JUNGLE_LOG), 3, 14);

        this.addShopProduct(shop, Material.STONE, 50, 20, 1, 0);
        this.addShopProduct(shop, Material.STONE_BRICKS, 50, 20, 1, 1);
        this.addShopProduct(shop, Material.CHISELED_STONE_BRICKS, 50, 20, 1, 2);
        this.addShopProduct(shop, Material.COBBLESTONE, 20, 10, 1, 3);
        this.addShopProduct(shop, Material.MOSSY_COBBLESTONE, 50, 20, 1, 4);
        this.addShopProduct(shop, Material.MOSSY_STONE_BRICKS, 60, 30, 1, 5);
        this.addShopProduct(shop, Material.GRASS_BLOCK, 50, 20, 1, 6);
        this.addShopProduct(shop, Material.RED_SAND, 50, 20, 1, 7);
        this.addShopProduct(shop, Material.SAND, 30, 15, 1, 8);
        this.addShopProduct(shop, Material.GRANITE, 50, 20, 1, 9);
        this.addShopProduct(shop, Material.POLISHED_GRANITE, 60, 30, 1, 10);
        this.addShopProduct(shop, Material.DIORITE, 50, 20, 1, 11);
        this.addShopProduct(shop, Material.ANDESITE, 50, 20, 1, 12);
        this.addShopProduct(shop, Material.GRAVEL, 30, 15, 1, 13);
        this.addShopProduct(shop, Material.PODZOL, 50, 20, 1, 14);
        this.addShopProduct(shop, Material.DIRT, 10, 5, 1, 15);
        this.addShopProduct(shop, Material.RED_SANDSTONE, 50, 20, 1, 16);
        this.addShopProduct(shop, Material.SANDSTONE, 50, 20, 1, 17);
        this.addShopProduct(shop, Material.DEEPSLATE, 60, 30, 1, 18);
        this.addShopProduct(shop, Material.COBBLED_DEEPSLATE, 40, 20, 1, 19);
        this.addShopProduct(shop, Material.CHISELED_DEEPSLATE, 60, 30, 1, 20);
        this.addShopProduct(shop, Material.POLISHED_DEEPSLATE, 70, 40, 1, 21);
        this.addShopProduct(shop, Material.TUFF, 50, 20, 1, 22);
        this.addShopProduct(shop, Material.MYCELIUM, 50, 20, 1, 23);
        this.addShopProduct(shop, Material.PACKED_MUD, 60, 40, 1, 24);
        this.addShopProduct(shop, Material.CHISELED_RED_SANDSTONE, 50, 20, 1, 25);
        this.addShopProduct(shop, Material.CHISELED_SANDSTONE, 50, 20, 1, 26);
        this.addShopProduct(shop, Material.DEEPSLATE_BRICKS, 70, 35, 1, 27);
        this.addShopProduct(shop, Material.CRACKED_DEEPSLATE_BRICKS, 80, 45, 1, 28);
        this.addShopProduct(shop, Material.DEEPSLATE_TILES, 80, 45, 1, 29);
        this.addShopProduct(shop, Material.CRACKED_DEEPSLATE_TILES, 90, 50, 1, 30);
        this.addShopProduct(shop, Material.CLAY, 50, 20, 1, 31);
        this.addShopProduct(shop, Material.BRICKS, 60, 30, 1, 32);
        this.addShopProduct(shop, Material.MUD_BRICKS, 60, 45, 1, 33);
        this.addShopProduct(shop, Material.SMOOTH_RED_SANDSTONE, 50, 20, 1, 34);
        this.addShopProduct(shop, Material.SMOOTH_SANDSTONE, 50, 20, 1, 35);
        this.addShopProduct(shop, Material.PRISMARINE, 50, 20, 1, 36);
        this.addShopProduct(shop, Material.PRISMARINE_BRICKS, 50, 20, 1, 37);
        this.addShopProduct(shop, Material.DARK_PRISMARINE, 50, 20, 1, 38);
        this.addShopProduct(shop, Material.SLIME_BLOCK, 50, 20, 1, 40);
        this.addShopProduct(shop, Material.CALCITE, 500, 250, 1, 42);
        this.addShopProduct(shop, Material.CUT_RED_SANDSTONE, 50, 20, 1, 43);
        this.addShopProduct(shop, Material.CUT_SANDSTONE, 50, 20, 1, 44);
        this.addShopProduct(shop, Material.OAK_LOG, 50, 20, 2, 0);
        this.addShopProduct(shop, Material.OAK_PLANKS, 50, 20, 2, 1);
        this.addShopProduct(shop, Material.MANGROVE_LOG, 50, 20, 2, 2);
        this.addShopProduct(shop, Material.MANGROVE_PLANKS, 50, 20, 2, 3);
        this.addShopProduct(shop, Material.NETHERRACK, 50, 20, 2, 4);
        this.addShopProduct(shop, Material.NETHER_BRICKS, 50, 20, 2, 5);
        this.addShopProduct(shop, Material.RED_NETHER_BRICKS, 50, 20, 2, 6);
        this.addShopProduct(shop, Material.PURPUR_BLOCK, 50, 20, 2, 7);
        this.addShopProduct(shop, Material.PURPUR_PILLAR, 50, 20, 2, 8);
        this.addShopProduct(shop, Material.SPRUCE_LOG, 50, 20, 2, 9);
        this.addShopProduct(shop, Material.SPRUCE_PLANKS, 50, 20, 2, 10);
        this.addShopProduct(shop, Material.CRIMSON_STEM, 50, 20, 2, 11);
        this.addShopProduct(shop, Material.CRIMSON_PLANKS, 50, 20, 2, 12);
        this.addShopProduct(shop, Material.BASALT, 50, 20, 2, 13);
        this.addShopProduct(shop, Material.POLISHED_BASALT, 50, 20, 2, 14);
        this.addShopProduct(shop, Material.SMOOTH_BASALT, -1, -1, 2, 15);
        this.addShopProduct(shop, Material.END_STONE, 50, 20, 2, 16);
        this.addShopProduct(shop, Material.END_STONE_BRICKS, 50, 20, 2, 17);
        this.addShopProduct(shop, Material.BIRCH_LOG, 50, 20, 2, 18);
        this.addShopProduct(shop, Material.BIRCH_PLANKS, 50, 20, 2, 19);
        this.addShopProduct(shop, Material.WARPED_STEM, 50, 20, 2, 20);
        this.addShopProduct(shop, Material.WARPED_PLANKS, 50, 20, 2, 21);
        this.addShopProduct(shop, Material.OBSIDIAN, 50, 20, 2, 22);
        this.addShopProduct(shop, Material.CRYING_OBSIDIAN, 100, 50, 2, 23);
        this.addShopProduct(shop, Material.MAGMA_BLOCK, 50, 20, 2, 24);
        this.addShopProduct(shop, Material.QUARTZ_BLOCK, 50, 20, 2, 25);
        this.addShopProduct(shop, Material.QUARTZ_BRICKS, 50, 20, 2, 26);
        this.addShopProduct(shop, Material.JUNGLE_LOG, 50, 20, 2, 27);
        this.addShopProduct(shop, Material.JUNGLE_PLANKS, 50, 20, 2, 28);
        this.addShopProduct(shop, Material.BLACKSTONE, 50, 20, 2, 29);
        this.addShopProduct(shop, Material.GILDED_BLACKSTONE, 50, 20, 2, 30);
        this.addShopProduct(shop, Material.CHISELED_POLISHED_BLACKSTONE, 50, 20, 2, 31);
        this.addShopProduct(shop, Material.POLISHED_BLACKSTONE, 50, 20, 2, 32);
        this.addShopProduct(shop, Material.POLISHED_BLACKSTONE_BRICKS, 50, 20, 2, 33);
        this.addShopProduct(shop, Material.CRACKED_POLISHED_BLACKSTONE_BRICKS, 50, 20, 2, 34);
        this.addShopProduct(shop, Material.SOUL_SAND, 50, 20, 2, 35);
        this.addShopProduct(shop, Material.ACACIA_LOG, 50, 20, 2, 36);
        this.addShopProduct(shop, Material.ACACIA_PLANKS, 50, 20, 2, 37);
        this.addShopProduct(shop, Material.DARK_OAK_LOG, 50, 20, 2, 38);
        this.addShopProduct(shop, Material.DARK_OAK_PLANKS, -1, -1, 2, 39);
        this.addShopProduct(shop, Material.BONE_BLOCK, 50, 20, 2, 40);
        this.addShopProduct(shop, Material.GLASS, 50, 20, 2, 41);
        this.addShopProduct(shop, Material.WARPED_NYLIUM, 50, 20, 2, 42);
        this.addShopProduct(shop, Material.CRIMSON_NYLIUM, 50, 20, 2, 43);
        this.addShopProduct(shop, Material.SOUL_SOIL, 50, 20, 2, 44);
        this.addShopProduct(shop, Material.COPPER_BLOCK, 50, 20, 3, 0);
        this.addShopProduct(shop, Material.CUT_COPPER, 50, 20, 3, 1);
        this.addShopProduct(shop, Material.EXPOSED_COPPER, 50, 20, 3, 2);
        this.addShopProduct(shop, Material.EXPOSED_CUT_COPPER, 50, 20, 3, 3);
        this.addShopProduct(shop, Material.RED_MUSHROOM_BLOCK, 50, 20, 3, 4);
        this.addShopProduct(shop, Material.ANCIENT_DEBRIS, 10_000, 10_000, 3, 6);
        this.addShopProduct(shop, Material.SCULK, 200, 100, 3, 7);
        this.addShopProduct(shop, Material.MOSS_BLOCK, 50, 20, 3, 8);
        this.addShopProduct(shop, Material.WEATHERED_COPPER, 50, 20, 3, 9);
        this.addShopProduct(shop, Material.WEATHERED_CUT_COPPER, 50, 20, 3, 10);
        this.addShopProduct(shop, Material.OXIDIZED_COPPER, 50, 20, 3, 11);
        this.addShopProduct(shop, Material.OXIDIZED_CUT_COPPER, -1, -1, 3, 12);
        this.addShopProduct(shop, Material.BROWN_MUSHROOM_BLOCK, 50, 20, 3, 13);
        this.addShopProduct(shop, Material.SCULK_CATALYST, 5_000, 2_500, 3, 16);
        this.addShopProduct(shop, Material.MOSS_CARPET, 50, 20, 3, 17);
        this.addShopProduct(shop, Material.WAXED_COPPER_BLOCK, 50, 20, 3, 18);
        this.addShopProduct(shop, Material.WAXED_CUT_COPPER, 50, 20, 3, 19);
        this.addShopProduct(shop, Material.WAXED_EXPOSED_COPPER, 50, 20, 3, 20);
        this.addShopProduct(shop, Material.WAXED_EXPOSED_CUT_COPPER, 50, 20, 3, 21);
        this.addShopProduct(shop, Material.MUSHROOM_STEM, 50, 20, 3, 22);
        this.addShopProduct(shop, Material.SCULK_SENSOR, 10_000, 10_000, 3, 25);
        this.addShopProduct(shop, Material.WAXED_WEATHERED_COPPER, 50, 20, 3, 27);
        this.addShopProduct(shop, Material.WAXED_WEATHERED_CUT_COPPER, 50, 20, 3, 28);
        this.addShopProduct(shop, Material.WAXED_OXIDIZED_COPPER, 50, 20, 3, 29);
        this.addShopProduct(shop, Material.WAXED_OXIDIZED_CUT_COPPER, -1, -1, 3, 30);
        this.addShopProduct(shop, Material.NETHER_WART_BLOCK, 50, 20, 3, 31);
        this.addShopProduct(shop, Material.SCULK_VEIN, 10_000, 10_000, 3, 34);
        this.addShopProduct(shop, Material.PACKED_ICE, 50, 20, 3, 36);
        this.addShopProduct(shop, Material.ICE, 50, 20, 3, 37);
        this.addShopProduct(shop, Material.SNOW_BLOCK, 50, 20, 3, 38);
        this.addShopProduct(shop, Material.WARPED_WART_BLOCK, 50, 20, 3, 40);
        this.addShopProduct(shop, Material.AMETHYST_BLOCK, 50, 20, 3, 43);
        this.addShopProduct(shop, Material.BUDDING_AMETHYST, 10_000, 5_000, 3, 44);

        shop.save();
    }

    private void createIngredientsShop() {
        String name = LIGHT_RED.enclose(BOLD.enclose("Ingredients"));
        List<String> desc = List.of(GRAY.enclose("Potions and Ingredients."));

        StaticShop shop = this.createStaticShop("ingredients", name, desc, new ItemStack(Material.REDSTONE), 1, 12);

        this.addShopProduct(shop, Material.COAL, 10, 10, 1, 0);
        this.addShopProduct(shop, Material.IRON_INGOT, 100, 100, 1, 1);
        this.addShopProduct(shop, Material.GOLD_INGOT, 100, 100, 1, 2);
        this.addShopProduct(shop, Material.NETHERITE_INGOT, 100_000, 100_000, 1, 3);
        this.addShopProduct(shop, Material.REDSTONE, 20, 20, 1, 4);
        this.addShopProduct(shop, Material.DIAMOND, 1_000, 1_000, 1, 5);
        this.addShopProduct(shop, Material.EMERALD, 1_000, 1_000, 1, 6);
        this.addShopProduct(shop, Material.LAPIS_LAZULI, 20, 20, 1, 7);
        this.addShopProduct(shop, Material.COPPER_INGOT, 1_000, 1_000, 1, 8);
        this.addShopProduct(shop, Material.QUARTZ, 50, 20, 1, 9);
        this.addShopProduct(shop, Material.SNOWBALL, 20, 10, 1, 10);
        this.addShopProduct(shop, Material.AMETHYST_SHARD, 50, 20, 1, 11);
        this.addShopProduct(shop, Material.COBWEB, 50, 20, 1, 12);
        this.addShopProduct(shop, Material.FLINT, 50, 20, 1, 13);
        this.addShopProduct(shop, Material.GLASS_BOTTLE, 50, 20, 1, 14);
        this.addShopProduct(shop, Material.POTION, 50, 20, 1, 15);
        this.addShopProduct(shop, Material.GLISTERING_MELON_SLICE, 50, 20, 1, 16);
        this.addShopProduct(shop, Material.RABBIT_FOOT, 50, 20, 1, 17);
        this.addShopProduct(shop, Material.GOLDEN_CARROT, 1_000, 500, 1, 18);
        this.addShopProduct(shop, Material.PHANTOM_MEMBRANE, 50, 20, 1, 19);
        this.addShopProduct(shop, Material.FERMENTED_SPIDER_EYE, 50, 20, 1, 20);
        this.addShopProduct(shop, Material.BLAZE_POWDER, 50, 20, 1, 21);
        this.addShopProduct(shop, Material.MAGMA_CREAM, 150, 75, 1, 22);
        this.addShopProduct(shop, Material.GHAST_TEAR, 500, 250, 1, 23);
        this.addShopProduct(shop, Material.CAULDRON, 50, 20, 1, 34);
        this.addShopProduct(shop, Material.BREWING_STAND, 50, 20, 1, 35);

        shop.save();
    }

    private void createFarmerShop() {
        String name = LIGHT_YELLOW.enclose(BOLD.enclose("Farmer's Market"));
        List<String> desc = List.of(GRAY.enclose("Everything your garden need."));

        StaticShop shop = this.createStaticShop("farmers_market", name, desc, new ItemStack(Material.WHEAT), 2, 23);

        this.addShopProduct(shop, Material.OAK_SAPLING, 50, 20, 1, 0);
        this.addShopProduct(shop, Material.SPRUCE_SAPLING, 50, 20, 1, 1);
        this.addShopProduct(shop, Material.BIRCH_SAPLING, 50, 20, 1, 2);
        this.addShopProduct(shop, Material.JUNGLE_SAPLING, 50, 20, 1, 3);
        this.addShopProduct(shop, Material.ACACIA_SAPLING, 50, 20, 1, 4);
        this.addShopProduct(shop, Material.DARK_OAK_SAPLING, 50, 20, 1, 5);
        this.addShopProduct(shop, Material.AZALEA, 50, 20, 1, 6);
        this.addShopProduct(shop, Material.FLOWERING_AZALEA, 50, 20, 1, 7);
        this.addShopProduct(shop, Material.MANGROVE_PROPAGULE, 50, 20, 1, 8);
        this.addShopProduct(shop, Material.RED_MUSHROOM, 50, 20, 1, 9);
        this.addShopProduct(shop, Material.BROWN_MUSHROOM, 50, 20, 1, 10);
        this.addShopProduct(shop, Material.WARPED_FUNGUS, 50, 20, 1, 11);
        this.addShopProduct(shop, Material.CRIMSON_FUNGUS, 50, 20, 1, 12);
        this.addShopProduct(shop, Material.NETHER_WART, 50, 20, 1, 13);
        this.addShopProduct(shop, Material.SHROOMLIGHT, 50, 20, 1, 17);
        this.addShopProduct(shop, Material.POPPY, 50, 20, 1, 18);
        this.addShopProduct(shop, Material.BLUE_ORCHID, 50, 20, 1, 19);
        this.addShopProduct(shop, Material.ALLIUM, 50, 20, 1, 20);
        this.addShopProduct(shop, Material.AZURE_BLUET, 50, 20, 1, 21);
        this.addShopProduct(shop, Material.ORANGE_TULIP, 50, 20, 1, 22);
        this.addShopProduct(shop, Material.WHITE_TULIP, 50, 20, 1, 23);
        this.addShopProduct(shop, Material.PINK_TULIP, 50, 20, 1, 24);
        this.addShopProduct(shop, Material.RED_TULIP, 50, 20, 1, 25);
        this.addShopProduct(shop, Material.DANDELION, 50, 20, 1, 26);
        this.addShopProduct(shop, Material.LILY_OF_THE_VALLEY, 50, 20, 1, 27);
        this.addShopProduct(shop, Material.WITHER_ROSE, 500, 250, 1, 28);
        this.addShopProduct(shop, Material.SUNFLOWER, 50, 20, 1, 29);
        this.addShopProduct(shop, Material.OXEYE_DAISY, 50, 20, 1, 30);
        this.addShopProduct(shop, Material.LILAC, 50, 20, 1, 31);
        this.addShopProduct(shop, Material.CORNFLOWER, 50, 20, 1, 32);
        this.addShopProduct(shop, Material.PEONY, 50, 20, 1, 33);
        this.addShopProduct(shop, Material.ROSE_BUSH, 50, 20, 1, 34);
        this.addShopProduct(shop, Material.CHORUS_FLOWER, 50, 20, 1, 35);
        this.addShopProduct(shop, Material.SUGAR_CANE, 50, 20, 1, 36);
        this.addShopProduct(shop, Material.CACTUS, 50, 20, 1, 37);
        this.addShopProduct(shop, Material.BAMBOO, 50, 20, 1, 38);
        this.addShopProduct(shop, Material.WEEPING_VINES, 50, 20, 1, 39);
        this.addShopProduct(shop, Material.CRIMSON_ROOTS, 50, 20, 1, 40);
        this.addShopProduct(shop, Material.WARPED_ROOTS, 50, 20, 1, 41);
        this.addShopProduct(shop, Material.TWISTING_VINES, 50, 20, 1, 42);
        this.addShopProduct(shop, Material.NETHER_SPROUTS, 50, 20, 1, 43);
        this.addShopProduct(shop, Material.SPORE_BLOSSOM, 50, 20, 1, 44);
        this.addShopProduct(shop, Material.WHEAT_SEEDS, 50, 20, 2, 0);
        this.addShopProduct(shop, Material.BEETROOT_SEEDS, 50, 20, 2, 1);
        this.addShopProduct(shop, Material.PUMPKIN_SEEDS, 50, 20, 2, 2);
        this.addShopProduct(shop, Material.MELON_SEEDS, 50, 20, 2, 3);
        this.addShopProduct(shop, Material.COCOA_BEANS, 50, 20, 2, 4);
        this.addShopProduct(shop, Material.POTATO, 50, 20, 2, 5);
        this.addShopProduct(shop, Material.KELP, 50, 20, 2, 6);
        this.addShopProduct(shop, Material.WHEAT, 50, 20, 2, 9);
        this.addShopProduct(shop, Material.BEETROOT, 50, 20, 2, 10);
        this.addShopProduct(shop, Material.PUMPKIN, 50, 20, 2, 11);
        this.addShopProduct(shop, Material.MELON, 50, 20, 2, 12);
        this.addShopProduct(shop, Material.CARROT, 50, 20, 2, 13);
        this.addShopProduct(shop, Material.SUGAR, 50, 20, 2, 14);
        this.addShopProduct(shop, Material.DRIED_KELP, 60, 30, 2, 15);
        this.addShopProduct(shop, Material.HAY_BLOCK, 450, 225, 2, 18);
        this.addShopProduct(shop, Material.BEE_NEST, 500, 250, 2, 19);
        this.addShopProduct(shop, Material.HONEYCOMB_BLOCK, 500, 250, 2, 20);
        this.addShopProduct(shop, Material.HONEY_BLOCK, 1_000, 500, 2, 21);
        this.addShopProduct(shop, Material.CHORUS_PLANT, 50, 20, 2, 23);
        this.addShopProduct(shop, Material.DRIED_KELP_BLOCK, 540, 270, 2, 24);
        this.addShopProduct(shop, Material.SEAGRASS, 50, 20, 2, 27);
        this.addShopProduct(shop, Material.SEA_PICKLE, 50, 20, 2, 28);
        this.addShopProduct(shop, Material.HANGING_ROOTS, 50, 20, 2, 29);
        this.addShopProduct(shop, Material.SPONGE, 1_000, 500, 2, 30);
        this.addShopProduct(shop, Material.TURTLE_EGG, 1_000, 500, 2, 31);
        this.addShopProduct(shop, Material.FROGSPAWN, 500, 20, 2, 32);
        this.addShopProduct(shop, Material.BONE_MEAL, 50, 20, 2, 33);

        shop.save();
    }

    private void createFishShop() {
        String name = LIGHT_ORANGE.enclose(BOLD.enclose("Fish Market"));
        List<String> desc = List.of(GRAY.enclose("Everything you can obtain"), GRAY.enclose("when fishing."));

        StaticShop shop = this.createStaticShop("fish_market", name, desc, new ItemStack(Material.TROPICAL_FISH), 1, 21);

        this.addShopProduct(shop, Material.COD_SPAWN_EGG, 200, 100, 1, 0);
        this.addShopProduct(shop, Material.SALMON_SPAWN_EGG, 200, 100, 1, 1);
        this.addShopProduct(shop, Material.TROPICAL_FISH_SPAWN_EGG, 200, 100, 1, 2);
        this.addShopProduct(shop, Material.PUFFERFISH_SPAWN_EGG, 200, 100, 1, 3);
        this.addShopProduct(shop, Material.TRIPWIRE_HOOK, 1_000, 500, 1, 6);
        this.addShopProduct(shop, Material.LILY_PAD, 50, 20, 1, 7);
        this.addShopProduct(shop, Material.LEATHER_BOOTS, 50, 20, 1, 8);
        this.addShopProduct(shop, Material.COOKED_COD, 75, 75, 1, 9);
        this.addShopProduct(shop, Material.COOKED_SALMON, 75, 75, 1, 10);
        this.addShopProduct(shop, Material.TROPICAL_FISH, 300, 300, 1, 11);
        this.addShopProduct(shop, Material.PUFFERFISH, 50, 50, 1, 12);
        this.addShopProduct(shop, Material.INK_SAC, 50, 20, 1, 16);
        this.addShopProduct(shop, Material.BONE, 50, 20, 1, 17);
        this.addShopProduct(shop, Material.COD, 50, 50, 1, 18);
        this.addShopProduct(shop, Material.SALMON, 50, 50, 1, 19);
        this.addShopProduct(shop, Material.NAUTILUS_SHELL, 10_000, 5_000, 1, 25);
        this.addShopProduct(shop, Material.SADDLE, 1_000, 500, 1, 26);
        this.addShopProduct(shop, Material.BONE_MEAL, 50, 20, 1, 27);
        this.addShopProduct(shop, Material.BONE_MEAL, 50, 20, 1, 28);

        shop.save();
    }

    private void createFoodShop() {
        String name = LIGHT_RED.enclose(BOLD.enclose("Food Market"));
        List<String> desc = List.of(GRAY.enclose("All regular food."));

        StaticShop shop = this.createStaticShop("food", name, desc, new ItemStack(Material.SWEET_BERRIES), 1, 22);

        this.addShopProduct(shop, Material.APPLE, 50, 20, 1, 0);
        this.addShopProduct(shop, Material.GOLDEN_APPLE, 1_000, 500, 1, 1);
        this.addShopProduct(shop, Material.ENCHANTED_GOLDEN_APPLE, 50_000, 25_000, 1, 2);
        this.addShopProduct(shop, Material.BREAD, 50, 20, 1, 7);
        this.addShopProduct(shop, Material.RABBIT_STEW, 500, 250, 1, 8);
        this.addShopProduct(shop, Material.COOKIE, 50, 20, 1, 9);
        this.addShopProduct(shop, Material.PUMPKIN_PIE, 50, 20, 1, 10);
        this.addShopProduct(shop, Material.CAKE, 50, 20, 1, 11);
        this.addShopProduct(shop, Material.MUSHROOM_STEW, 50, 20, 1, 17);
        this.addShopProduct(shop, Material.MELON_SLICE, 50, 20, 1, 18);
        this.addShopProduct(shop, Material.CARROT, 50, 20, 1, 19);
        this.addShopProduct(shop, Material.GOLDEN_CARROT, 1_000, 500, 1, 20);
        this.addShopProduct(shop, Material.BAKED_POTATO, 75, 50, 1, 21);
        this.addShopProduct(shop, Material.BOWL, 50, 20, 1, 26);
        this.addShopProduct(shop, Material.COOKED_PORKCHOP, 100, 50, 1, 27);
        this.addShopProduct(shop, Material.COOKED_BEEF, 100, 50, 1, 28);
        this.addShopProduct(shop, Material.COOKED_MUTTON, 100, 50, 1, 29);
        this.addShopProduct(shop, Material.COOKED_RABBIT, 100, 50, 1, 30);
        this.addShopProduct(shop, Material.COOKED_CHICKEN, 100, 50, 1, 31);
        this.addShopProduct(shop, Material.COOKED_SALMON, 100, 50, 1, 32);
        this.addShopProduct(shop, Material.COOKED_COD, 100, 50, 1, 33);
        this.addShopProduct(shop, Material.SWEET_BERRIES, 50, 20, 1, 36);
        this.addShopProduct(shop, Material.GLOW_BERRIES, 50, 20, 1, 37);
        this.addShopProduct(shop, Material.CHORUS_FRUIT, 50, 20, 1, 38);

        shop.save();
    }

    private void createHostileLootShop() {
        String name = LIGHT_GREEN.enclose(BOLD.enclose("Hostile Loot"));
        List<String> desc = List.of(GRAY.enclose("Everything related to"), GRAY.enclose("hostile mobs and their loot."));

        StaticShop shop = this.createStaticShop("hostile_loot", name, desc, new ItemStack(Material.CREEPER_HEAD), 4, 16);

        this.addShopProduct(shop, Material.SPIDER_SPAWN_EGG, 100, 50, 1, 0);
        this.addShopProduct(shop, Material.SKELETON_SPAWN_EGG, 100, 50, 1, 1);
        this.addShopProduct(shop, Material.CREEPER_SPAWN_EGG, 100, 50, 1, 2);
        this.addShopProduct(shop, Material.SLIME_SPAWN_EGG, 150, 75, 1, 3);
        this.addShopProduct(shop, Material.SHULKER_SHELL, 500, 250, 1, 4);
        this.addShopProduct(shop, Material.ENDERMAN_SPAWN_EGG, 100, 50, 1, 5);
        this.addShopProduct(shop, Material.ENDER_DRAGON_SPAWN_EGG, 2_500_000, 1_250_000, 1, 6);
        this.addShopProduct(shop, Material.CAVE_SPIDER_SPAWN_EGG, 100, 50, 1, 7);
        this.addShopProduct(shop, Material.ZOMBIE_SPAWN_EGG, 100, 50, 1, 8);
        this.addShopProduct(shop, Material.SPIDER_EYE, 80, 40, 1, 9);
        this.addShopProduct(shop, Material.BONE, 50, 20, 1, 10);
        this.addShopProduct(shop, Material.GUNPOWDER, 50, 20, 1, 11);
        this.addShopProduct(shop, Material.SLIME_BALL, 100, 50, 1, 12);
        this.addShopProduct(shop, Material.ENDER_PEARL, 100, 50, 1, 14);
        this.addShopProduct(shop, Material.DRAGON_EGG, 20_000_000, 10_000_000, 1, 15);
        this.addShopProduct(shop, Material.ROTTEN_FLESH, 50, 20, 1, 17);
        this.addShopProduct(shop, Material.STRING, 50, 20, 1, 18);
        this.addShopProduct(shop, Material.BONE_MEAL, 50, 20, 1, 19);
        this.addShopProduct(shop, Material.IRON_INGOT, 100, 100, 1, 26);
        this.addShopProduct(shop, Material.ARROW, 10, 5, 1, 28);
        this.addShopProduct(shop, Material.BAKED_POTATO, 75, 50, 1, 34);
        this.addShopProduct(shop, Material.IRON_NUGGET, 10, 10, 1, 35);
        this.addShopProduct(shop, Material.POTATO, 50, 20, 1, 43);
        this.addShopProduct(shop, Material.CARROT, 50, 20, 1, 44);
        this.addShopProduct(shop, Material.BLAZE_SPAWN_EGG, 200, 100, 2, 0);
        this.addShopProduct(shop, Material.MAGMA_CUBE_SPAWN_EGG, 200, 100, 2, 1);
        this.addShopProduct(shop, Material.STRIDER_SPAWN_EGG, 300, 150, 2, 2);
        this.addShopProduct(shop, Material.HOGLIN_SPAWN_EGG, 150, 75, 2, 3);
        this.addShopProduct(shop, Material.PIGLIN_SPAWN_EGG, 150, 75, 2, 4);
        this.addShopProduct(shop, Material.ZOGLIN_SPAWN_EGG, 150, 75, 2, 5);
        this.addShopProduct(shop, Material.ZOMBIFIED_PIGLIN_SPAWN_EGG, 150, 75, 2, 6);
        this.addShopProduct(shop, Material.GHAST_SPAWN_EGG, 1_000, 500, 2, 7);
        this.addShopProduct(shop, Material.WITHER_SKELETON_SPAWN_EGG, 10_000, 1_000, 2, 8);
        this.addShopProduct(shop, Material.BLAZE_ROD, 200, 100, 2, 9);
        this.addShopProduct(shop, Material.MAGMA_CREAM, 150, 75, 2, 10);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 2, 11);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 2, 12);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 2, 14);
        this.addShopProduct(shop, Material.ROTTEN_FLESH, 50, 20, 2, 15);
        this.addShopProduct(shop, Material.GUNPOWDER, 50, 20, 2, 16);
        this.addShopProduct(shop, Material.BONE, 50, 20, 2, 17);
        this.addShopProduct(shop, Material.SADDLE, 1_000, 500, 2, 20);
        this.addShopProduct(shop, Material.COOKED_PORKCHOP, 100, 50, 2, 21);
        this.addShopProduct(shop, Material.GOLD_INGOT, 100, 100, 2, 24);
        this.addShopProduct(shop, Material.GHAST_TEAR, 500, 250, 2, 25);
        this.addShopProduct(shop, Material.BONE_MEAL, 50, 20, 2, 26);
        this.addShopProduct(shop, Material.STRING, 50, 20, 2, 29);
        this.addShopProduct(shop, Material.PORKCHOP, 50, 20, 2, 30);
        this.addShopProduct(shop, Material.GOLD_NUGGET, 10, 10, 2, 33);
        this.addShopProduct(shop, Material.DROWNED_SPAWN_EGG, 150, 75, 3, 0);
        this.addShopProduct(shop, Material.HUSK_SPAWN_EGG, 150, 75, 3, 1);
        this.addShopProduct(shop, Material.EVOKER_SPAWN_EGG, 200, 100, 3, 2);
        this.addShopProduct(shop, Material.PILLAGER_SPAWN_EGG, 200, 100, 3, 3);
        this.addShopProduct(shop, Material.VINDICATOR_SPAWN_EGG, -1, -1, 3, 4);
        this.addShopProduct(shop, Material.RAVAGER_SPAWN_EGG, 300, 150, 3, 5);
        this.addShopProduct(shop, Material.WITCH_SPAWN_EGG, 200, 100, 3, 7);
        this.addShopProduct(shop, Material.ROTTEN_FLESH, 50, 20, 3, 9);
        this.addShopProduct(shop, Material.ROTTEN_FLESH, 50, 20, 3, 10);
        this.addShopProduct(shop, Material.TOTEM_OF_UNDYING, 5_000, 2_500, 3, 11);
        this.addShopProduct(shop, Material.CROSSBOW, 50, 20, 3, 12);
        this.addShopProduct(shop, Material.EMERALD, 1_000, 1_000, 3, 13);
        this.addShopProduct(shop, Material.SADDLE, 1_000, 500, 3, 14);
        this.addShopProduct(shop, Material.EMERALD, 1_000, 1_000, 3, 16);
        this.addShopProduct(shop, Material.STICK, 50, 20, 3, 17);
        this.addShopProduct(shop, Material.TRIDENT, 5_000, 2_000, 3, 18);
        this.addShopProduct(shop, Material.EMERALD, 1_000, 1_000, 3, 20);
        this.addShopProduct(shop, Material.SUGAR, 50, 50, 3, 25);
        this.addShopProduct(shop, Material.GLASS_BOTTLE, 50, 20, 3, 26);
        this.addShopProduct(shop, Material.NAUTILUS_SHELL, 10_000, 5_000, 3, 27);
        this.addShopProduct(shop, Material.GLOWSTONE_DUST, 50, 20, 3, 34);
        this.addShopProduct(shop, Material.REDSTONE, 50, 20, 3, 35);
        this.addShopProduct(shop, Material.COPPER_INGOT, 100, 100, 3, 36);
        this.addShopProduct(shop, Material.GUNPOWDER, 50, 20, 3, 43);
        this.addShopProduct(shop, Material.GUARDIAN_SPAWN_EGG, 5_000, 2_500, 4, 0);
        this.addShopProduct(shop, Material.ELDER_GUARDIAN_SPAWN_EGG, 10_000, 5_000, 4, 1);
        this.addShopProduct(shop, Material.STRAY_SPAWN_EGG, 1_000, 500, 4, 2);
        this.addShopProduct(shop, Material.PHANTOM_SPAWN_EGG, 1_000, 500, 4, 3);
        this.addShopProduct(shop, Material.VEX_SPAWN_EGG, 1_000, 500, 4, 4);
        this.addShopProduct(shop, Material.ENDERMITE_SPAWN_EGG, 5_000, 2_500, 4, 5);
        this.addShopProduct(shop, Material.ZOMBIE_VILLAGER_SPAWN_EGG, 300, 150, 4, 6);
        this.addShopProduct(shop, Material.WARDEN_SPAWN_EGG, 30_000_000, 0, 4, 8);
        this.addShopProduct(shop, Material.PRISMARINE_CRYSTALS, 50, 20, 4, 9);
        this.addShopProduct(shop, Material.PRISMARINE_CRYSTALS, 50, 20, 4, 10);
        this.addShopProduct(shop, Material.TIPPED_ARROW, -1, -1, 4, 11);
        this.addShopProduct(shop, Material.PHANTOM_MEMBRANE, 300, 150, 4, 12);
        this.addShopProduct(shop, Material.ROTTEN_FLESH, 50, 20, 4, 15);
        this.addShopProduct(shop, Material.SCULK_CATALYST, 50_000, 25_000, 4, 17);
        this.addShopProduct(shop, Material.PRISMARINE_SHARD, 100, 50, 4, 18);
        this.addShopProduct(shop, Material.PRISMARINE_SHARD, 100, 50, 4, 19);
        this.addShopProduct(shop, Material.ARROW, 10, 5, 4, 20);
        this.addShopProduct(shop, Material.PUFFERFISH, 50, 50, 4, 27);
        this.addShopProduct(shop, Material.WET_SPONGE, 1_000, 500, 4, 28);
        this.addShopProduct(shop, Material.BONE, 50, 20, 4, 29);
        this.addShopProduct(shop, Material.COOKED_COD, 100, 50, 4, 36);
        this.addShopProduct(shop, Material.COD, 50, 50, 4, 37);
        this.addShopProduct(shop, Material.BOW, 50, 20, 4, 38);

        shop.save();
    }

    private void createPeacefulLoot() {
        String name = LIGHT_PINK.enclose(BOLD.enclose("Peaceful Loot"));
        List<String> desc = List.of(GRAY.enclose("Everything related to"), GRAY.enclose("peaceful mobs and their loot."));
        ItemStack icon = ItemUtil.getSkinHead("9b1760e3778f8087046b86bec6a0a83a567625f30f0d6bce866d4bed95dba6c1");

        StaticShop shop = this.createStaticShop("peaceful_loot", name, desc, icon, 3, 15);

        this.addShopProduct(shop, Material.COW_SPAWN_EGG, 100, 50, 1, 0);
        this.addShopProduct(shop, Material.MOOSHROOM_SPAWN_EGG, 1_000, 500, 1, 1);
        this.addShopProduct(shop, Material.PIG_SPAWN_EGG, 100, 50, 1, 2);
        this.addShopProduct(shop, Material.SHEEP_SPAWN_EGG, 100, 50, 1, 3);
        this.addShopProduct(shop, Material.HORSE_SPAWN_EGG, 100, 50, 1, 4);
        this.addShopProduct(shop, Material.SKELETON_HORSE_SPAWN_EGG, 1_000, 500, 1, 5);
        this.addShopProduct(shop, Material.ZOMBIE_HORSE_SPAWN_EGG, 5_000, 2_500, 1, 6);
        this.addShopProduct(shop, Material.DONKEY_SPAWN_EGG, 100, 50, 1, 7);
        this.addShopProduct(shop, Material.MULE_SPAWN_EGG, 100, 50, 1, 8);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 1, 9);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 1, 10);
        this.addShopProduct(shop, Material.COOKED_PORKCHOP, 100, 50, 1, 11);
        this.addShopProduct(shop, Material.COOKED_MUTTON, 100, 50, 1, 12);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 1, 13);
        this.addShopProduct(shop, Material.BONE, 50, 20, 1, 14);
        this.addShopProduct(shop, Material.ROTTEN_FLESH, 50, 20, 1, 15);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 1, 16);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 1, 17);
        this.addShopProduct(shop, Material.COOKED_BEEF, 100, 50, 1, 18);
        this.addShopProduct(shop, Material.COOKED_BEEF, 100, 50, 1, 19);
        this.addShopProduct(shop, Material.PORKCHOP, 50, 20, 1, 20);
        this.addShopProduct(shop, Material.MUTTON, 50, 20, 1, 21);
        this.addShopProduct(shop, Material.BEEF, 50, 20, 1, 27);
        this.addShopProduct(shop, Material.BEEF, 50, 20, 1, 28);
        this.addShopProduct(shop, Material.BEE_SPAWN_EGG, 5_000, 2_500, 1, 34);
        this.addShopProduct(shop, Material.ALLAY_SPAWN_EGG, 100_000, 50_000, 1, 35);
        this.addShopProduct(shop, Material.WOLF_SPAWN_EGG, 300, 150, 1, 43);
        this.addShopProduct(shop, Material.FROG_SPAWN_EGG, 5_000, 2_500, 1, 44);
        this.addShopProduct(shop, Material.CHICKEN_SPAWN_EGG, 50, 20, 2, 0);
        this.addShopProduct(shop, Material.RABBIT_SPAWN_EGG, 50, 20, 2, 1);
        this.addShopProduct(shop, Material.TURTLE_SPAWN_EGG, 100, 50, 2, 2);
        this.addShopProduct(shop, Material.GLOW_SQUID_SPAWN_EGG, 100, 50, 2, 3);
        this.addShopProduct(shop, Material.IRON_GOLEM_SPAWN_EGG, 100, 50, 2, 4);
        this.addShopProduct(shop, Material.SNOW_GOLEM_SPAWN_EGG, 300, 150, 2, 5);
        this.addShopProduct(shop, Material.CAT_SPAWN_EGG, 50, 20, 2, 6);
        this.addShopProduct(shop, Material.OCELOT_SPAWN_EGG, 300, 150, 2, 7);
        this.addShopProduct(shop, Material.FOX_SPAWN_EGG, 300, 150, 2, 8);
        this.addShopProduct(shop, Material.COOKED_CHICKEN, 100, 50, 2, 9);
        this.addShopProduct(shop, Material.COOKED_RABBIT, 100, 50, 2, 10);
        this.addShopProduct(shop, Material.SCUTE, 10_000, 10_000, 2, 11);
        this.addShopProduct(shop, Material.INK_SAC, 50, 20, 2, 12);
        this.addShopProduct(shop, Material.POPPY, 50, 20, 2, 13);
        this.addShopProduct(shop, Material.SNOWBALL, 50, 20, 2, 14);
        this.addShopProduct(shop, Material.STRING, 50, 20, 2, 15);
        this.addShopProduct(shop, Material.CHICKEN, 50, 20, 2, 18);
        this.addShopProduct(shop, Material.RABBIT, 50, 20, 2, 19);
        this.addShopProduct(shop, Material.SEAGRASS, 50, 20, 2, 20);
        this.addShopProduct(shop, Material.GLOW_INK_SAC, 50, 20, 2, 21);
        this.addShopProduct(shop, Material.IRON_INGOT, 100, 100, 2, 22);
        this.addShopProduct(shop, Material.EGG, 50, 20, 2, 27);
        this.addShopProduct(shop, Material.RABBIT_HIDE, 50, 20, 2, 28);
        this.addShopProduct(shop, Material.BOWL, 50, 20, 2, 29);
        this.addShopProduct(shop, Material.FEATHER, 50, 20, 2, 36);
        this.addShopProduct(shop, Material.RABBIT_FOOT, 50, 20, 2, 37);
        this.addShopProduct(shop, Material.POLAR_BEAR_SPAWN_EGG, 500, 250, 3, 0);
        this.addShopProduct(shop, Material.PANDA_SPAWN_EGG, 1_000, 500, 3, 1);
        this.addShopProduct(shop, Material.PARROT_SPAWN_EGG, 100, 50, 3, 2);
        this.addShopProduct(shop, Material.AXOLOTL_SPAWN_EGG, 10_000, 5_000, 3, 3);
        this.addShopProduct(shop, Material.DOLPHIN_SPAWN_EGG, 5_000, 2_500, 3, 4);
        this.addShopProduct(shop, Material.GOAT_SPAWN_EGG, 1_000, 500, 3, 5);
        this.addShopProduct(shop, Material.WANDERING_TRADER_SPAWN_EGG, 1_000, 500, 3, 6);
        this.addShopProduct(shop, Material.TRADER_LLAMA_SPAWN_EGG, 1_000, 500, 3, 7);
        this.addShopProduct(shop, Material.LLAMA_SPAWN_EGG, 1_000, 500, 3, 8);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 3, 9);
        this.addShopProduct(shop, Material.BAMBOO, 50, 20, 3, 10);
        this.addShopProduct(shop, Material.FEATHER, 50, 20, 3, 11);
        this.addShopProduct(shop, Material.COOKED_COD, 75, 75, 3, 13);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 3, 16);
        this.addShopProduct(shop, Material.LEATHER, 50, 20, 3, 17);
        this.addShopProduct(shop, Material.COOKED_COD, 75, 75, 3, 18);
        this.addShopProduct(shop, Material.COD, 50, 50, 3, 22);
        this.addShopProduct(shop, Material.LEAD, 50, 20, 3, 25);
        this.addShopProduct(shop, Material.COD, 50, 50, 3, 27);

        shop.save();
    }

    private void createToolShop() {
        String name = LIGHT_CYAN.enclose(BOLD.enclose("Tool Store"));
        List<String> desc = List.of(GRAY.enclose("All type tools."));

        StaticShop shop = this.createStaticShop("tools", name, desc, new ItemStack(Material.DIAMOND_PICKAXE), 1, 10);

        this.addShopProduct(shop, Material.COMPASS, 50, 20, 1, 1);
        this.addShopProduct(shop, Material.SHEARS, 50, 20, 1, 3);
        this.addShopProduct(shop, Material.FISHING_ROD, 50, 20, 1, 5);
        this.addShopProduct(shop, Material.CLOCK, 50, 20, 1, 7);
        this.addShopProduct(shop, Material.WOODEN_SHOVEL, 50, 20, 1, 10);
        this.addShopProduct(shop, Material.STONE_SHOVEL, 50, 20, 1, 12);
        this.addShopProduct(shop, Material.IRON_SHOVEL, 50, 20, 1, 14);
        this.addShopProduct(shop, Material.DIAMOND_SHOVEL, 50, 20, 1, 16);
        this.addShopProduct(shop, Material.WOODEN_PICKAXE, 50, 20, 1, 19);
        this.addShopProduct(shop, Material.STONE_PICKAXE, 50, 20, 1, 21);
        this.addShopProduct(shop, Material.IRON_PICKAXE, 50, 20, 1, 23);
        this.addShopProduct(shop, Material.DIAMOND_PICKAXE, 50, 20, 1, 25);
        this.addShopProduct(shop, Material.WOODEN_HOE, 50, 20, 1, 28);
        this.addShopProduct(shop, Material.STONE_HOE, 50, 20, 1, 30);
        this.addShopProduct(shop, Material.IRON_HOE, 50, 20, 1, 32);
        this.addShopProduct(shop, Material.DIAMOND_HOE, 50, 20, 1, 34);
        this.addShopProduct(shop, Material.WOODEN_AXE, 50, 20, 1, 37);
        this.addShopProduct(shop, Material.STONE_AXE, 50, 20, 1, 39);
        this.addShopProduct(shop, Material.IRON_AXE, 50, 20, 1, 41);
        this.addShopProduct(shop, Material.DIAMOND_AXE, 50, 20, 1, 43);

        shop.save();
    }

    private void createWeaponShop() {
        String name = YELLOW.enclose(BOLD.enclose("Armory"));
        List<String> desc = List.of(GRAY.enclose("All type armors and weapons."));

        StaticShop shop = this.createStaticShop("weapons", name, desc, new ItemStack(Material.GOLDEN_SWORD), 2, 11);

        this.addShopProduct(shop, Material.SHIELD, 50, 20, 1, 4);
        this.addShopProduct(shop, Material.BOW, 50, 20, 1, 12);
        this.addShopProduct(shop, Material.ARROW, 50, 20, 1, 13);
        this.addShopProduct(shop, Material.CROSSBOW, 50, 20, 1, 14);
        this.addShopProduct(shop, Material.WOODEN_SWORD, 50, 20, 1, 20);
        this.addShopProduct(shop, Material.STONE_SWORD, 50, 20, 1, 21);
        this.addShopProduct(shop, Material.GOLDEN_SWORD, 50, 20, 1, 22);
        this.addShopProduct(shop, Material.IRON_SWORD, 50, 20, 1, 23);
        this.addShopProduct(shop, Material.DIAMOND_SWORD, 50, 20, 1, 24);
        this.addShopProduct(shop, Material.TRIDENT, 5_000, 2_000, 1, 31);
        this.addShopProduct(shop, Material.LEATHER_HELMET, 50, 20, 2, 1);
        this.addShopProduct(shop, Material.GOLDEN_HELMET, 50, 20, 2, 2);
        this.addShopProduct(shop, Material.DIAMOND_HELMET, 50, 20, 2, 4);
        this.addShopProduct(shop, Material.CHAINMAIL_HELMET, 50, 20, 2, 6);
        this.addShopProduct(shop, Material.IRON_HELMET, 50, 20, 2, 7);
        this.addShopProduct(shop, Material.LEATHER_CHESTPLATE, 50, 20, 2, 10);
        this.addShopProduct(shop, Material.GOLDEN_CHESTPLATE, 50, 20, 2, 11);
        this.addShopProduct(shop, Material.DIAMOND_CHESTPLATE, 50, 20, 2, 13);
        this.addShopProduct(shop, Material.CHAINMAIL_CHESTPLATE, 50, 20, 2, 15);
        this.addShopProduct(shop, Material.IRON_CHESTPLATE, 50, 20, 2, 16);
        this.addShopProduct(shop, Material.LEATHER_LEGGINGS, 50, 20, 2, 19);
        this.addShopProduct(shop, Material.GOLDEN_LEGGINGS, 50, 20, 2, 20);
        this.addShopProduct(shop, Material.DIAMOND_LEGGINGS, 50, 20, 2, 22);
        this.addShopProduct(shop, Material.CHAINMAIL_LEGGINGS, 50, 20, 2, 24);
        this.addShopProduct(shop, Material.IRON_LEGGINGS, 50, 20, 2, 25);
        this.addShopProduct(shop, Material.LEATHER_BOOTS, 50, 20, 2, 28);
        this.addShopProduct(shop, Material.GOLDEN_BOOTS, 50, 20, 2, 29);
        this.addShopProduct(shop, Material.DIAMOND_BOOTS, 50, 20, 2, 31);
        this.addShopProduct(shop, Material.CHAINMAIL_BOOTS, 50, 20, 2, 33);
        this.addShopProduct(shop, Material.IRON_BOOTS, 50, 20, 2, 34);

        shop.save();
    }

    private void createWoolShop() {
        String name = LIGHT_GREEN.enclose(BOLD.enclose("Wool Market"));
        List<String> desc = List.of(GRAY.enclose("Wool with colors."));

        StaticShop shop = this.createStaticShop("wool", name, desc, new ItemStack(Material.LIME_WOOL), 1, 13);

        this.addShopProduct(shop, Material.WHITE_WOOL, 50, 20, 1, 10);
        this.addShopProduct(shop, Material.ORANGE_WOOL, 50, 20, 1, 11);
        this.addShopProduct(shop, Material.MAGENTA_WOOL, 50, 20, 1, 12);
        this.addShopProduct(shop, Material.LIGHT_BLUE_WOOL, 50, 20, 1, 13);
        this.addShopProduct(shop, Material.YELLOW_WOOL, 50, 20, 1, 14);
        this.addShopProduct(shop, Material.LIME_WOOL, 50, 20, 1, 15);
        this.addShopProduct(shop, Material.PINK_WOOL, 50, 20, 1, 16);
        this.addShopProduct(shop, Material.GRAY_WOOL, 50, 20, 1, 19);
        this.addShopProduct(shop, Material.LIGHT_GRAY_WOOL, 50, 20, 1, 20);
        this.addShopProduct(shop, Material.CYAN_WOOL, 50, 20, 1, 21);
        this.addShopProduct(shop, Material.PURPLE_WOOL, 50, 20, 1, 22);
        this.addShopProduct(shop, Material.BLUE_WOOL, 50, 20, 1, 23);
        this.addShopProduct(shop, Material.BROWN_WOOL, 50, 20, 1, 24);
        this.addShopProduct(shop, Material.GREEN_WOOL, 50, 20, 1, 25);
        this.addShopProduct(shop, Material.RED_WOOL, 50, 20, 1, 30);
        this.addShopProduct(shop, Material.BLACK_WOOL, 50, 20, 1, 32);

        shop.save();
    }

    private void createTravellerShop() {
        String name = GREEN.enclose(BOLD.enclose("Traveller Shop"));
        List<String> desc = List.of(GRAY.enclose("Shop with items changing"), GRAY.enclose("every 24 hours."));

        RotatingShop shop = this.createRotatingShop("traveller", name, desc, new ItemStack(Material.ENDER_EYE));

        this.addShopProduct(shop, Material.EXPERIENCE_BOTTLE, 100, 25);
        this.addShopProduct(shop, Material.ENDER_EYE, 350, 15);
        this.addShopProduct(shop, Material.IRON_INGOT, 100, 25);
        this.addShopProduct(shop, Material.GOLD_INGOT, 100, 25);
        this.addShopProduct(shop, Material.LAPIS_LAZULI, 100, 25);
        this.addShopProduct(shop, Material.FLINT, 100, 25);
        this.addShopProduct(shop, Material.EGG, 25, 5);
        this.addShopProduct(shop, Material.TNT, 400, 35);
        this.addShopProduct(shop, Material.CARROT, 35, 5);
        this.addShopProduct(shop, Material.GOLD_NUGGET, 100, 25);
        this.addShopProduct(shop, Material.IRON_NUGGET, 100, 25);
        this.addShopProduct(shop, Material.WHEAT, 100, 25);
        this.addShopProduct(shop, Material.STICK, 100, 25);
        this.addShopProduct(shop, Material.LEATHER, 100, 25);
        this.addShopProduct(shop, Material.COPPER_INGOT, 100, 25);
        this.addShopProduct(shop, Material.GRAY_DYE, 15, 1);
        this.addShopProduct(shop, Material.BOOK, 10, 1);
        this.addShopProduct(shop, Material.GLASS_BOTTLE, 20, 2);
        this.addShopProduct(shop, Material.MELON_SLICE, 100, 25);
        this.addShopProduct(shop, Material.NAUTILUS_SHELL, 100, 25);
        this.addShopProduct(shop, Material.HEART_OF_THE_SEA, 100, 25);
        this.addShopProduct(shop, Material.FIRE_CHARGE, 100, 25);
        this.addShopProduct(shop, Material.NAME_TAG, 100, 25);
        this.addShopProduct(shop, Material.APPLE, 100, 25);
        this.addShopProduct(shop, Material.LEAD, 100, 25);
        this.addShopProduct(shop, Material.COOKED_COD, 100, 25);
        this.addShopProduct(shop, Material.SPIDER_EYE, 100, 25);

        shop.save();
        shop.rotate();
    }

    @NotNull
    private RotatingShop createRotatingShop(@NotNull String id, @NotNull String name, @NotNull List<String> description, @NotNull ItemStack icon) {
        File file = new File(this.module.getAbsolutePath() + VirtualShopModule.DIR_ROTATING_SHOPS + id, AbstractVirtualShop.FILE_NAME);
        FileUtil.create(file);

        RotatingShop shop = new RotatingShop(this.plugin, this.module, file, id);
        shop.setDefaultLayout(Placeholders.DEFAULT);
        shop.setRotationType(RotationType.INTERVAL);
        shop.setRotationInterval(86400);
        shop.setProductMinAmount(15);
        shop.setProductMaxAmount(15);
        shop.setProductSlots(new int[]{10,11,12,13,14,15,16,20,21,22,23,24,30,31,32});
        this.setShopSettings(shop, name, description, icon);

        return shop;
    }

    @NotNull
    private StaticShop createStaticShop(@NotNull String id, @NotNull String name, @NotNull List<String> description, @NotNull ItemStack icon, int pages, int menuSlot) {
        File file = new File(this.module.getAbsolutePath() + VirtualShopModule.DIR_SHOPS + id, AbstractVirtualShop.FILE_NAME);
        FileUtil.create(file);

        StaticShop shop = new StaticShop(this.plugin, this.module, file, id);
        shop.setPages(pages);
        shop.setMainMenuSlot(menuSlot);
        shop.setDefaultLayout(Placeholders.DEFAULT);
        this.setShopSettings(shop, name, description, icon);

        return shop;
    }

    private void setShopSettings(@NotNull AbstractVirtualShop<?> shop, @NotNull String name, @NotNull List<String> description, @NotNull ItemStack icon) {
        shop.setName(name);
        shop.setDescription(new ArrayList<>(description));
        shop.setIcon(icon);
        shop.setPermissionRequired(false);
        for (TradeType tradeType : TradeType.values()) {
            shop.setTransactionEnabled(tradeType, true);
        }
        shop.saveSettings();
    }

    private void addShopProduct(@NotNull StaticShop shop, @NotNull Material material, double buyPrice, double sellPrice, int page, int slot) {
        this.addShopProduct(shop, material, UniDouble.of(buyPrice, sellPrice), UniInt.of(page, slot));
    }

    private void addShopProduct(@NotNull RotatingShop shop, @NotNull Material material, double buyPrice, double sellPrice) {
        this.addShopProduct(shop, material, UniDouble.of(buyPrice, sellPrice));
    }

    private void addShopProduct(@NotNull StaticShop shop, @NotNull Material material, @NotNull UniDouble price, @NotNull UniInt pageSlot) {
        ItemStack itemStack = new ItemStack(material);

        BukkitItemHandler handler = ProductHandlerRegistry.forBukkitItem();
        BukkitItemPacker packer = handler.createPacker(itemStack);
        if (packer == null) return;

        StaticProduct product = shop.createProduct(this.currency, handler, packer);
        product.setPricer(AbstractProductPricer.from(PriceType.FLAT));
        product.getPricer().setPrice(TradeType.BUY, price.getMinValue());
        product.getPricer().setPrice(TradeType.SELL, price.getMaxValue());
        product.setPage(pageSlot.getMinValue());
        product.setSlot(pageSlot.getMaxValue());

        shop.addProduct(product);
    }

    private void addShopProduct(@NotNull RotatingShop shop, @NotNull Material material, @NotNull UniDouble price) {
        ItemStack itemStack = new ItemStack(material);

        BukkitItemHandler handler = ProductHandlerRegistry.forBukkitItem();
        BukkitItemPacker packer = handler.createPacker(itemStack);
        if (packer == null) return;

        RotatingProduct product = shop.createProduct(this.currency, handler, packer);
        product.setPricer(AbstractProductPricer.from(PriceType.FLAT));
        product.getPricer().setPrice(TradeType.BUY, price.getMinValue());
        product.getPricer().setPrice(TradeType.SELL, price.getMaxValue());
        product.setRotationChance(50D);

        shop.addProduct(product);
    }
}
