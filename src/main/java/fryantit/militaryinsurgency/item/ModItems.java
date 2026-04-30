package fryantit.militaryinsurgency.item;

import fryantit.militaryinsurgency.MilitaryInsurgencyMod;
import fryantit.militaryinsurgency.item.custom.NVGItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item NVG_ITEM = registerItem("nvg_item", new NVGItem(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MilitaryInsurgencyMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        MilitaryInsurgencyMod.LOGGER.info("Registering Mod Items for " + MilitaryInsurgencyMod.MOD_ID);
    }
}
