package landmaster.plustic.tools;

import java.awt.Color;
import java.util.*;

import javax.annotation.*;

import landmaster.plustic.config.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.relauncher.*;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.tinkering.*;
import slimeknights.tconstruct.library.tools.*;
import slimeknights.tconstruct.library.utils.*;
import slimeknights.tconstruct.tools.*;
import slimeknights.tconstruct.tools.ranged.item.*;

public class ToolKatana extends SwordCore {
	public static final float DURABILITY_MODIFIER = 0.88f;
	
	public static final String COUNTER_TAG = "PlusTiC_Counter";
	
	static {
		MinecraftForge.EVENT_BUS.register(ToolKatana.class);
	}
	
	private static float counter_multiplier(float attack) {
		if (Config.katana_smooth_progression) {
			return MathHelper.clamp(1.2f + 0.025f * attack, 1.2f, 1.8f);
		}
		if (attack <= 5) {
			return 1.2f;
		}
		if (attack <= 11) {
			return 1.35f;
		}
		return 1.5f;
	}
	
	public static float counter_cap(ItemStack tool) {
		float attack = TagUtil.getToolStats(tool).attack;
		return attack * counter_multiplier(attack);
	}
	
	public ToolKatana() {
		super(PartMaterialType.handle(TinkerTools.toughToolRod),
				PartMaterialType.head(TinkerTools.largeSwordBlade),
				PartMaterialType.head(TinkerTools.largeSwordBlade),
				PartMaterialType.extra(TinkerTools.toughBinding));
		
		this.addCategory(Category.WEAPON);
		
		setTranslationKey("katana").setRegistryName("katana");
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent event) {
		final Minecraft mc = Minecraft.getMinecraft();
		final ItemStack is = mc.player.getHeldItemMainhand();
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT
				&& is != null && is.getItem() instanceof ToolKatana) {
			float counter = TagUtil.getTagSafe(is).getFloat(COUNTER_TAG);
			if (counter > 0) {
				mc.fontRenderer.drawString(I18n.format("meter.plustic.katana", Math.round(counter * 10f) / 10f),
						5, 5, Color.HSBtoRGB(Math.min(counter/(counter_cap(is)*3), 1.0f/3), 1, 1) & 0xFFFFFF, true);
			}
		}
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		return this._rightClick(playerIn.getHeldItem(hand), worldIn, playerIn, hand);
	}
	
	@Nonnull
	private ActionResult<ItemStack> _rightClick(@Nonnull ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND && playerIn.getHeldItemOffhand() != null && !(playerIn.getHeldItemOffhand().getItem() instanceof Shuriken)) {
			return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
	}
	
	@Override
	public int[] getRepairParts() {
		return new int[] {1,2};
	}
	
	@Override
	public double attackSpeed() {
		return 2.55;
	}

	@Override
	public float damagePotential() {
		return 0.77f;
	}
	
	@Override
	public float damageCutoff() {
		return 22.0f;
	}
	
	@Override
	public float knockback() {
		return 0.83f;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!worldIn.isRemote) {
			NBTTagCompound tag = TagUtil.getTagSafe(stack);
			float counter = tag.getFloat(COUNTER_TAG);
			counter -= 0.005f;
			counter = MathHelper.clamp(counter, 0, counter_cap(stack));
			tag.setFloat(COUNTER_TAG, counter);
			stack.setTagCompound(tag);
		}
	}
	
	@Override
	public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase targetLiving = (EntityLivingBase)entity;
			if (targetLiving.getTotalArmorValue() <= 0) {
				damage += 2.6f; // increase damage against unarmored
			}
		}
		NBTTagCompound tag = TagUtil.getTagSafe(stack);
		float counter = tag.getFloat(COUNTER_TAG);
		damage += counter * Config.katana_combo_multiplier;
		
		boolean success = super.dealDamage(stack, player, entity, damage);
		if (success) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase targetLiving = (EntityLivingBase)entity;
				if (targetLiving.getHealth() <= 0 || !Config.katana_boosts_only_on_killing) counter += 1.0f;
				counter = MathHelper.clamp(counter, 0, counter_cap(stack));
			}
			tag.setFloat(COUNTER_TAG, counter);
			stack.setTagCompound(tag);
		}
		return success;
	}
	
	@Override
	public float getRepairModifierForPart(int index) {
	    return DURABILITY_MODIFIER;
	}
	
	@Override
	protected ToolNBT buildTagData(List<Material> materials) {
		HandleMaterialStats handle = materials.get(0).getStatsOrUnknown(MaterialTypes.HANDLE);
	    HeadMaterialStats head0 = materials.get(1).getStatsOrUnknown(MaterialTypes.HEAD);
	    HeadMaterialStats head1 = materials.get(2).getStatsOrUnknown(MaterialTypes.HEAD);
	    ExtraMaterialStats binding = materials.get(3).getStatsOrUnknown(MaterialTypes.EXTRA);
	    
	    ToolNBT data = new ToolNBT();
	    data.head(head0, head1);
	    data.extra(binding);
	    data.handle(handle);
	    
		data.attack += 1f;
		data.durability *= DURABILITY_MODIFIER;
		return data;
	}
}
