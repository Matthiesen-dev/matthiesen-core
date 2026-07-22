package dev.matthiesen.matthiesen_core.common.core.mixins;

import dev.matthiesen.matthiesen_core.common.core.item.CreativeTabSectionHeaderItem;
import dev.matthiesen.matthiesen_core.common.core.registry.CreativeModeTabSectionsManager;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This mixin class modifies the behavior of the CreativeModeInventoryScreen class in Minecraft to enhance the functionality of the creative inventory screen.
 * It provides two main features:
 * <p>
 * 1. Blocking clicks on section header slots: The mixin intercepts the slotClicked method to prevent interaction with section header slots,
 * ensuring that they cannot be moved or interacted with like regular items.
 * </p>
 * <p>
 * 2. Structuring items in creative tabs with sections: The mixin injects into the selectTab method to organize items in creative tabs according to
 * defined sections, including adding section headers and placeholders. This allows for a more organized and visually appealing presentation of items in the creative inventory.
 * </p>
 */
@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    /**
     * Injects into the slotClicked method of the CreativeModeInventoryScreen to block clicks on section header slots.
     * If the clicked slot contains a section header item, the click event is canceled, preventing any interaction with the section header.
     * This ensures that section headers are not treated as regular items and cannot be moved or interacted with in the creative inventory.
     * @param slot The slot that was clicked.
     * @param i The index of the clicked slot.
     * @param j The mouse button used for the click.
     * @param clickType The type of click (e.g., left-click, right-click).
     * @param ci The CallbackInfo object that allows for cancellation of the original method execution.
     */
    @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
    private void blockSectionHeaderSlotClicks(Slot slot, int i, int j, ClickType clickType, CallbackInfo ci) {
        if (slot != null && CreativeTabSectionHeaderItem.isSectionMarkerStack(slot.getItem())) {
            ci.cancel();
        }
    }

    /**
     * Injects into the selectTab method of the CreativeModeInventoryScreen to modify the behavior of tab selection.
     * This injection checks if the selected tab has defined sections and, if so, structures the items in the tab according to
     * the defined sections, including adding section headers and placeholders.
     * The structured items are then set in the ItemPickerMenu, and the scroll position is reset to the top.
     * @param creativeModeTab The CreativeModeTab that is being selected.
     * @param ci The CallbackInfo object that allows for cancellation of the original method execution.
     */
    @Inject(method = "selectTab", at = @At("TAIL"))
    private void injectSectionHeaders$selectTab(CreativeModeTab creativeModeTab, CallbackInfo ci) {
        if (creativeModeTab == null) return;
        ResourceLocation selectedTabId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(creativeModeTab);
        if (selectedTabId == null) return;
        if (CreativeModeTabSectionsManager.INSTANCE.hasTabSections(selectedTabId)) {
            NonNullList<ItemStack> structuredItems = NonNullList.create();
            Map<ResourceLocation, List<ItemStack>> sections = CreativeModeTabSectionsManager.INSTANCE.getTabSections(selectedTabId).sections();
            if (sections == null) return;
            sections.entrySet().stream()
                    .sorted(Comparator.comparingInt((Map.Entry<ResourceLocation, List<ItemStack>> e) ->
                                    CreativeModeTabSectionsManager.INSTANCE.getTabSections(selectedTabId)
                                            .metadata()
                                            .get(e.getKey())
                                            .priority())
                            .reversed()
                    )
                    .forEach(entry -> {
                        ResourceLocation sectionId = entry.getKey();
                        ItemStack headerStack = CreativeTabSectionHeaderItem.createHeaderStack(selectedTabId, sectionId);
                        structuredItems.add(headerStack);
                        for (int i = 0; i < 8; i++) {
                            structuredItems.add(CreativeTabSectionHeaderItem.createPlaceholderStack());
                        }
                        structuredItems.addAll(entry.getValue());
                        while (structuredItems.size() % 9 != 0) {
                            structuredItems.add(ItemStack.EMPTY);
                        }
                    });
            if (((CreativeModeInventoryScreen) (Object) this).getMenu() instanceof ItemPickerMenuAccessor menuAccessor) {
                var menuItems = menuAccessor.getItemsList();
                menuItems.clear();
                menuItems.addAll(structuredItems);
                menuAccessor.invokeScrollTo(0.0f);
            }
        }
    }
}
