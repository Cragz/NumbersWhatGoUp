package io.github.cragz.numberswhatgoup;

import io.github.cragz.numberswhatgoup.enums.EntityClass;
import io.github.cragz.numberswhatgoup.enums.MobClass;
import io.github.cragz.numberswhatgoup.enums.WeaponClass;
import io.github.cragz.numberswhatgoup.logging.LoggerFactory;
import io.github.cragz.runeplugin.LocationSerializer;
import io.github.cragz.runeplugin.RuneUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils
{
	public static void switchItemInHand(Player player, ItemStack item)
	{
		ItemStack inHand = player.getItemInHand();
		
		if (inHand != null && inHand.getAmount() > 0)
		{
			inHand.setAmount(inHand.getAmount() - 1);
			player.getInventory().clear(player.getInventory().getHeldItemSlot());
		}
		
		player.getInventory().setItemInHand(item);
		
		if (inHand.getAmount() > 0)
		{
			HashMap<Integer, ItemStack> unableToAdd = player.getInventory().addItem(inHand);
			
			if (unableToAdd.size() > 0)
			{
				for (ItemStack dropItem : unableToAdd.values())
				{
					player.getWorld().dropItemNaturally(player.getLocation(), dropItem);
				}
			}
		}
	}
	
    public static Location getHandLocation(Player player)
    {
        Location loc = player.getLocation().clone();
        
        double a = loc.getYaw() / 180D * Math.PI + Math.PI / 2;
        double l = Math.sqrt(0.8D * 0.8D + 0.4D * 0.4D);
        
        loc.setX(loc.getX() + l * Math.cos(a) - 0.4D * Math.sin(a));
        loc.setY(loc.getY() + player.getEyeHeight() - 0.2D);
        loc.setZ(loc.getZ() + l * Math.sin(a) + 0.4D * Math.cos(a));
        
        return loc;
    }
    
	public static void setItemLore(ItemStack item, String lore)
	{
		setItemLore(item, lore, ChatColor.RESET, false);
	}

	public static void setItemLore(ItemStack item, String lore, ChatColor color, Boolean disableItalics)
	{
		lore = String.format("%s%s%s", (disableItalics ? ChatColor.ITALIC.toString() : ""), color.toString(), lore);
		
		ItemMeta metaClone = item.getItemMeta();
		
		ArrayList<String> loreArray = new ArrayList<String>();
		loreArray.add(lore);
			
		metaClone.setLore(loreArray);
		item.setItemMeta(metaClone);
	}
	
	public static void clearItemLore(ItemStack item)
	{
		ItemMeta metaClone = item.getItemMeta();
		metaClone.setLore(null);
		item.setItemMeta(metaClone);
	}
	
	public static void setItemName(ItemStack item, String name)
	{
		setItemName(item, name, ChatColor.RESET, false);
	}
	
	public static void setItemName(ItemStack item, String name, ChatColor color, Boolean disableItalics)
	{
		name = String.format("%s%s%s", (disableItalics ? ChatColor.ITALIC.toString() : ""), color.toString(), name);
		
		ItemMeta metaClone = item.getItemMeta();
		metaClone.setDisplayName(name);
		item.setItemMeta(metaClone);
	}

	public static void clearItemName(ItemStack item)
	{
		ItemMeta metaClone = item.getItemMeta();
		metaClone.setDisplayName("");
		item.setItemMeta(metaClone);
	}
	
	public static Boolean isUnderground(Entity entity)
	{
		// Todo: This needs fleshing out to actually check if it's underground!
		
		return entity.getLocation().getY() <= 60;
	}
	
	public static Double randDouble(Double min, Double max)
	{
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		return round(min + (max - min) * r.nextDouble(), 1);
	}
	
	public static Double round(Double value, Integer places)
	{
	    return round(value, places, RoundingMode.HALF_UP);
	}
	
	public static Double round(Double value, Integer places, RoundingMode roundMode)
	{
	    if (places < 0)
	    	throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, roundMode);
	    
	    return bd.doubleValue();
	}
	
	public static Boolean holdingSword(Player player)
	{
		ItemStack inHand = player.getItemInHand();
		
		if (inHand != null)
			return isSword(inHand.getType());
		
		return false;
	}
	
	public static Boolean isSword(Material material)
	{
		switch (material)
		{
			case WOOD_SWORD:
			case STONE_SWORD:
			case IRON_SWORD:
			case GOLD_SWORD:
			case DIAMOND_SWORD:
				return true;
			default:
				return false;
		}
	}
	
	// Todo: This is horrific. Need a wholesale rework of the "get class" setup.
	
	public static EntityClass getMaterialClass(Material material)
	{
		switch (material)
		{
			case GRAVEL:
				return EntityClass.GARBAGE_BLOCK;
				
			case COBBLESTONE:
			case STONE:
			case SANDSTONE:
				return EntityClass.STANDARD_BLOCK;
			
			case COAL_ORE:
			case DIAMOND_ORE:
			case EMERALD_ORE:
			case GLOWING_REDSTONE_ORE:
			case GOLD_ORE:
			case IRON_ORE:
			case LAPIS_ORE:
			case QUARTZ_ORE:
			case REDSTONE_ORE:
				 return EntityClass.SPECIAL_BLOCK;
				 
			 default:
				 return EntityClass.NONE;
		}
	}
	
	public static EntityClass getEntityClass(Entity entity)
	{
		switch (entity.getType())
		{
			case DROPPED_ITEM:
			{
				if (entity instanceof Item)
				{
					switch (((Item)entity).getItemStack().getType())
					{
						case RAW_FISH:
							return EntityClass.CAUGHT_FISH;
						
						default:
							return EntityClass.CAUGHT_ITEM;
					}
				}
				
				return EntityClass.NONE;
			}
			
			case BAT:
			case CHICKEN:
			case COW:
			case HORSE:
			case MUSHROOM_COW:
			case OCELOT:
			case PIG:
			case SHEEP:
			case SQUID:
			case VILLAGER:	
				return EntityClass.MOB_PASSIVE;		
	
			case CAVE_SPIDER:
			case ENDERMAN:
			case SPIDER:
			case WOLF:
			case PIG_ZOMBIE:
				return EntityClass.MOB_NEUTRAL;
				
			case BLAZE:
			case CREEPER:
			case GHAST:
			case GIANT:
			case IRON_GOLEM:
			case MAGMA_CUBE:
			case SILVERFISH:
			case SKELETON:
			case SLIME:
			case SNOWMAN:
			case WITCH:
			case ZOMBIE:
				return EntityClass.MOB_HOSTILE;
				
			case WITHER:
			case ENDER_CRYSTAL:
			case ENDER_DRAGON:
				return EntityClass.MOB_BOSS;
			
			default:
				return EntityClass.NONE;
		}
	}
	
	public static WeaponClass getWeaponClass(ItemStack item)
	{
		switch (item.getType())
		{
			case WOOD_SWORD:
			case STONE_SWORD:
			case IRON_SWORD:
			case GOLD_SWORD:
			case DIAMOND_SWORD:
				return WeaponClass.SWORD;
				
			case WOOD_AXE:
			case STONE_AXE:
			case IRON_AXE:
			case GOLD_AXE:
			case DIAMOND_AXE:
				return WeaponClass.AXE;
				
			case WOOD_PICKAXE:
			case STONE_PICKAXE:
			case IRON_PICKAXE:
			case GOLD_PICKAXE:
			case DIAMOND_PICKAXE:
				return WeaponClass.PICKAXE;
				
			case BOW:
			case ARROW:
				return WeaponClass.BOW;
				
			default:
				return WeaponClass.UNARMED;
		}
	}
	
	public static MobClass getMobClass(Entity entity)
	{
		switch(entity.getType())
		{
			case BAT:
			case CHICKEN:
			case COW:
			case HORSE:
			case MUSHROOM_COW:
			case OCELOT:
			case PIG:
			case SHEEP:
			case SQUID:
			case VILLAGER:	
				return MobClass.PASSIVE;		

			case CAVE_SPIDER:
			case ENDERMAN:
			case SPIDER:
			case WOLF:
			case PIG_ZOMBIE:
				return MobClass.NEUTRAL;
				
			case BLAZE:
			case CREEPER:
			case GHAST:
			case GIANT:
			case IRON_GOLEM:
			case MAGMA_CUBE:
			case SILVERFISH:
			case SKELETON:
			case SLIME:
			case SNOWMAN:
			case WITCH:
			case ZOMBIE:
				return MobClass.HOSTILE;
				
			case WITHER:
			case ENDER_CRYSTAL:
			case ENDER_DRAGON:
				return MobClass.BOSS;
			
			default:
				return MobClass.NONE;
		}
	}
	
	// Todo: This isn't particularly efficient
	
	public static String getNameFromObject(Object type)
	{
		StringBuilder rebuilder = new StringBuilder();
		
		for (String part : type.toString().toLowerCase().split("_"))
		{
			rebuilder.append(Character.toUpperCase(part.charAt(0)) + part.substring(1));
			rebuilder.append(' ');
		}
		
		return rebuilder.toString().substring(0, rebuilder.length() - 1);
	}
	
	public static String getNameFromMaterial(Material material)
	{
		return getNameFromObject(material);
	}

	public static String getNameFromEntityType(EntityType type)
	{
		return getNameFromObject(type);
	}
	
	public static String getCreatureFriendlyName(EntityType type)
	{
		switch (type)
		{
			case MUSHROOM_COW:	return "Mooshroom";
			case PIG_ZOMBIE:	return "Zombie Pigman";
			default:			return getNameFromEntityType(type);
		}
	}
}
