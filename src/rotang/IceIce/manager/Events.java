package rotang.IceIce.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class Events implements Listener
{
	Manager manager;
	
	public Events(Manager manager) 
	{
		this.manager = manager;
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event)
	{
		ItemStack item = event.getItemInHand();
		
		if(item.equals(manager.getIceItem()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		Action action = event.getAction();
		
		if(!manager.process)
		{
			return;
		}
		
		if(item.equals(manager.getIceItem()))
		{
			if(event.getHand().equals(EquipmentSlot.HAND))
			{
				if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))
				{
					if(manager.getRunners().contains(player))
					{
						Runner runner = manager.runnerMap.get(player);
						
						runner.freezeRunner();
						
						if(manager.isEveryRunnerOut())
						{
							manager.stop();
						}
					}
					else
					{
						player.sendMessage(ChatColor.RED + "아이템을 사용할 수 없습니다.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerHitPlayer(EntityDamageByEntityEvent event)
	{
		if(!manager.process)
			return;
			
		if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player))
		{
			return;
		}
		
		Player damager = (Player) event.getDamager();
		Player hitter = (Player) event.getEntity();
		
		if(manager.getTaggers().contains(damager) && manager.getRunners().contains(hitter))	//tagger hit runner
		{
			Runner runner = manager.runnerMap.get(hitter);
			
			if(runner.isFreeze)
				event.setCancelled(true);
			
			else if(!runner.isFreeze)
			{
				runner.out();
				
				if(manager.isEveryRunnerOut())
				{
					manager.stop();
				}
			}
		}
		
		if(manager.getRunners().contains(damager) && manager.getRunners().contains(hitter)) //runner hit runner
		{
			manager.runnerMap.get(hitter).setFreeze(false);
			
		}
		
		if(manager.getRunners().contains(damager) && manager.getTaggers().contains(hitter))	//runner hit tagger
		{
			if(damager.getInventory().getItemInMainHand().equals(manager.getIceItem()))
			{
				manager.taggerMap.get(hitter).freezeTagger();
			}
		}
	}
}