package landmaster.plustic.traits;

import net.minecraft.item.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.entity.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class Naphtha extends AbstractTrait {
	public static final Naphtha naphtha = new Naphtha();
	
	public Naphtha() {
		super("naphtha", 0xFF3314);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void lightUp(LivingHurtEvent event) {
		if (!event.getEntity().getEntityWorld().isRemote && event.getSource().getImmediateSource() instanceof EntityProjectileBase) {
			final ItemStack bow = ((EntityProjectileBase)event.getSource().getImmediateSource()).tinkerProjectile.getLaunchingStack();
			if (TinkerUtil.hasTrait(TagUtil.getTagSafe(bow), identifier)) {
				event.getEntity().setFire(10);
			}
		}
	}
}
