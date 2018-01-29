package landmaster.plustic;

import java.util.*;
import java.util.Optional;
import java.util.concurrent.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;

import gnu.trove.map.hash.*;
import landmaster.plustic.proxy.*;
import landmaster.plustic.api.*;
import landmaster.plustic.config.*;
import landmaster.plustic.modules.*;
import landmaster.plustic.net.*;
import landmaster.plustic.util.*;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.Mod.*;
import slimeknights.tconstruct.library.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.tools.*;

@Mod.EventBusSubscriber
@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDS, useMetadata = true, acceptedMinecraftVersions = "[1.12, 1.13)")
public class PlusTiC {
	public static Config config;
	
	@Instance(ModInfo.MODID)
	public static PlusTiC INSTANCE;
	
	@SidedProxy(serverSide = "landmaster.plustic.proxy.CommonProxy", clientSide = "landmaster.plustic.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static final Logger log = LogManager.getLogger(
			ModInfo.MODID.toUpperCase(Locale.US/* to avoid problems with Turkish */));
	
	public static final Map<String, Material> materials = new THashMap<>();
	public static final Map<String, MaterialIntegration> materialIntegrations = new THashMap<>();
	
	public static final Map<String, CompletionStage<?>> materialIntegrationStages = new THashMap<>();
	
	public static final BowMaterialStats justWhy = new BowMaterialStats(0.2f, 0.4f, -1f);
	
	private static final String[] renamesToHandle = new String[] { "osmium", "titanium", "iridium" };
	
	@SubscribeEvent
	public static void missingBlockMappings(RegistryEvent.MissingMappings<Block> event) {
		event.getMappings().forEach(mapping -> {
			for (String name: renamesToHandle) {
				if (mapping.key.equals(new ResourceLocation(ModInfo.MODID, ModInfo.MODID+".molten_"+name))) {
					Optional.ofNullable(FluidRegistry.getFluid(name))
					.map(Fluid::getBlock)
					.ifPresent(mapping::remap);
				}
			}
		});
	}
	
	@SubscribeEvent
	public static void missingItemMappings(RegistryEvent.MissingMappings<Item> event) {
		event.getMappings().forEach(mapping -> {
			for (String name: renamesToHandle) {
				if (mapping.key.equals(new ResourceLocation(ModInfo.MODID, ModInfo.MODID+".molten_"+name))) {
					Optional.ofNullable(FluidRegistry.getFluid(name))
					.map(Fluid::getBlock)
					.map(Item::getItemFromBlock)
					.ifPresent(mapping::remap);
				}
			}
		});
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		(config = new Config(event)).init1();
		
		proxy.initEntities();
		
		// TODO add more modules when needed
		
		IModule.modules.addAll(Arrays.asList(
				new ModuleBase(),
				new ModuleNatura(),
				new ModuleBoP(),
				new ModuleMekanism(),
				new ModuleAdvRocketry(),
				new ModuleBotania(),
				new ModuleArmorPlus(),
				new ModuleEnderIO(),
				new ModuleTF(),
				new ModuleDraconicEvolution(),
				new ModuleActAdd(),
				new ModulePsi(),
				new ModuleAvaritia(),
				new ModuleLandCraft(),
				new ModuleLandCore(),
				new ModuleGalacticraft(),
				new ModuleSurvivalist(),
				new ModuleProjectE(),
				new ModuleGemsPlus(),
				new ModuleAppEng2(),
				new ModuleEnvironTech(),
				new ModuleTools(),
				new ModuleModifiers()
				));
		
		IModule.modules.forEach(IModule::init);
		
		config.init2(materials);
		
		config.update();
		
		preIntegrate(materials, materialIntegrations, materialIntegrationStages);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.initToolGuis();
		proxy.registerKeyBindings();
		PacketHandler.init();
		
		IModule.modules.forEach(IModule::init2);
		
		postIntegrate();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Seriously? Registering oredicts *this* late? -_-
		Utils.setDispItem(materials.get("desh"), "ingotDesh");
	}
	
	private static void preIntegrate(Map<String,Material> materials,
			Map<String,MaterialIntegration> materialIntegrations,
			Map<String, CompletionStage<?>> materialIntegrationStages) {
		materials.forEach((k, v) -> {
			if (!materialIntegrations.containsKey(k)) {
				materialIntegrationStages.getOrDefault(k, CompletableFuture.completedFuture(null)).thenRun(() -> {
					MaterialIntegration mi;
					if (v.getRepresentativeItem().getItem() == Items.EMERALD) {
						mi = new MaterialIntegration(v, v.getFluid());
					} /*glass*/ else if(v.getRepresentativeItem().getItem() == Item.getItemFromBlock(Blocks.GLASS)) {
						mi = new MaterialIntegration(v, v.getFluid());
					} else if (v.getFluid() != null) {
						mi = new MaterialIntegration(v, v.getFluid(), StringUtils.capitalize(k)).toolforge();
					} else {
						mi = new MaterialIntegration(v);
					}
					TinkerRegistry.integrate(mi).preInit();
					materialIntegrations.put(k, mi);
				});
			}
		});
	}
	
	private static void postIntegrate() {
		Utils.displace(TinkerMaterials.wood.getIdentifier()); // so that natura woods are prioritized
	}
}
