package landmaster.plustic.config;

import java.io.*;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.tuple.*;

import com.google.common.base.Throwables;

import it.unimi.dsi.fastutil.ints.*;
import landmaster.plustic.traits.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.*;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.oredict.OreDictionary;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.smeltery.*;

public class Config extends Configuration {
	// MODULES
	public static boolean base;
	public static boolean bop;
	public static boolean projectRed;
	public static boolean mekanism;
	public static boolean botania;
	public static boolean advancedRocketry;
	public static boolean armorPlus;
	public static boolean thermalFoundation;
	public static boolean draconicEvolution;
	public static boolean actuallyAdditions;
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
	public static boolean thaumcraft;
	public static boolean constructsArmory;
	public static boolean machines;
	public static boolean astralSorcery;
	public static boolean aoa3;
	
	public static boolean jetpackConarmModifier;
	public static float jetpackDurabilityBonusScale;
	
	// alas…
	public static boolean forceOutNaturalPledgeMaterials;
	
	public static boolean pyrotheumSmelt;
	public static boolean tfMelt;
	
	public static boolean katana;
	public static boolean laserGun;
	
	public static float katana_combo_multiplier;
	public static boolean katana_boosts_only_on_killing;
	public static boolean katana_smooth_progression;
	
	public static int laser_energy;
	
	public static int centrifugeEnergyPerMB;
	
	private static final IntArrayList botan_amount = new IntArrayList(Botanical.MAX_LEVELS);
	
	private static final Set<Pair<String, Set<String>>> blacklistedForCentrifuge = new HashSet<>();

