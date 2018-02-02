package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import java.util.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.tools.stats.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

public class ModuleEnderIO implements IModule {

	public void init() {
		if (Config.enderIO && Loader.isModLoaded("enderio")) {
			Fluid coalFluid = Utils.fluidMetal("coal", 0x111111);
			coalFluid.setTemperature(500);
			Utils.initFluidMetal(coalFluid);
			
			TinkerRegistry.registerMelting("coal", coalFluid, 100);
			TinkerRegistry.registerBasinCasting(new ItemStack(Blocks.COAL_BLOCK), ItemStack.EMPTY, coalFluid, 900);
			TinkerRegistry.registerTableCasting(new ItemStack(Items.COAL), ItemStack.EMPTY, coalFluid, 100);
			
			Material darkSteel = new Material("darksteel_plustic_enderio", TextFormatting.DARK_GRAY);
			darkSteel.addTrait(Portly.portly, HEAD);
			darkSteel.addTrait(coldblooded);
			darkSteel.addItem("ingotDarkSteel", 1, Material.VALUE_Ingot);
			darkSteel.setCraftable(false).setCastable(true);
			Utils.setDispItem(darkSteel, "ingotDarkSteel");
			PlusTiC.proxy.setRenderInfo(darkSteel, 0x333333);
			
			Fluid darkSteelFluid = Arrays.asList("darksteel", "dark_steel").stream()
					.map(FluidRegistry::getFluid)
					.filter(fluid -> fluid != null)
					.findAny().orElse(null);
			darkSteel.setFluid(darkSteelFluid);
			
			if (Config.alloyDarkSteel) {
				TinkerRegistry.registerAlloy(
						new FluidStack(darkSteelFluid, 36),
						new FluidStack(TinkerFluids.obsidian, 72),
						new FluidStack(TinkerFluids.iron, 36),
						new FluidStack(coalFluid, 25));
			}
			
			TinkerRegistry.addMaterialStats(darkSteel, new HeadMaterialStats(666, 7, 4, OBSIDIAN),
					new HandleMaterialStats(1.05f, 40), new ExtraMaterialStats(40),
					new BowMaterialStats(0.38f, 2.05f, 10), new BatteryCellMaterialStats(75000));
			PlusTiC.materials.put("darkSteel", darkSteel);
		}
	}
	
}
