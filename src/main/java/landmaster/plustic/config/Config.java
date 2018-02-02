package landmaster.plustic.config;

import java.io.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;

import com.google.common.base.Throwables;
import com.google.common.collect.*;

import gnu.trove.*;
import gnu.trove.list.*;
import gnu.trove.list.array.*;
import landmaster.plustic.traits.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.*;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.event.*;
import slimeknights.tconstruct.library.materials.*;

public class Config extends Configuration {
	// MODULES
	public static boolean base;
	public static boolean bop;
	public static boolean projectRed;
	public static boolean mekanism;
	public static boolean botania;
	public static boolean advancedRocketry;
	public static boolean armorPlus;
	public static boolean enderIO;
	public static boolean thermalFoundation;
	public static boolean draconicEvolution;
	public static boolean actuallyAdditions;
	public static boolean substratum;
	public static boolean natura;
	public static boolean psi;
	public static boolean avaritia;
	public static boolean landCraft;
	public static boolean landCore;
	public static boolean mfr;
	public static boolean galacticraft;
	public static boolean survivalist;
	public static boolean projectE;
	public static boolean gemsplus;
	public static boolean appEng2;
	public static boolean environTech;
	public static boolean twilightForest;
	
	// alas…
	public static boolean forceOutNaturalPledgeMaterials;
	
	public static boolean pyrotheumSmelt;
	public static boolean tfMelt;
	
	public static boolean alloyDarkSteel;
	
	public static boolean katana;
	public static boolean laserGun;
	
	public static float katana_combo_multiplier;
	public static boolean katana_boosts_only_on_killing;
	public static boolean katana_smooth_progression;
	
	private static final TIntArrayList botan_amount = new TIntArrayList(Botanical.MAX_LEVELS);
	
	public static TIntList getBotanAmount() {
		return TCollections.unmodifiableList(botan_amount);
	}
	
	private static class TrashThing {
		public final int weight;
		public final ItemStack stack;
		
		public TrashThing(int weight, ItemStack stack) {
			this.weight = weight;
			this.stack = stack;
		}
	}
	
	private static final List<TrashThing> trashThings = new ArrayList<>();
	
	private static int trashThingsSum = 0;
	public static @javax.annotation.Nullable ItemStack fetchThing(Random random) {
		if (trashThingsSum <= 0) {
			trashThingsSum = trashThings.stream().mapToInt(t -> t.weight).sum();
		}
		int rval = random.nextInt(trashThingsSum);
		ItemStack thing = ItemStack.EMPTY;
		for (TrashThing entry: trashThings) {
			rval -= entry.weight;
			thing = entry.stack;
			if (rval < 0) break;
		}
		return thing;
	}
	
	private static final ArrayListMultimap<Item, ItemStack> endlectricBlacklist = ArrayListMultimap.create();
	
	public Config(FMLPreInitializationEvent event) {
		super(event.getSuggestedConfigurationFile());
	}
	
