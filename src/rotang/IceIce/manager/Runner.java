package rotang.IceIce.manager;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import rotang.IceIce.IceIce;

public class Runner 
{
	private Plugin plugin = IceIce.getPlugin(IceIce.class);
	
	private Player runner;
	
	public Boolean isFreeze = false;
	
	public Boolean isOut = false;
	
	public Runner(Player player) 
	{
		runner = player;
	}
	
	public void setFreeze(boolean state)
	{
		isFreeze = state;
	}
	
	public void freezeRunner()
	{
		Location freezeLoc = runner.getLocation();
		
		setFreeze(true);
		
		runner.sendTitle(ChatColor.DARK_AQUA + "¾óÀ½!", "", 0, 60, 0);
		runner.playSound(runner.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, 1, 0.5f);
		
		new BukkitRunnable()
		{	
			@Override
			public void run()
			{
				if(isFreeze)
				{
					Location curLoc = runner.getLocation();
					Vector vec = freezeLoc.toVector().subtract(curLoc.toVector()).multiply(0.4);
						
					runner.setVelocity(vec);
					runner.getWorld().spawnParticle(Particle.END_ROD, curLoc.getX(), curLoc.getY() + 0.5, curLoc.getZ(), 5, 0.5, 0.8, 0.5, 0);
				}
				
				else
				{
					runner.sendTitle(ChatColor.DARK_AQUA + "¶¯!", "", 0, 60, 0);
					runner.playSound(runner.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0);
					
					cancel();
					return;
				}
			}
		}.runTaskTimer(plugin, 0, 2);
	}
	
	public void out()
	{
		runner.setGameMode(GameMode.SPECTATOR);
		isOut = true;
		
		runner.sendTitle(ChatColor.RED + "¾Æ¿ô!", "", 0, 60, 20);
		runner.playSound(runner.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 0);
	}
}
