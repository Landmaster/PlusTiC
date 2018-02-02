package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import java.util.concurrent.*;

import com.progwml6.natura.shared.*;

import landmaster.plustic.*;
import landmaster.plustic.api.*;
import landmaster.plustic.config.*;
import landmaster.plustic.traits.*;
import net.minecraft.item.*;
import net.minecraft.util.text.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

@Mod.EventBusSubscriber(modid = ModInfo.MODID)
public class ModuleNatura implements IModule {
	private static final CompletableFuture<?> itemPromise = new CompletableFuture<>();

	public void init() {
		if (Config.natura && Loader.isModLoaded("natura")) {
			Material darkwood = new Material("darkwood_plustic", TextFormatting.DARK_BLUE);
			darkwood.addTrait(DarkTraveler.darktraveler);
			darkwood.addTrait(ecological);
			CompletableFuture<?> darkwoodPromise = itemPromise.thenRun(() -> {
				darkwood.addItem(ModuleNaturaStuff.darkwoodPlankStack(), 1, Material.VALUE_Ingot);
				darkwood.addItem(ModuleNaturaStuff.darkwoodLogStack(), 1, 4*Material.VALUE_Ingot);
				darkwood.addItem(NaturaCommons.darkwood_stick, 1, Material.VALUE_Shard);
				darkwood.setRepresentativeItem(ModuleNaturaStuff.darkwoodPlankStack());
			});
			darkwood.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(darkwood, 0x000044);
			TinkerRegistry.addMaterialStats(darkwood,
					new HeadMaterialStats(350, 5f, 3f, COBALT),
	                new HandleMaterialStats(1.3f, -5),
	                new ExtraMaterialStats(90),
	                new BowMaterialStats(1.2f, 1.3f, 3));
			PlusTiC.materials.put("darkwood", darkwood);
			PlusTiC.materialIntegrationStages.put("darkwood", darkwoodPromise);
			
			Material ghostwood = new Material("ghostwood_plustic", TextFormatting.WHITE);
			ghostwood.addTrait(Ghastly.ghastly);
			ghostwood.addTrait(ecological);
			CompletableFuture<?> ghostwoodPromise = itemPromise.thenRun(() -> {
				ghostwood.addItem(ModuleNaturaStuff.ghostwoodPlankStack(), 1, Material.VALUE_Ingot);
				ghostwood.addItem(ModuleNaturaStuff.ghostwoodLogStack(), 1, 4*Material.VALUE_Ingot);
				ghostwood.addItem(NaturaCommons.ghostwood_stick, 1, Material.VALUE_Shard);
				ghostwood.setRepresentativeItem(ModuleNaturaStuff.ghostwoodPlankStack());
			});
			ghostwood.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(ghostwood, 0xFFFFFF);
			TinkerRegistry.addMaterialStats(ghostwood,
					new HeadMaterialStats(300, 4.9f, 2.5f, COBALT),
	                new HandleMaterialStats(1.1f, 0),
	                new ExtraMaterialStats(100),
	                new BowMaterialStats(1.6f, 1.1f, 2));
			PlusTiC.materials.put("ghostwood", ghostwood);
			PlusTiC.materialIntegrationStages.put("ghostwood", ghostwoodPromise);
			
			Material fusewood = new Material("fusewood_plustic", 0x00D18E);
			fusewood.addTrait(DPRK.dprk);
			fusewood.addTrait(ecological);
			CompletableFuture<?> fusewoodPromise = itemPromise.thenRun(() -> {
				fusewood.addItem(ModuleNaturaStuff.fusewoodPlankStack(), 1, Material.VALUE_Ingot);
				fusewood.addItem(ModuleNaturaStuff.fusewoodLogStack(), 1, 4*Material.VALUE_Ingot);
				fusewood.addItem(NaturaCommons.fusewood_stick, 1, Material.VALUE_Shard);
				fusewood.setRepresentativeItem(ModuleNaturaStuff.fusewoodPlankStack());
			});
			fusewood.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(fusewood, 0x00D18E);
			TinkerRegistry.addMaterialStats(fusewood,
					new HeadMaterialStats(430, 6.0f, 4f, COBALT),
	                new HandleMaterialStats(1.0f, -20),
	                new ExtraMaterialStats(50),
	                new BowMaterialStats(0.7f, 2.0f, 7));
			PlusTiC.materials.put("fusewood", fusewood);
			PlusTiC.materialIntegrationStages.put("fusewood", fusewoodPromise);
			
			Material flamestring = new Material("flamestring_plustic", 0xFF3314);
			flamestring.addTrait(Naphtha.naphtha);
			CompletableFuture<?> flamestringPromise = itemPromise.thenRun(() -> {
				flamestring.addItem(NaturaCommons.flameString, 1, Material.VALUE_Ingot);
				flamestring.setRepresentativeItem(NaturaCommons.flameString);
			});
			flamestring.setCraftable(true);
			PlusTiC.proxy.setRenderInfo(flamestring, 0xFF3314);
			TinkerRegistry.addMaterialStats(flamestring, new BowStringMaterialStats(1.2f));
			PlusTiC.materials.put("flamestring", flamestring);
			PlusTiC.materialIntegrationStages.put("flamestring", flamestringPromise);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void itemRegEvent(RegistryEvent.Register<Item> event) {
		itemPromise.complete(null);
	}
}
