package su.nightexpress.nexshop.shop.menu;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nexshop.ShopPlugin;
import su.nightexpress.nexshop.api.shop.product.typing.PhysicalTyping;
import su.nightexpress.nexshop.api.shop.type.TradeType;
import su.nightexpress.nexshop.api.type.RefreshType;
import su.nightexpress.nexshop.config.Lang;
import su.nightexpress.nexshop.product.price.AbstractProductPricer;
import su.nightexpress.nexshop.product.price.impl.*;
import su.nightexpress.nexshop.shop.impl.AbstractProduct;
import su.nightexpress.nexshop.util.ShopUtils;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.LinkHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.wrapper.UniDouble;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public abstract class ProductPriceMenu<T extends AbstractProduct<?>> extends LinkedMenu<ShopPlugin, T> {

    private static final String SKULL_BUY     = "33658f9ed2145ef8323ec8dc2688197c58964510f3d29939238ce1b6e45af0ff";
    private static final String SKULL_SELL    = "574e65e2f1625b0b2102d6bf3df8264ac43d9d679437a3a42e262d24c4fc";
    private static final String SKULL_CLOCK   = "cbbc06a8d6b1492e40f0e7c3b632b6fd8e66dc45c15234990caa5410ac3ac3fd";
    private static final String SKULL_DAYS    = "ffe96fc80becc0df08be5fbbd3696bfadf75a35e98ac623a475ef6e141d8fb6b";
    private static final String SKULL_INITIAL = "271c2b4782646b380408ab4489c98531b30ed42a3a9f6d33269efc5edf5dc0e8";
    private static final String SKULL_STEP    = "7b14317e3aa4bea88027d5457a6b13e1bea7b54f5ae1486491063ec0186ed662";
    private static final String SKULL_PLAYER  = "97e6e5657b8f85f3af2c835b3533856607682f8571a4548967e2bdb535ac56b7";
    private static final String SKULL_RESET   = "802246ff8b6c617168edaec39660612e72a54ab2eacc27c5e815e4ac70239e3a";

    public ProductPriceMenu(@NotNull ShopPlugin plugin, @NotNull String title) {
        super(plugin, MenuType.GENERIC_9X6, title);

        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.handleReturn(viewer, event, this.getLink(viewer));
        }));

        this.addItem(Material.NAME_TAG, Lang.PRODUCT_EDIT_PRICE_TYPE, 10, this::handlePriceType);

        this.addItem(Material.GOLD_NUGGET, Lang.PRODUCT_EDIT_PRICE_CURRENCY, 19, this::handleCurrency, ItemOptions.builder().setDisplayModifier((viewer, item) -> {
            item.inherit(NightItem.fromItemStack(this.getLink(viewer).getCurrency().getIcon()))
                .localized(Lang.PRODUCT_EDIT_PRICE_CURRENCY)
                .setHideComponents(true);
        }).build());

        this.addItem(NightItem.asCustomHead(SKULL_RESET), Lang.PRODUCT_PRICE_RESET, 28, (viewer, event, product) -> {
            this.runNextTick(() -> plugin.getShopManager().openConfirmation(viewer.getPlayer(), Confirmation.create(
                (viewer1, event1) -> {
                    plugin.getDataManager().resetPriceData(product);
                    product.updatePrice(true);
                    this.open(viewer1.getPlayer(), product);
                },
                (viewer1, event1) -> {
                    this.open(viewer1.getPlayer(), product);
                }
            )));
        }, ItemOptions.builder().setVisibilityPolicy(this::canResetPriceData).build());
    }

    protected void saveAndFlush(@NotNull MenuViewer viewer, @NotNull T product) {
        this.save(viewer, product);
        this.runNextTick(() -> this.flush(viewer));
    }

    protected abstract void save(@NotNull MenuViewer viewer, @NotNull T product);

    protected abstract boolean canResetPriceData(@NotNull MenuViewer viewer);

    protected abstract void handleReturn(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull T product);

    protected abstract void handleCurrency(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull T product);

    protected abstract void handlePriceType(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event, @NotNull T product);

    private void handleFlatPrice(@NotNull MenuViewer viewer,
                                 @NotNull InventoryClickEvent event,
                                 @NotNull T product,
                                 @NotNull TradeType tradeType) {
        if (event.getClick() == ClickType.DROP) {
            product.setPrice(tradeType, -1D);
            this.saveAndFlush(viewer, product);
            return;
        }

        this.handleInput(Dialog.builder(viewer, Lang.EDITOR_PRODUCT_ENTER_PRICE, input -> {
            product.setPrice(tradeType, input.asDouble(0));
            this.save(viewer, product);
            return true;
        }));
    }

    private void handleRangedPrice(@NotNull MenuViewer viewer,
                                   @NotNull InventoryClickEvent event,
                                   @NotNull T product,
                                   @NotNull RangedPricer pricer,
                                   @NotNull TradeType tradeType) {
        if (event.getClick() == ClickType.DROP) {
            pricer.setPriceRange(tradeType, UniDouble.of(-1D, -1D));
            product.setPrice(tradeType, -1D);
            this.saveAndFlush(viewer, product);
            return;
        }

        this.handleInput(Dialog.builder(viewer, Lang.EDITOR_PRODUCT_ENTER_UNI_PRICE, input -> {
            String[] split = input.getTextRaw().split(" ");
            double min = NumberUtil.getDoubleAbs(split[0]);
            double max = split.length >= 2 ? NumberUtil.getDoubleAbs(split[1]) : min;

            pricer.setPriceRange(tradeType, UniDouble.of(min, max));
            this.save(viewer, product);
            return true;
        }));
    }

    @Override
    protected void onItemPrepare(@NotNull MenuViewer viewer, @NotNull MenuItem menuItem, @NotNull NightItem item) {
        super.onItemPrepare(viewer, menuItem, item);

        item.replacement(replacer -> replacer.replace(this.getLink(viewer).replacePlaceholders()));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        T product = this.getLink(viewer);
        AbstractProductPricer pricer = product.getPricer();
        switch (pricer) {
            case FlatPricer ignored -> this.addFlatButtons(viewer, product);
            case FloatPricer floatPricer -> {
                this.addRangedButtons(viewer, product, floatPricer);
                this.addFloatButtons(viewer, floatPricer);
            }
            case DynamicPricer dynamicPricer -> {
                this.addRangedButtons(viewer, product, dynamicPricer);
                this.addDynamicButtons(viewer, dynamicPricer);
            }
            case PlayersPricer playersPricer -> {
                this.addRangedButtons(viewer, product, playersPricer);
                this.addPlayersButtons(viewer, playersPricer);
            }
            default -> {}
        }
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    private void addItem(@NotNull MenuViewer viewer,
                         @NotNull NightItem item,
                         @NotNull LangUIButton locale,
                         int slot,
                         @NotNull LinkHandler<T> handler) {
        this.addItem(viewer, item, locale, slot, handler, null);
    }

    private void addItem(@NotNull MenuViewer viewer,
                         @NotNull NightItem item,
                         @NotNull LangUIButton locale,
                         int slot,
                         @NotNull LinkHandler<T> handler,
                         @Nullable ItemOptions options) {
        viewer.addItem(item.localized(locale).toMenuItem().setSlots(slot).setHandler(ItemHandler.forLink(this, handler, options)).build());
    }

    private void addFlatButtons(@NotNull MenuViewer menuViewer, @NotNull T shopItem) {
        this.addItem(menuViewer, NightItem.asCustomHead(SKULL_BUY), Lang.PRODUCT_EDIT_PRICE_FLAT_BUY, 13, (viewer, event, product) -> {
            this.handleFlatPrice(viewer, event, product, TradeType.BUY);
        });

        if (shopItem.getType() instanceof PhysicalTyping) {
            this.addItem(menuViewer, NightItem.asCustomHead(SKULL_SELL), Lang.PRODUCT_EDIT_PRICE_FLAT_SELL, 15, (viewer, event, product) -> {
                this.handleFlatPrice(viewer, event, product, TradeType.SELL);
            });
        }
    }

    private void addRangedButtons(@NotNull MenuViewer menuViewer, @NotNull T shopItem, @NotNull RangedPricer pricer) {
        this.addItem(menuViewer, NightItem.asCustomHead(SKULL_BUY), Lang.PRODUCT_EDIT_PRICE_BOUNDS_BUY, 13, (viewer, event, product) -> {
            this.handleRangedPrice(viewer, event, product, pricer, TradeType.BUY);
        });

        if (shopItem.getType() instanceof PhysicalTyping) {
            this.addItem(menuViewer, NightItem.asCustomHead(SKULL_SELL), Lang.PRODUCT_EDIT_PRICE_BOUNDS_SELL, 15, (viewer, event, product) -> {
                this.handleRangedPrice(viewer, event, product, pricer, TradeType.SELL);
            });
        }
    }

    private void addFloatButtons(@NotNull MenuViewer menuViewer, @NotNull FloatPricer pricer) {
        this.addItem(menuViewer, NightItem.fromType(Material.SHEARS), Lang.PRODUCT_EDIT_PRICE_FLOAT_DECIMALS, 40, (viewer, event, product) -> {
            pricer.setRoundDecimals(!pricer.isRoundDecimals());
            this.saveAndFlush(viewer, product);
        });

        this.addItem(menuViewer, NightItem.fromType(Material.OAK_HANGING_SIGN), Lang.PRODUCT_EDIT_PRICE_FLOAT_REFRESH_TYPE, 31, (viewer, event, product) -> {
            pricer.setRefreshType(Lists.next(pricer.getRefreshType()));
            this.saveAndFlush(viewer, product);
        });

        if (pricer.getRefreshType() == RefreshType.INTERVAL) {
            this.addItem(menuViewer, NightItem.asCustomHead(SKULL_CLOCK), Lang.PRODUCT_EDIT_PRICE_FLOAT_REFRESH_INTERVAL, 33, (viewer, event, product) -> {
                this.handleInput(Dialog.builder(viewer, Lang.EDITOR_GENERIC_ENTER_SECONDS, input -> {
                    pricer.setRefreshInterval(input.asIntAbs(0));
                    this.save(viewer, product);
                    return true;
                }));
            });
        }

        if (pricer.getRefreshType() == RefreshType.FIXED) {
            this.addItem(menuViewer, NightItem.asCustomHead(SKULL_DAYS), Lang.PRODUCT_EDIT_PRICE_FLOAT_REFRESH_DAYS, 33, (viewer, event, product) -> {
                if (event.isRightClick()) {
                    pricer.getDays().clear();
                    this.saveAndFlush(viewer, product);
                    return;
                }

                this.handleInput(Dialog.builder(viewer, Lang.EDITOR_GENERIC_ENTER_DAY, input -> {
                    DayOfWeek day = StringUtil.getEnum(input.getTextRaw(), DayOfWeek.class).orElse(null);
                    if (day == null) return true;

                    pricer.getDays().add(day);
                    this.save(viewer, product);
                    return true;
                }).setSuggestions(Lists.getEnums(DayOfWeek.class), true));
            });

            this.addItem(menuViewer, NightItem.asCustomHead(SKULL_CLOCK), Lang.PRODUCT_EDIT_PRICE_FLOAT_REFRESH_TIMES, 42, (viewer, event, product) -> {
                if (event.isRightClick()) {
                    pricer.getTimes().clear();
                    this.saveAndFlush(viewer, product);
                    return;
                }

                this.handleInput(Dialog.builder(viewer, Lang.EDITOR_GENERIC_ENTER_TIME, input -> {
                    try {
                        pricer.getTimes().add(LocalTime.parse(input.getTextRaw(), ShopUtils.TIME_FORMATTER));
                        this.save(viewer, product);
                    }
                    catch (DateTimeParseException ignored) {
                    }
                    return true;
                }));
            });
        }
    }



    private void addDynamicButtons(@NotNull MenuViewer menuViewer, @NotNull DynamicPricer pricer) {
        this.addItem(menuViewer, NightItem.asCustomHead(SKULL_INITIAL), Lang.PRODUCT_EDIT_PRICE_DYNAMIC_INITIAL, 31, (viewer, event, product) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_PRODUCT_ENTER_PRICE, input -> {
                TradeType tradeType = event.isLeftClick() ? TradeType.BUY : TradeType.SELL;

                pricer.setInitial(tradeType, input.asDouble(0));
                this.save(viewer, product);
                return true;
            }));
        });

        this.addItem(menuViewer, NightItem.asCustomHead(SKULL_STEP), Lang.PRODUCT_EDIT_PRICE_DYNAMIC_STEP, 33, (viewer, event, product) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_PRODUCT_ENTER_PRICE, input -> {
                TradeType tradeType = event.isLeftClick() ? TradeType.BUY : TradeType.SELL;

                pricer.setStep(tradeType, input.asDouble(0));
                this.save(viewer, product);
                return true;
            }));
        });
    }

    private void addPlayersButtons(@NotNull MenuViewer menuViewer, @NotNull PlayersPricer pricer) {
        this.addItem(menuViewer, NightItem.asCustomHead(SKULL_INITIAL), Lang.PRODUCT_EDIT_PRICE_PLAYERS_INITIAL, 31, (viewer, event, product) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_PRODUCT_ENTER_PRICE, input -> {
                TradeType tradeType = event.isLeftClick() ? TradeType.BUY : TradeType.SELL;

                pricer.setInitial(tradeType, input.asDouble(0));
                this.save(viewer, product);
                return true;
            }));
        });

        this.addItem(menuViewer, NightItem.asCustomHead(SKULL_STEP), Lang.PRODUCT_EDIT_PRICE_PLAYERS_ADJUST, 32, (viewer, event, product) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_PRODUCT_ENTER_PRICE, input -> {
                TradeType tradeType = event.isLeftClick() ? TradeType.BUY : TradeType.SELL;

                pricer.setAdjustAmount(tradeType, input.asDouble(0));
                this.save(viewer, product);
                return true;
            }));
        });

        this.addItem(menuViewer, NightItem.asCustomHead(SKULL_PLAYER), Lang.PRODUCT_EDIT_PRICE_PLAYERS_STEP, 33, (viewer, event, product) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_GENERIC_ENTER_AMOUNT, input -> {
                pricer.setAdjustStep(input.asInt(0));
                this.save(viewer, product);
                return true;
            }));
        });
    }
}
