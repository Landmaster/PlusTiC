package landmaster.plustic.traits;

import javax.annotation.*;

import landmaster.plustic.api.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.potion.*;
import slimeknights.tconstruct.library.traits.*;

public class MysticalFire extends AbstractTrait {
	public static final MysticalFire mystical_fire = new MysticalFire();
	
	public static final MFPotion POTION = new MFPotion();
	
	public MysticalFire() {
		super("mystical_fire", 0x681302);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void registerPotion(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(POTION);
	}
	
	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		target.setFire(20);
		POTION.apply(target, 20*20);
	}
	
	private static class MFPotion extends TinkerPotion {
		MFPotion() {
			super(new ResourceLocation(ModInfo.MODID, "mystical_fire_potion"), true, false, 0x681302);
		}
		
		@Override
	    public boolean isReady(int duration, int strength) {
			return duration % 5 == 0;
		}
		
		@Override
	    public void performEffect(@Nonnull EntityLivingBase entity, int id) {
			entity.attackEntityFrom(DamageSource.MAGIC, 1.0f);
		}
	}
}