	public void init1() {
		this.addCustomCategoryComment("modules", "Use this to disable entire modules.");
		
		// MODULES
		base = getBoolean("Enable vanilla TiC addons", "modules", true, "Add features to vanilla Tinkers Construct");
		bop = getBoolean("Enable BoP integration", "modules", true, "Integrate with Biomes o' Plenty");
		projectRed = getBoolean("Enable Project Red integration", "modules", true, "Integrate with Project Red-Exploration");
		mekanism = getBoolean("Enable Mekanism integration", "modules", true, "Integrate with Mekanism");
		botania = getBoolean("Enable Botania integration", "modules", true, "Integrate with Botania");
		{
			forceOutNaturalPledgeMaterials = getBoolean("Force out Natural Pledge TiC materials", "tweaks", true,
					"If Natural Pledge is loaded:\n"
					+ "If true: replace Natural Pledge Botania TiC materials with PlusTiC ones;\n"
					+ "Otherwise: do *not* load the Botania module for PlusTiC, overriding other settings.");
		}
		advancedRocketry = getBoolean("Enable Advanced Rocketry integration", "modules", true, "Integrate with Advanced Rocketry (actually LibVulpes)");
		armorPlus = getBoolean("Enable ArmorPlus integration", "modules", true, "Integrate with ArmorPlus");
		enderIO = getBoolean("Enable EnderIO integration", "modules", true, "Integrate with EnderIO");
		{
			alloyDarkSteel = getBoolean("Alloy Dark Steel in smeltery", "tweaks", true, "Allow Dark Steel alloying in the TiC smeltery");
		}
		thermalFoundation = getBoolean("Enable Thermal Foundation integration", "modules", true, "Integrate with Thermal Foundation");
		{
			pyrotheumSmelt = getBoolean("Use Pyrotheum as smeltery fuel", "tweaks", true, "Use Pyrotheum as TiC smeltery fuel (only if Thermal Foundation is loaded)");
			tfMelt = getBoolean("Add smeltery recipes for Redstone, Glowstone, and Ender pearl", "tweaks", true, "Add smelting recipes for Redstone, Glowstone, and Ender pearl (only if Thermal Foundation is loaded)");
		}
		draconicEvolution = getBoolean("Enable Draconic Evolution integration", "modules", true, "Integrate with Draconic Evolution");
		actuallyAdditions = getBoolean("Enable Actually Additions support", "modules", true, "Integrate with Actually Additions");
		substratum = getBoolean("Enable Substratum support", "modules", true, "Integrate with Substratum");
		natura = getBoolean("Enable Natura support", "modules", true, "Integrate with Natura");
		psi = getBoolean("Enable Psi support", "modules", true, "Integrate with Psi");
		avaritia = getBoolean("Enable Avaritia support", "modules", true, "Integrate with Avaritia");
		landCraft = getBoolean("Enable Land Craft support", "modules", true, "Integrate with Land Craft");
		landCore = getBoolean("Enable LandCore support", "modules", true, "Integrate with LandCore");
		mfr = getBoolean("Enable MFR support", "modules", true, "Integrate with Minefactory Reloaded");
		galacticraft = getBoolean("Enable Galacticraft support", "modules", true, "Integrate with Galacticraft");
		survivalist = getBoolean("Enable Survivalist support", "modules", true, "Integrate with Survivalist");
		projectE = getBoolean("Enable ProjectE support", "modules", true, "Integrate with ProjectE");
		gemsplus = getBoolean("Enable Gems+ support", "modules", true, "Integrate with Gems+");
		appEng2 = getBoolean("Enable Applied Energistics 2 support", "modules", true, "Integrate with Applied Energistics 2");
		environTech = getBoolean("Enable Environmental Tech support", "modules", true, "Integrate with Environmental Tech");
		twilightForest = getBoolean("Enable Twilight Forest support", "modules", true, "Integrate with Twilight Forest");
		
		// TOOLS
		katana = getBoolean("Enable Katana", "tools", true, "Enable Katana");
		katana_combo_multiplier = getFloat("Katana combo multiplier", "tools", 1.25f, 0, Float.MAX_VALUE, "Multiply combo value by this to calculate bonus damage");
		katana_boosts_only_on_killing = getBoolean("Katana boosts only on killing", "tools", true, "Does Katana boost only on killing mob (true) or on any hit (false)?");
		katana_smooth_progression = getBoolean("Smooth Katana progression", "tools", false, "Should boosted damage of Katana change smoothly from material to material?");
		
		laserGun = getBoolean("Enable Laser Gun", "tools", true, "Enable Laser Gun");
		
		// Trash
		String[] trash_things_arr = getStringList("Trash generation", "tweaks",
				new String[] {"20|coal", "5|slime_ball", "10|saddle",
						"5|tconstruct:edible;1", "1|emerald", "3|melon"},
				"Objects that the Trash modifier will generate; enter in the format \"weight|modid:name;meta\" (leave meta blank for zero metadata)");
		{
			int meta = 0;
			int weight = 0;
			for (int i=0; i<trash_things_arr.length; ++i) {
				String[] trash_wi = trash_things_arr[i].split("\\|");
				try {
					weight = Integer.parseInt(trash_wi[0]);
					if (weight < 0) {
						throw new IllegalArgumentException("Weight must not be negative");
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				String[] loc_meta = trash_wi[1].split(";");
				if (loc_meta.length > 1) {
					try {
						meta = Integer.parseInt(loc_meta[1]);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				Item it = Item.REGISTRY.getObject(new ResourceLocation(loc_meta[0]));
				if (it != null && weight > 0) {
					trashThings.add(new TrashThing(weight, new ItemStack(it, 1, meta)));
				}
			}
		}
		
		// Endlectric
		
		String[] arr_endlectric = getStringList("Items that Endlectric will not drain from", "tweaks", new String[0], "Enter in the format \"modid:name;meta\" (leave meta blank to match any meta)");
		{
			int meta = -1;
			for (int i=0; i<arr_endlectric.length; ++i) {
				String[] loc_meta = arr_endlectric[i].split(";");
				if (loc_meta.length > 1) {
					try {
						meta = Integer.parseInt(loc_meta[1]);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				Item it = Item.REGISTRY.getObject(new ResourceLocation(loc_meta[0]));
				if (it != null) {
					endlectricBlacklist.put(it, (meta>=0 ? new ItemStack(it, 1, meta) : null));
				}
			}
		}
		
		// Modifier values for Botanical
		Property botan_amount_prop = this.get("tweaks", "Modifier values for Botanical", new int[0]);
		botan_amount_prop.setLanguageKey("Modifiers added for Botanical modifier");
		botan_amount_prop.setComment("Enter integer amounts in order of level (defaults will be extrapolated if some left blank)");
		botan_amount_prop.setMinValue(0);
		
		botan_amount.add(botan_amount_prop.getIntList());
		if (botan_amount.isEmpty()) botan_amount.add(1);
		while (botan_amount.size() < Botanical.MAX_LEVELS) {
			botan_amount.add(botan_amount.get(botan_amount.size()-1)<<1);
		}
	}
	
	private static final MethodHandle injectHandle;
	static {
		try {
			Method temp = LanguageMap.class.getDeclaredMethod("inject", LanguageMap.class, InputStream.class);
			temp.setAccessible(true);
			injectHandle = MethodHandles.lookup().unreflect(temp);
		} catch (Throwable e) {
			Throwables.throwIfUnchecked(e);
			throw new RuntimeException(e);
		}
	}
	
	private static final LanguageMap englishMap = new LanguageMap();
	static {
		try {
			final String[] langFiles = new String[] { "/assets/plustic/lang/en_us.lang", "/assets/plustic/lang/en_US.lang" };
			for (String langFile: langFiles) {
				try (InputStream inS = Config.class.getResourceAsStream(langFile)) {
					injectHandle.invokeExact(englishMap, inS);
				}
			}
		} catch (Throwable e) {
			Throwables.throwIfUnchecked(e);
			throw new RuntimeException(e);
		}
	}
	
	public void init2(Map<String, Material> materials) {
		this.addCustomCategoryComment("materials", "Materials will only appear here when their respective modules are loaded.");
		
		for (final Iterator<Material> it = materials.values().iterator(); it.hasNext(); ) {
			final Material mat = it.next();
			final String matName = englishMap.translateKey(String.format(Material.LOC_Name, mat.getIdentifier()));
			if (!this.getBoolean(String.format("Enable material %s", mat.getIdentifier()),
					"materials", true, String.format("Set to false to prevent %s from being loaded", matName))) {
				it.remove(); // delete the disabled material
			}
		}
	}
	
	public void update() {
		if (hasChanged()) save();
	}
	
	public static boolean isInEndlectricBlacklist(ItemStack is) {
		if (is == null) return true;
		if (endlectricBlacklist.containsKey(is.getItem())) {
			List<ItemStack> lst = endlectricBlacklist.get(is.getItem());
			for (ItemStack is1: lst) {
				if (is1 == null || is.getMetadata() == is1.getMetadata()) return true;
			}
		}
		return false;
	}
}
