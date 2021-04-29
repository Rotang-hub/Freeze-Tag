package rotang.IceIce.manager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import rotang.IceIce.IceIce;

public class Timer
{
	private Plugin plugin = IceIce.getPlugin(IceIce.class);
	
	private Manager manager;
	
	public boolean process = false;

	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	
	public Timer(Manager manager) 
	{
		this.manager = manager;
	}
	
	public void setTime(int h, int m, int s)
	{
		hour = h;
		minute = m;
		second = s;
	}
	
	public void start() 
	{
		process = true;
		
		new BukkitRunnable()
		{	
			int h = hour;
			int m = minute;
			int s = second;
			
			@Override
			public void run() 
			{
				if(process)
				{
					if(s == 0)	
					{
						if(m == 0)
						{
							if(h != 0)
								h--;
							m = 60;
						}
						
						m--;
						s = 60;
					}
					s--;
					
					for(Player player : plugin.getServer().getOnlinePlayers())
					{
						String curTime = ChatColor.GREEN + "남은 시간 [" + h + " : " + m + " : " + s + "]"; 
						
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(curTime));
					}
					
					if(h <= 0 && m <= 0 && s <= 0)
					{
						process = false;
					}
				}
				
				else if(!process)
				{
					cancel();
					stop();
				}
			}
		}.runTaskTimer(plugin , 0, 20);
	}

	public void stop()
	{
		process = false;
		
		if(manager.process)
			manager.stop();
	}
}
