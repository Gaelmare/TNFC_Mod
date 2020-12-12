package tnfcmod.qfc.features;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import tnfcmod.tnfcmod;
import tnfcmod.util.ConfigTNFCMod;
import vazkii.arl.item.ItemMod;
import tnfcmod.qfc.item.ItemQfcFood;
import tnfcmod.qfc.base.LibEntityIDs;
import tnfcmod.qfc.util.LibPotionIndices;
import tnfcmod.qfc.module.Feature;
import tnfcmod.qfc.potion.PotionMod;
import tnfcmod.qfc.potion.ExtraPotions;
import tnfcmod.qfc.render.RenderCrab;
import tnfcmod.qfc.entity.EntityCrab;
import tnfcmod.qfc.item.ItemCrabShell;
import tnfcmod.qfc.event.RaveEventListener;

public class Crabs extends Feature {

	public static int weight, min, max;

	public static ItemQfcFood crabLeg;
	public static ItemQfcFood cookedCrabLeg;
	public static ItemMod crabShell;

	public static PotionMod resilience;

	@Override
	public void setupConfig() {
        weight = ConfigTNFCMod.GENERAL.crabweight;
		min = 1;
		max = 4;
	}

	@SubscribeEvent
	public void onNewWorld(WorldEvent.Load event) {
		event.getWorld().addEventListener(new RaveEventListener(event.getWorld()));
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		crabLeg = new ItemQfcFood("crab_leg", 1, 0.3F, true);
		cookedCrabLeg = new ItemQfcFood("cooked_crab_leg", 8, 0.8F, true);
		crabShell = new ItemCrabShell();

		resilience = new PotionMod("resilience", false, 0x5b1a04, LibPotionIndices.RESILIENCE);
		resilience.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "0cb68666-1b17-42b3-82c0-28412d6c0b22", 0.5, 0);

		String crabName = "tnfcmod:crab";
		LootTableList.register(EntityCrab.CRAB_LOOT_TABLE);
		EntityRegistry.registerModEntity(new ResourceLocation(crabName), EntityCrab.class, crabName, LibEntityIDs.CRAB, tnfcmod.instance, 80, 3, true, 0x893c22, 0x916548);
	}

	@Override
	public void postPreInit() {
		ExtraPotions.addStandardBlend(resilience, crabShell);
	}

	@Override
	public void init() {
		EntityRegistry.addSpawn(EntityCrab.class, ConfigTNFCMod.GENERAL.crabfreq, min, max, EnumCreatureType.CREATURE, Biomes.BEACH);
	}

	@Override
	public void postInit() {
		FurnaceRecipes.instance().addSmelting(crabLeg, new ItemStack(cookedCrabLeg), 0.35F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient() {
		RenderingRegistry.registerEntityRenderingHandler(EntityCrab.class, RenderCrab.FACTORY);
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
