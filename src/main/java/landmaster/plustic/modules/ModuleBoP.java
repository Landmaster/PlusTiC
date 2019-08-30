package landmaster.plustic.modules;

import static slimeknights.tconstruct.library.materials.MaterialTypes.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.*;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

import landmaster.plustic.*;
import landmaster.plustic.config.*;
import landmaster.plustic.traits.*;
import landmaster.plustic.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;

public class ModuleBoP implements IModule {

	public void init() {
		final boolean isBoPLoaded = Loader.isModLoaded("BiomesOPlenty") || Loader.isModLoaded("biomesoplenty");
		
		if ((Config.bop && isBoPLoaded) || (Config.projectRed && Loader.isModLoaded("projectred-core")) || (Config.aoa3 && Loader.isModLoaded("aoa3"))) {
			Material sapphire = new Material("sapphire", TextFormatting.BLUE);
			sapphire.addTrait(aquadynamic);
			sapphire.addItem("gemSapphire", 1, Material.VALUE_Ingot);
			sapphire.setCraftable(true);
			new OreRegisterPromise("gemSapphire").thenAccept(sapphire::setRepresentativeItem);
			PlusTiC.proxy.setRenderInfo(sapphire, 0x0000FF);
			TinkerRegistry.addMaterialStats(sapphire, new HeadMaterialStats(700, 5, 6.4f, OBSIDIAN));
			TinkerRegistry.addMaterialStats(sapphire, new HandleMaterialStats(1, 100));
			TinkerRegistry.addMaterialStats(sapphire, new ExtraMaterialStats(120));
			TinkerRegistry.addMaterialStats(sapphire, new BowMaterialStats(1, 1.5f, 4));
			PlusTiC.materials.put("sapphire", sapphire);
		}
		if ((Config.bop && isBoPLoaded) || (Config.projectRed && Loader.isModLoaded("projectred-core"))) {
			Material ruby = new Material("ruby", TextFormatting.RED);
			ruby.addTrait(BloodyMary.bloodymary);
			ruby.addTrait(sharp, HEAD);
			ruby.addItem("gemRuby", 1, Material.VALUE_Ingot);
			ruby.setCraftable(true);
			new OreRegisterPromise("gemRuby").thenAccept(ruby::setRepresentativeItem);
			PlusTiC.proxy.setRenderInfo(ruby, 0xFF0000);
			TinkerRegistry.addMaterialStats(ruby, new HeadMaterialStats(660, 4.6f, 6.4f, OBSIDIAN));
			TinkerRegistry.addMaterialStats(ruby, new HandleMaterialStats(1.2f, 0));
			TinkerRegistry.addMaterialStats(ruby, new ExtraMaterialStats(20));
			TinkerRegistry.addMaterialStats(ruby, new BowMaterialStats(1.5f, 1.4f, 4));
			PlusTiC.materials.put("ruby", ruby);
			
			Material peridot = new Material("peridot", TextFormatting.GREEN);
			peridot.addTrait(NaturesBlessing.naturesblessing);
			peridot.addItem("gemPeridot", 1, Material.VALUE_Ingot);
			peridot.setCraftable(true);
			new OreRegisterPromise("gemPeridot").thenAccept(peridot::setRepresentativeItem);
			PlusTiC.proxy.setRenderInfo(peridot, 0xBEFA5C);
			TinkerRegistry.addMaterialStats(peridot, new HeadMaterialStats(640, 4.0f, 6.1f, OBSIDIAN));
			TinkerRegistry.addMaterialStats(peridot, new HandleMaterialStats(1.3f, -30));
			TinkerRegistry.addMaterialStats(peridot, new ExtraMaterialStats(20));
			TinkerRegistry.addMaterialStats(peridot, new BowMaterialStats(1.4f, 1.4f, 4));
			PlusTiC.materials.put("peridot", peridot);
		}
		if ((Config.bop && isBoPLoaded) || (Config.aoa3 && Loader.isModLoaded("aoa3"))){
			Material amethyst = new Material("amethyst", TextFormatting.LIGHT_PURPLE);
			amethyst.addTrait(Apocalypse.apocalypse);
			amethyst.addItem("gemAmethyst", 1, Material.VALUE_Ingot);
			amethyst.setCraftable(true);
			new OreRegisterPromise("gemAmethyst").thenAccept(amethyst::setRepresentativeItem);
			PlusTiC.proxy.setRenderInfo(amethyst, 0xFF00FF);
			TinkerRegistry.addMaterialStats(amethyst, new HeadMaterialStats(1200, 6, 10, COBALT));
			TinkerRegistry.addMaterialStats(amethyst, new HandleMaterialStats(1.6f, 100));
			TinkerRegistry.addMaterialStats(amethyst, new ExtraMaterialStats(100));
			TinkerRegistry.addMaterialStats(amethyst, new BowMaterialStats(0.65f, 1.7f, 6.5f));
			PlusTiC.materials.put("amethyst", amethyst);
		}
		if (Config.bop && isBoPLoaded) {
			Material malachite = new Material("malachite_gem", TextFormatting.DARK_GREEN);
			malachite.addTrait(NaturesWrath.natureswrath);
			malachite.addItem("gemMalachite", 1, Material.VALUE_Ingot);
			malachite.setCraftable(true);
			Utils.setDispItem(malachite, "biomesoplenty", "gem", 5);
			PlusTiC.proxy.setRenderInfo(malachite, 0x007523);
			TinkerRegistry.addMaterialStats(malachite, new HeadMaterialStats(640, 3.0f, 6.1f, OBSIDIAN));
			TinkerRegistry.addMaterialStats(malachite, new HandleMaterialStats(1.3f, -30));
			TinkerRegistry.addMaterialStats(malachite, new ExtraMaterialStats(20));
			TinkerRegistry.addMaterialStats(malachite, new BowMaterialStats(1.4f, 1.4f, 4));
			PlusTiC.materials.put("malachite", malachite);
			
			Material amber = new Material("amber", TextFormatting.GOLD);
			amber.addTrait(shocking);
			amber.addTrait(Thundering.thundering, PROJECTILE);
			amber.addTrait(Thundering.thundering, SHAFT);
			amber.addItem("gemAmber", 1, Material.VALUE_Ingot);
			amber.setCraftable(true);
			Utils.setDispItem(amber, "biomesoplenty", "gem", 7);
			PlusTiC.proxy.setRenderInfo(amber, 0xFFD000);
			TinkerRegistry.addMaterialStats(amber, new HeadMaterialStats(730, 4.6f, 5.7f, OBSIDIAN));
			TinkerRegistry.addMaterialStats(amber, new HandleMaterialStats(1, 30));
			TinkerRegistry.addMaterialStats(amber, new ExtraMaterialStats(100));
			TinkerRegistry.addMaterialStats(amber, PlusTiC.justWhy);
			TinkerRegistry.addMaterialStats(amber, new ArrowShaftMaterialStats(1, 5));
			PlusTiC.materials.put("amber", amber);
			
			Material topaz = new Material("topaz", TextFormatting.GOLD);
			topaz.addTrait(NaturesPower.naturespower);
			topaz.addItem("gemTopaz", 1, Material.VALUE_Ingot);
			topaz.setCraftable(true);
			Utils.setDispItem(topaz, "biomesoplenty", "gem", 3);
			PlusTiC.proxy.setRenderInfo(topaz, 0xFFFF00);
			TinkerRegistry.addMaterialStats(topaz, new HeadMaterialStats(690, 6, 6, OBSIDIAN));
			TinkerRegistry.addMaterialStats(topaz, new HandleMaterialStats(0.8f, 70));
			TinkerRegistry.addMaterialStats(topaz, new ExtraMaterialStats(65));
			TinkerRegistry.addMaterialStats(topaz, new BowMaterialStats(0.4f, 1.4f, 7));
			PlusTiC.materials.put("topaz", topaz);
			
			Material tanzanite = new Material("tanzanite", TextFormatting.LIGHT_PURPLE);
			tanzanite.addTrait(freezing);
			tanzanite.addItem("gemTanzanite", 1, Material.VALUE_Ingot);
			tanzanite.setCraftable(true);
			Utils.setDispItem(tanzanite, "biomesoplenty", "gem", 4);
			PlusTiC.proxy.setRenderInfo(tanzanite, 0x6200FF);
			TinkerRegistry.addMaterialStats(tanzanite, new HeadMaterialStats(650, 3, 7, OBSIDIAN));
			TinkerRegistry.addMaterialStats(tanzanite, new HandleMaterialStats(0.7f, 0));
			TinkerRegistry.addMaterialStats(tanzanite, new ExtraMaterialStats(25));
			TinkerRegistry.addMaterialStats(tanzanite, PlusTiC.justWhy);
			PlusTiC.materials.put("tanzanite", tanzanite);
		}
	}
	
}
