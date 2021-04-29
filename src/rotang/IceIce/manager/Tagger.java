package rotang.IceIce.manager;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import rotang.IceIce.IceIce;

public class Tagger 
{
	private Plugin plugin = IceIce.getPlugin(IceIce.class);
	
	private Player tagger;
	
	private int freezeTick = 20;	//2s
	
	private double speed;
	
	public Tagger(Player player) 
	{
		tagger = player;
		speed = player.getWalkSpeed();
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public void setSpeed(double speed)
	{
		this.speed = speed;
	}
	
	public void freezeTagger()
	{
		Location freezeLoc = tagger.getLocation();
		
		tagger.sendTitle(ChatColor.DARK_AQUA + "¾óÀ½!", "", 0, 60, 0);
		tagger.playSound(tagger.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, 1, 0.5f);
		
		new BukkitRunnable() 
		{
			int tick = 0;
			
			@Override
			public void run() 
			{
				if(tick == freezeTick)
				{
					tagger.sendTitle(ChatColor.DARK_AQUA + "¶¯!", "", 0, 60, 0);
					tagger.playSound(tagger.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 0);
					
					cancel();
				}
				
				Location curLoc = tagger.getLocation();
				Vector vec = freezeLoc.toVector().subtract(curLoc.toVector()).normalize();
					
				tagger.setVelocity(vec);
				tagger.getWorld().spawnParticle(Particle.END_ROD, curLoc.getX(), curLoc.getY() + 0.5, curLoc.getZ(), 10, 0.5, 0.8, 0.5, 0);
				
				tick++;
			}
		}.runTaskTimer(plugin, 0, 2);	//10tick/s
	}
}