    public static IntList getBotanAmount() {
		return IntLists.unmodifiable(botan_amount);
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
	
	public static List<ItemStack> fruitStacks = new ArrayList<>();
	public static IntSet fruitOreDicts;
	public static boolean isFruit(ItemStack stack) {
		for (int id: OreDictionary.getOreIDs(stack)) {
			if (fruitOreDicts.contains(id)) {
				return true;
			}
		}
		for (ItemStack fruit: fruitStacks) {
			if (OreDictionary.itemMatches(fruit, stack, false)) {
				return true;
			}
		}
		return false;
	}
	
	public Config(FMLPreInitializationEvent event) {
		super(event.getSuggestedConfigurationFile());
	}
	
	public void init1() {
		this.addCustomCategoryComment("modules", "Use this to disable entire modules.");
		
		// MODULES
		base = getBoolean("Enable vanilla TiC addons", "modules", true, "Add features to vanilla Tinkers Construct");
		bop = getBoolean("Enable BoP integration", "modules", true, "Integrate with Biomes o' Plenty");
		projectRed = getBoolean("Enable Project Red integration", "modules", true, "Integrate with Project Red-Core");
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
		thermalFoundation = getBoolean("Enable Thermal Foundation integration", "modules", true, "Integrate with Thermal Foundation");
		{
			pyrotheumSmelt = getBoolean("Use Pyrotheum as smeltery fuel", "tweaks", true, "Use Pyrotheum as TiC smeltery fuel (only if Thermal Foundation is loaded)");
			tfMelt = getBoolean("Add smeltery recipes for Redstone, Glowstone, and Ender pearl", "tweaks", true, "Add smelting recipes for Redstone, Glowstone, and Ender pearl (only if Thermal Foundation is loaded)");
		}
		draconicEvolution = getBoolean("Enable Draconic Evolution integration", "modules", true, "Integrate with Draconic Evolution");
		actuallyAdditions = getBoolean("Enable Actually Additions support", "modules", true, "Integrate with Actually Additions");
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
		thaumcraft = getBoolean("Enable Thaumcraft support", "modules", true, "Integrate with Thaumcraft");
		constructsArmory = getBoolean("Enable Constructs Armory support", "modules", true, "Integrate with Constructs Armory");
		machines = getBoolean("Enable Machines addon", "modules", true, "Enable the machines from this mod (Centrifuge, etc.)");
		astralSorcery = getBoolean("Enable Astral Sorcery support", "modules", true, "Integrate with Astral Sorcery");
		aoa3 = getBoolean("Enable Advent of Ascension support", "modules", true, "Integrate with Advent of Ascension");
		
		jetpackConarmModifier = getBoolean("Add Simply Jetpacks as ConArm modifiers", "modifiers", true, "Add Simply Jetpacks as ConArm modifiers");
		jetpackDurabilityBonusScale = getFloat("Durability bonus scalar for Simply Jetpacks modifiers", "modifiers", 1f/8000, 0, Float.MAX_VALUE, "Durability bonus calculated as FUEL_CAPACITY_OF_JETPACK*this_scalar");
		
		// TOOLS
		katana = getBoolean("Enable Katana", "tools", true, "Enable Katana");
		katana_combo_multiplier = getFloat("Katana combo multiplier", "tools", 1.25f, 0, Float.MAX_VALUE, "Multiply combo value by this to calculate bonus damage");
		katana_boosts_only_on_killing = getBoolean("Katana boosts only on killing", "tools", true, "Does Katana boost only on killing mob (true) or on any hit (false)?");
		katana_smooth_progression = getBoolean("Smooth Katana progression", "tools", false, "Should boosted damage of Katana change smoothly from material to material?");
		
		laserGun = getBoolean("Enable Laser Gun", "tools", true, "Enable Laser Gun");
		laser_energy = getInt("Laser Gun Energy consumed", "tools", 100, 0, Integer.MAX_VALUE, "How much energy is used, by default, per laser attack");
		
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
		
		// Fruit salad
		String[] fruitStacksArr = this.getStringList("Fruits stack list", "tweaks",
				new String[] {"apple", "golden_apple;"+OreDictionary.WILDCARD_VALUE, "melon", "chorus_fruit"}, "Enter in the format \"modid:name;meta\" (leave meta blank for zero metadata)");
		{
			int meta = 0;
			for (int i=0; i<fruitStacksArr.length; ++i) {
				String[] loc_meta = fruitStacksArr[i].split(";");
				if (loc_meta.length > 1) {
					try {
						meta = Integer.parseInt(loc_meta[1]);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				Item it = Item.REGISTRY.getObject(new ResourceLocation(loc_meta[0]));
				if (it != null) {
					fruitStacks.add(new ItemStack(it, 1, meta));
				}
			}
		}
		fruitOreDicts = Arrays.stream(this.getStringList("Fruits oredict list", "tweaks",
				new String[] { "cropApple", "listAllfruit" }, "Valid ore dictionary values for Fruit Salad"))
				.mapToInt(OreDictionary::getOreID)
				.collect(IntOpenHashSet::new, IntOpenHashSet::add, IntOpenHashSet::addAll);
		
		// Modifier values for Botanical
		Property botan_amount_prop = this.get("tweaks", "Modifier values for Botanical", new int[0]);
		botan_amount_prop.setLanguageKey("Modifiers added for Botanical modifier");
		botan_amount_prop.setComment("Enter integer amounts, specifying the amount of modifiers added to the tool for each level, in increasing order of level (defaults will be extrapolated if some left blank)");
		botan_amount_prop.setMinValue(0);
		
		botan_amount.addElements(botan_amount.size(), botan_amount_prop.getIntList());
		if (botan_amount.isEmpty()) botan_amount.add(1);
		while (botan_amount.size() < Botanical.MAX_LEVELS) {
			botan_amount.add(botan_amount.getInt(botan_amount.size()-1)<<1);
		}
		
		// Centrifuge
		for (String blacklistEntry: this.getStringList("Centrifuge blacklist", "tweaks", new String[0], "Enter in the format inputFluid:outputFluid1;outputFluid2;outputFluid3")) {
			String[] separateInOut = blacklistEntry.split(":");
			blacklistedForCentrifuge.add(Pair.of(separateInOut[0], new HashSet<>(Arrays.asList(separateInOut[1].split(";")))));
		}
		centrifugeEnergyPerMB = this.getInt("Centrifuge energy per mB", "tweaks", 5, 0, Integer.MAX_VALUE, "Energy consumed by centrifuge per millibucket");
		
		for (String traitLoadEntry: this.getStringList("Force load traits", "tweaks", new String[0], "Force-load these traits (as a fully-qualified class name; e.g. landmaster.plustic.traits.Global) without the required mods themselves being loaded")) {
			try {
				Class.forName(traitLoadEntry);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
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

	public static boolean isCentrifugeRecipeValid(AlloyRecipe recipe) {
		Pair<String, Set<String>> pairToCheck = Pair.of(
				FluidRegistry.getFluidName(recipe.getResult()),
				recipe.getFluids().stream()
				.map(FluidRegistry::getFluidName)
				.collect(Collectors.toSet()));
		return !blacklistedForCentrifuge.contains(pairToCheck);
	}
}
