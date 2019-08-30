package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.PlusTiC;
import landmaster.plustic.config.Config;
import landmaster.plustic.fluids.FluidMolten;
import landmaster.plustic.util.Utils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

public class ModuleAoA implements IModule {

    public void init() {
        if((Config.aoa3 && Loader.isModLoaded("aoa3"))) {
            Material limonite = new Material("limonite", TextFormatting.YELLOW);
            limonite.addTrait(magnetic2, HEAD);
            limonite.addTrait(cheap);
            limonite.addItem("ingotLimonite", 1, Material.VALUE_Ingot);
            limonite.setCraftable(false).setCastable(true);
            Utils.setDispItem(limonite, "aoa3", "ingotLimonite");
            PlusTiC.proxy.setRenderInfo(limonite, 0xDE9300);
            FluidMolten limoniteFluid = Utils.fluidMetal("limonite", 0xDE9300);
            limoniteFluid.setTemperature(790);
            Utils.initFluidMetal(limoniteFluid);
            limonite.setFluid(limoniteFluid);
            TinkerRegistry.addMaterialStats(limonite, new HeadMaterialStats(640, 6.5f, 4.5f, OBSIDIAN));
            TinkerRegistry.addMaterialStats(limonite, new HandleMaterialStats(1f, 70));
            TinkerRegistry.addMaterialStats(limonite, new ExtraMaterialStats(-20));
            TinkerRegistry.addMaterialStats(limonite, new BowMaterialStats(1.15f, 1.3f, 4f));
            PlusTiC.materials.put("limonite", limonite);

            Material emberstone = new Material("emberstone", TextFormatting.DARK_RED);
            emberstone.addTrait(hellish, HEAD);
            emberstone.addTrait(autosmelt);
            emberstone.addItem("ingotEmberstone", 1, Material.VALUE_Ingot);
            emberstone.setCraftable(false).setCastable(true);
            Utils.setDispItem(emberstone, "aoa3", "ingotEmberstone");
            PlusTiC.proxy.setRenderInfo(emberstone, 0x450009 );
            FluidMolten emberstoneFluid = Utils.fluidMetal("emberstone",0x450009);
            emberstoneFluid.setTemperature(790);
            Utils.initFluidMetal(emberstoneFluid);
            emberstone.setFluid(emberstoneFluid);
            TinkerRegistry.addMaterialStats(emberstone, new HeadMaterialStats(1100, 9f, 9.5f, COBALT));
            TinkerRegistry.addMaterialStats(emberstone, new HandleMaterialStats(1f, 0));
            TinkerRegistry.addMaterialStats(emberstone, new ExtraMaterialStats(120));
            PlusTiC.materials.put("emberstone", emberstone);

            Material skeletal = new Material("skeletal", TextFormatting.GRAY);
            skeletal.addTrait(sharp, HEAD);
            skeletal.addTrait(fractured);
            skeletal.addItem("ingotSkeletal", 1, Material.VALUE_Ingot);
            skeletal.setCraftable(false).setCastable(true);
            Utils.setDispItem(skeletal, "aoa3", "ingotSkeletal");
            PlusTiC.proxy.setRenderInfo(emberstone, 0xE8E3C5 );
            FluidMolten skeletalFluid = Utils.fluidMetal("skeletal",0xE8E3C5);
            skeletalFluid.setTemperature(790);
            Utils.initFluidMetal(skeletalFluid);
            skeletal.setFluid(skeletalFluid);
            TinkerRegistry.addMaterialStats(skeletal, new HeadMaterialStats(1500, 12, 12f, COBALT));
            TinkerRegistry.addMaterialStats(skeletal, new HandleMaterialStats(1f, 200));
            TinkerRegistry.addMaterialStats(skeletal, new ExtraMaterialStats(200));
            PlusTiC.materials.put("skeletal", skeletal);

            Material bloodstone = new Material("bloodstone", TextFormatting.RED);
            bloodstone.addTrait(coldblooded, HEAD);
            bloodstone.addTrait(hellish);
            bloodstone.addItem("gemBloodstone", 1, Material.VALUE_Ingot);
            bloodstone.setCraftable(true);
            Utils.setDispItem(bloodstone, "aoa3", "gemBloodstone");
            PlusTiC.proxy.setRenderInfo(bloodstone, 0xC7001E );
            TinkerRegistry.addMaterialStats(bloodstone, new HeadMaterialStats(666, 2f, 11.1f, DIAMOND));
            TinkerRegistry.addMaterialStats(bloodstone, new HandleMaterialStats(1f, -666));
            TinkerRegistry.addMaterialStats(bloodstone, new ExtraMaterialStats(666));
            PlusTiC.materials.put("bloodstone", bloodstone);

            Material crystallite = new Material("crystallite", TextFormatting.RED);
            crystallite.addTrait(dense, HANDLE);
            crystallite.addTrait(jagged);
            crystallite.addItem("gemCrystallite", 1, Material.VALUE_Ingot);
            crystallite.setCraftable(true);
            Utils.setDispItem(crystallite, "aoa3", "gemCrystallite");
            PlusTiC.proxy.setRenderInfo(crystallite, 0xFFC14D );
            TinkerRegistry.addMaterialStats(crystallite, new HeadMaterialStats(1050, 2f, 10f, DIAMOND));
            TinkerRegistry.addMaterialStats(crystallite, new HandleMaterialStats(2f, -100));
            TinkerRegistry.addMaterialStats(crystallite, new ExtraMaterialStats(100));
            PlusTiC.materials.put("crystallite", crystallite);
        }
    }
}
