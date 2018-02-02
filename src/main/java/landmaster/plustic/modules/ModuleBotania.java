package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import java.util.*;
import java.util.concurrent.*;

import com.google.common.collect.*;

import gnu.trove.map.hash.*;
import landmaster.plustic.*;
import landmaster.plustic.api.ModInfo;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class ModuleBotania implements IModule {
	private static final CompletableFuture<?> itemPromise = new CompletableFuture<>();

	public void init() {
		if (Config.botania && Loader.isModLoaded("botania")) {
			if (Config.forceOutNaturalPledgeMaterials || !Loader.isModLoaded("botanicaladdons")) {
				final Map<String, Material> botaniaMaterials = new THashMap<>();
				
				Material terrasteel = new Material("terrasteel", TextFormatting.GREEN);
				botaniaMaterials.put("terrasteel", terrasteel);
				FluidMolten terrasteelFluid = Utils.fluidMetal("terrasteel", 0x00FF00);
				terrasteelFluid.setTemperature(760);
				Utils.initFluidMetal(terrasteelFluid);
				
				Material elementium = new Material("elementium", TextFormatting.LIGHT_PURPLE);
				botaniaMaterials.put("elvenElementium", elementium);
				FluidMolten elementiumFluid = Utils.fluidMetal("elementium", 0xF66AFD);
				elementiumFluid.setTemperature(800);
				Utils.initFluidMetal(elementiumFluid);
				
				Material manasteel = new Material("manasteel", TextFormatting.BLUE);
				botaniaMaterials.put("manasteel", manasteel);
				FluidMolten manasteelFluid = Utils.fluidMetal("manasteel", 0x54E5FF);
				manasteelFluid.setTemperature(681);
				Utils.initFluidMetal(manasteelFluid);
				
				Material livingwood = new Material("livingwood_plustic", TextFormatting.DARK_GREEN);
				botaniaMaterials.put("livingwood", livingwood);
				
				Material mirion = new Material("mirion", TextFormatting.YELLOW);
				botaniaMaterials.put("mirion", mirion);
				FluidMolten mirionFluid = Utils.fluidMetal("mirion", 0xDDFF00);
				mirionFluid.setTemperature(777);
				Utils.initFluidMetal(mirionFluid);
				
				final CompletableFuture<?> integrationPromise = itemPromise.thenRun(() -> {
					terrasteel.addTrait(Mana.mana);
					terrasteel.addTrait(Terrafirma.terrafirma.get(0));
					terrasteel.addTrait(Mana.mana, HEAD);
					terrasteel.addTrait(Terrafirma.terrafirma.get(1), HEAD);
					terrasteel.addItem("ingotTerrasteel", 1, Material.VALUE_Ingot);
					terrasteel.setCraftable(false).setCastable(true);
					Utils.setDispItem(terrasteel, "ingotTerrasteel");
					PlusTiC.proxy.setRenderInfo(terrasteel, 0x00FF00);
					
					terrasteel.setFluid(terrasteelFluid);
					
					TinkerRegistry.addMaterialStats(terrasteel, new HeadMaterialStats(1562, 9, 6.5f, COBALT));
					TinkerRegistry.addMaterialStats(terrasteel, new HandleMaterialStats(1.4f, 10));
					TinkerRegistry.addMaterialStats(terrasteel, new ExtraMaterialStats(10));
					TinkerRegistry.addMaterialStats(terrasteel, new BowMaterialStats(0.55f, 2f, 11f));
					
					
					elementium.addTrait(Mana.mana);
					elementium.addTrait(Mana.mana, HEAD);
					elementium.addTrait(Elemental.elemental, HEAD);
					elementium.addItem("ingotElvenElementium", 1, Material.VALUE_Ingot);
					elementium.setCraftable(false).setCastable(true);
					Utils.setDispItem(elementium, "ingotElvenElementium");
					PlusTiC.proxy.setRenderInfo(elementium, 0xF66AFD);
					
					elementium.setFluid(elementiumFluid);
					
					TinkerRegistry.addMaterialStats(elementium, new HeadMaterialStats(540, 7.00f, 6.00f, OBSIDIAN),
							new HandleMaterialStats(1.25f, 150), new ExtraMaterialStats(60));
					TinkerRegistry.addMaterialStats(elementium, new BowMaterialStats(0.8f, 1.5f, 7.5f));
					
					
					manasteel.addTrait(Mana.mana);
					manasteel.addItem("ingotManasteel", 1, Material.VALUE_Ingot);
					manasteel.setCraftable(false).setCastable(true);
					Utils.setDispItem(manasteel, "ingotManasteel");
					PlusTiC.proxy.setRenderInfo(manasteel, 0x54E5FF);
					
					manasteel.setFluid(manasteelFluid);
					
					TinkerRegistry.addMaterialStats(manasteel, new HeadMaterialStats(540, 7.00f, 6.00f, OBSIDIAN),
							new HandleMaterialStats(1.25f, 150), new ExtraMaterialStats(60));
					TinkerRegistry.addMaterialStats(manasteel, new BowMaterialStats(1, 1.1f, 1));
					
					
					livingwood.addTrait(Botanical.botanical.get(1), HEAD);
					livingwood.addTrait(ecological, HEAD);
					livingwood.addTrait(Botanical.botanical.get(0));
					livingwood.addTrait(ecological);
					livingwood.addItem("livingwood", 1, Material.VALUE_Ingot);
					livingwood.setCraftable(true);
					Utils.setDispItem(livingwood, "livingwood");
					PlusTiC.proxy.setRenderInfo(livingwood, 0x560018);
					TinkerRegistry.addMaterialStats(livingwood, new HeadMaterialStats(50, 5.1f, 2.8f, IRON),
							new HandleMaterialStats(1.15f, 20), new ExtraMaterialStats(20),
							new BowMaterialStats(1.1f, 1.1f, 1.8f), new ArrowShaftMaterialStats(1f, 6));
					
					
					// MIRION ALLOY
					Utils.ItemMatGroup mirionGroup = Utils.registerMatGroup("mirion");
					
					mirion.addTrait(Mirabile.mirabile, HEAD);
					mirion.addTrait(Mana.mana, HEAD);
					mirion.addTrait(Mana.mana);
					mirion.addItem("ingotMirion", 1, Material.VALUE_Ingot);
					mirion.setCraftable(false).setCastable(true);
					mirion.setRepresentativeItem(mirionGroup.ingot);
					PlusTiC.proxy.setRenderInfo(mirion, 0xDDFF00);
					
					mirion.setFluid(mirionFluid);
					TinkerRegistry.registerAlloy(new FluidStack(mirionFluid, 4 * 18), new FluidStack(terrasteelFluid, 18),
							new FluidStack(manasteelFluid, 18), new FluidStack(elementiumFluid, 18),
							new FluidStack(TinkerFluids.cobalt, 18), new FluidStack(TinkerFluids.glass, 125));
					
					TinkerRegistry.addMaterialStats(mirion, new HeadMaterialStats(1919, 9, 9, 5));
					TinkerRegistry.addMaterialStats(mirion, new HandleMaterialStats(1.1f, 40));
					TinkerRegistry.addMaterialStats(mirion, new ExtraMaterialStats(90));
					TinkerRegistry.addMaterialStats(mirion, new BowMaterialStats(1.35f, 1.5f, 5.5f));
				});
				
				PlusTiC.materials.putAll(botaniaMaterials);
				PlusTiC.materialIntegrationStages.putAll(Maps.transformValues(botaniaMaterials, mat -> integrationPromise));
			}
		}
	}
	
	@SubscribeEvent
	public static void onItemReg(RegistryEvent.Register<Item> event) {
		itemPromise.complete(null);
	}
	
	public void init2() {
		if (Config.botania && Config.forceOutNaturalPledgeMaterials) {
			Utils.forceOutModsMaterial("terrasteel", "botanicaladdons");
			Utils.forceOutModsMaterial("elementium", "botanicaladdons");
			Utils.forceOutModsMaterial("manasteel", "botanicaladdons");
			Utils.forceOutModsMaterial("livingwood", "botanicaladdons");
		}
	}
	
}
