package fryantit.militaryinsurgency;

import fryantit.militaryinsurgency.armor.MilitaryArmorItems;
import fryantit.militaryinsurgency.item.MilitaryItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MilitaryInsurgencyMod implements ModInitializer {
    public static final String MOD_ID = "militaryinsurgency";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ItemGroup MILITARY_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MOD_ID, "military_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(MilitaryArmorItems.TITANIUM_HELMET))
                    .displayName(Text.translatable("itemGroup.militaryinsurgency.military_group"))
                    .entries((context, entries) -> {
                        entries.add(MilitaryItems.COAL_COKE);
                        entries.add(MilitaryItems.TITANIUM_INGOT);
                        entries.add(MilitaryItems.CARBON_FIBER_SHEET);
                        entries.add(MilitaryArmorItems.TITANIUM_HELMET);
                        entries.add(MilitaryArmorItems.TITANIUM_CHESTPLATE);
                        entries.add(MilitaryArmorItems.TITANIUM_LEGGINGS);
                        entries.add(MilitaryArmorItems.TITANIUM_BOOTS);
                        // Add more items here if needed
                    })
                    .build());

    @Override
    public void onInitialize() {
        LOGGER.info("Military Insurgency Gear: Tactical Systems Online!");
        MilitaryItems.registerModItems();
        MilitaryArmorItems.registerModArmor();
    }
}
