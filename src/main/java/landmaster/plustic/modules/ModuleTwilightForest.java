package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import java.util.concurrent.*;

import landmaster.plustic.*;
import landmaster.plustic.api.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.mantle.util.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.smeltery.*;
import slimeknights.tconstruct.shared.*;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class ModuleTwilightForest implements IModule {
	private static final CompletableFuture<?> itemPromise = new CompletableFuture<>();
	
	@Override
	public void init() {
		if (Config.twilightForest && Loader.isModLoaded("twilightforest")) {
			Material fiery = new Material("fiery", 0x681302);
			fiery.addTrait(superheat);
			fiery.addTrait(MysticalFire.mystical_fire, HEAD);
			
			fiery.addItem("ingotFiery", 1, Material.VALUE_Ingot);
			fiery.setCraftable(false).setCastable(true);
			new OreRegisterPromise("ingotFiery").thenAccept(fiery::setRepresentativeItem);
			PlusTiC.proxy.setRenderInfo(fiery, 0x681302);
			
			FluidMolten fieryBloodFluid = Utils.fluidMetal("fiery_blood", 0x210601);
			fieryBloodFluid.setTemperature(2000);
			Utils.initFluidMetal(fieryBloodFluid);
			itemPromise.thenRun(() -> {
				final Item fieryBlood = Item.REGISTRY.getObject(new ResourceLocation("twilightforest", "fiery_blood"));
				TinkerRegistry.registerMelting(new MeltingRecipe(RecipeMatch.of(fieryBlood, 1, Material.VALUE_Ingot), fieryBloodFluid, 350));
				TinkerRegistry.registerTableCasting(new ItemStack(fieryBlood), ItemStack.EMPTY, fieryBloodFluid, Material.VALUE_Ingot);
			});
			
			FluidMolten fieryFluid = Utils.fluidMetal("fiery", 0x681302);
			fieryFluid.setTemperature(2000);
			Utils.initFluidMetal(fieryFluid);
			fiery.setFluid(fieryFluid);
			TinkerRegistry.registerAlloy(new FluidStack(fieryFluid, 1),
					new FluidStack(TinkerFluids.iron, 1),
					new FluidStack(fieryBloodFluid, 1));
			
			TinkerRegistry.addMaterialStats(fiery,
					new HeadMaterialStats(1024, 9, 6, COBALT),
					new HandleMaterialStats(1.0f, 200),
					new ExtraMaterialStats(200),
					new BowMaterialStats(0.8f, 1.2f, 5.5f));
			
			PlusTiC.materials.put("fiery", fiery);
			
			// TODO add the remaining Twilight Forest materials
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void itemRegEvent(RegistryEvent.Register<Item> event) {
		itemPromise.complete(null);
	}
}
