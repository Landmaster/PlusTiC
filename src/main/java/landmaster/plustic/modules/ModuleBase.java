package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import java.util.*;
import java.util.concurrent.*;

import landmaster.plustic.*;
import landmaster.plustic.api.*;
import landmaster.plustic.config.*;
import landmaster.plustic.fluids.*;
import landmaster.plustic.tools.stats.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.init.*;
import net.minecraft.item.Item;
import net.minecraft.util.text.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.shared.*;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class ModuleBase implements IModule {
	private static CompletableFuture<?> emeraldStage = new CompletableFuture<>();
	private static CompletableFuture<?> glassStage = new CompletableFuture<>();
	
	public void init() {
		if (Config.base) {
			Material tnt = new Material("tnt", TextFormatting.RED);
			tnt.addTrait(Explosive.explosive);
			tnt.addItem(Blocks.TNT, Material.VALUE_Ingot);
			tnt.setRepresentativeItem(Blocks.TNT);
			tnt.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(tnt, 0xFF4F4F);
			TinkerRegistry.addMaterialStats(tnt, new ArrowShaftMaterialStats(0.95f, 0));
			PlusTiC.materials.put("tnt", tnt);
			
			Material emerald = new Material("emerald_plustic", TextFormatting.GREEN);
			emerald.addTrait(Terrafirma.terrafirma.get(0));
			emerald.addTrait(Elemental.elemental, HEAD);
			emerald.addItem("gemEmerald", 1, Material.VALUE_Ingot);
			emerald.setRepresentativeItem(Items.EMERALD);
			emerald.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(emerald, 0x13DB52);
			CompletableFuture<?> emeraldStage1 = emeraldStage.thenRun(() -> emerald.setFluid(TinkerFluids.emerald));
			TinkerRegistry.addMaterialStats(emerald, new HeadMaterialStats(1222, 7, 7, COBALT),
					new HandleMaterialStats(1.1f, 0),
					new ExtraMaterialStats(70),
					new BowMaterialStats(1.1f, 1, 0.9f));
			PlusTiC.materials.put("emerald", emerald);
			PlusTiC.materialIntegrationStages.put("emerald", emeraldStage1);
			
			// Glass
			Material glass = new Material("glass_plustic", TextFormatting.WHITE);
			glass.addTrait(splinters);
			glass.addTrait(splinters, HEAD);
			glass.addTrait(jagged, HEAD);
			glass.addTrait(sharp, HEAD);
			glass.addItem("blockGlass", 1, Material.VALUE_Ingot);
			glass.setRepresentativeItem(Blocks.GLASS);
			glass.setCraftable(false).setCastable(true);
			PlusTiC.proxy.setRenderInfo(glass, 0xFFFFFF);
			CompletableFuture<?> glassStage1 = glassStage.thenRun(() -> glass.setFluid(TinkerFluids.glass));
			TinkerRegistry.addMaterialStats(glass, new HeadMaterialStats(10, 4, 5, STONE),
					new HandleMaterialStats(0.2F, 6),
					new ExtraMaterialStats(8),
					new BowMaterialStats(0.2f, 0.4f, -1f));
			PlusTiC.materials.put("glass", glass);
			PlusTiC.materialIntegrationStages.put("glass", glassStage1);

			
			// alumite is back! (with some changes)
			Utils.ItemMatGroup alumiteGroup = Utils.registerMatGroup("alumite");
			
			Material alumite = new Material("alumite", TextFormatting.RED);
			alumite.addTrait(Global.global);
			alumite.addItem("ingotAlumite", 1, Material.VALUE_Ingot);
			alumite.setCraftable(false).setCastable(true);
			alumite.setRepresentativeItem(alumiteGroup.ingot);
			PlusTiC.proxy.setRenderInfo(alumite, 0xFFE0F1);
			
			FluidMolten alumiteFluid = Utils.fluidMetal("alumite", 0xFFE0F1);
			alumiteFluid.setTemperature(890);
			Utils.initFluidMetal(alumiteFluid);
			alumite.setFluid(alumiteFluid);
			
			TinkerRegistry.addMaterialStats(alumite, new HeadMaterialStats(700, 6.8f, 5.5f, COBALT),
					new HandleMaterialStats(1.10f, 70), new ExtraMaterialStats(80),
					new BowMaterialStats(0.65f, 1.6f, 7f));
			
			PlusTiC.materials.put("alumite", alumite);
			
			if (TinkerRegistry.getMaterial("nickel") == Material.UNKNOWN) {
				Material nickel = new Material("nickel", TextFormatting.YELLOW);
				nickel.addTrait(NickOfTime.nickOfTime, HEAD);
				nickel.addTrait(magnetic);
				nickel.addItem("ingotNickel", 1, Material.VALUE_Ingot);
				nickel.setCraftable(false).setCastable(true);
				new OreRegisterPromise("ingotNickel").thenAccept(nickel::setRepresentativeItem);
				PlusTiC.proxy.setRenderInfo(nickel, 0xFFF98E);
				
				nickel.setFluid(TinkerFluids.nickel);
				
				TinkerRegistry.addMaterialStats(nickel, new HeadMaterialStats(460, 6, 4.5f, OBSIDIAN),
						new HandleMaterialStats(1, -5), new ExtraMaterialStats(70), PlusTiC.justWhy,
						new FletchingMaterialStats(0.95f, 1.05f), new BatteryCellMaterialStats(75000));
				
				PlusTiC.materials.put("nickel", nickel);
			}
			
			if (TinkerRegistry.getMaterial("invar") == Material.UNKNOWN) {
				Utils.ItemMatGroup invarGroup = Utils.registerMatGroup("invar");
				
				Material invar = new Material("invar", 0xD6D6D6);
				invar.addTrait(DevilsStrength.devilsstrength);
				invar.addTrait(magnetic);
				invar.addItem("ingotInvar", 1, Material.VALUE_Ingot);
				invar.setCraftable(false).setCastable(true);
				invar.setRepresentativeItem(invarGroup.ingot);
				PlusTiC.proxy.setRenderInfo(invar, 0xD6D6D6);
				
				FluidMolten invarFluid = Utils.fluidMetal("invar", 0xD6D6D6);
				invarFluid.setTemperature(1000);
				Utils.initFluidMetal(invarFluid);
				invar.setFluid(invarFluid);
				
				TinkerRegistry.addMaterialStats(invar,
						new HeadMaterialStats(600, 6, 5f, OBSIDIAN),
						new HandleMaterialStats(1.3f, 0),
						new ExtraMaterialStats(100),
						PlusTiC.justWhy,
						new FletchingMaterialStats(1, 1.15f));
				
				PlusTiC.materials.put("invar", invar);
			}
			
			if (TinkerRegistry.getMaterial("iridium") == Material.UNKNOWN) {
				Material iridium = new Material("iridium", TextFormatting.GRAY);
				iridium.addTrait(dense);
				iridium.addTrait(alien, HEAD);
				iridium.addItem("ingotIridium", 1, Material.VALUE_Ingot);
				iridium.setCraftable(false).setCastable(true);
				new OreRegisterPromise("ingotIridium").thenAccept(iridium::setRepresentativeItem);
				PlusTiC.proxy.setRenderInfo(iridium, 0xE5E5E5);
				
				FluidMolten iridiumFluid = Utils.fluidMetal("iridium", 0xE5E5E5);
				iridiumFluid.setTemperature(810);
				Utils.initFluidMetal(iridiumFluid);
				iridium.setFluid(iridiumFluid);
				
				TinkerRegistry.addMaterialStats(iridium, new HeadMaterialStats(520, 6, 5.8f, DIAMOND));
				TinkerRegistry.addMaterialStats(iridium, new HandleMaterialStats(1.15f, -20));
				TinkerRegistry.addMaterialStats(iridium, new ExtraMaterialStats(60));
				TinkerRegistry.addMaterialStats(iridium, PlusTiC.justWhy);
				
				PlusTiC.materials.put("iridium", iridium);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void itemRegEvent(RegistryEvent.Register<Item> event) {
		emeraldStage.complete(null);
		glassStage.complete(null);
	}
	
	public void init2() {
		Optional.ofNullable(PlusTiC.materials.get("alumite"))
		.map(Material::getFluid)
		.ifPresent(alumiteFluid -> {
			if (FluidRegistry.isFluidRegistered(TinkerFluids.aluminum)) {
				TinkerRegistry.registerAlloy(new FluidStack(alumiteFluid, 3), new FluidStack(TinkerFluids.aluminum, 5),
						new FluidStack(TinkerFluids.iron, 2), new FluidStack(TinkerFluids.obsidian, 2));
			}
		});
		
		Optional.ofNullable(PlusTiC.materials.get("invar"))
		.map(Material::getFluid)
		.ifPresent(invarFluid -> {
			if (FluidRegistry.isFluidRegistered(TinkerFluids.nickel)) {
				TinkerRegistry.registerAlloy(new FluidStack(invarFluid, 3),
						new FluidStack(TinkerFluids.iron, 2),
						new FluidStack(TinkerFluids.nickel, 1));
			}
		});
	}
	
}
