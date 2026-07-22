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

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {
    @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
    private void blockSectionHeaderSlotClicks(Slot slot, int i, int j, ClickType clickType, CallbackInfo ci) {
        if (slot != null && CreativeTabSectionHeaderItem.isSectionMarkerStack(slot.getItem())) {
            ci.cancel();
        }
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void injectSectionHeaders$selectTab(CreativeModeTab creativeModeTab, CallbackInfo ci) {
        if (creativeModeTab == null) return;
        ResourceLocation selectedTabId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(creativeModeTab);
        if (selectedTabId == null) return;
        if (CreativeModeTabSectionsManager.hasTabSections(selectedTabId)) {
            NonNullList<ItemStack> structuredItems = NonNullList.create();
            Map<ResourceLocation, List<ItemStack>> sections = CreativeModeTabSectionsManager.getTabSections(selectedTabId).sections();
            if (sections == null) return;
            sections.entrySet().stream()
                    .sorted(Comparator.comparingInt((Map.Entry<ResourceLocation, List<ItemStack>> e) ->
                                    CreativeModeTabSectionsManager.getTabSections(selectedTabId)
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
