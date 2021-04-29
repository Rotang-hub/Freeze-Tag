package rotang.IceIce.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import rotang.IceIce.IceIce;

public class Manager 
{
	private Plugin plugin = IceIce.getPlugin(IceIce.class);
	
	public Timer timer = new Timer(this);
	
	public boolean process = false;
	
	private List<Player> taggers = new ArrayList<>();
	
	private List<Player> runners = new ArrayList<>();
	
	public Map<Player, Tagger> taggerMap = new HashMap<>();
	
	public Map<Player, Runner> runnerMap = new HashMap<>();
	
	public ItemStack getIceItem()
	{
		ItemStack item = new ItemStack(Material.BLUE_ICE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "얼음");
		meta.setLore(Arrays.asList("-우클릭 시 [얼음] 상태가 됩니다.", "-아이템을 들고 [얼음] 상태인 상대를 때릴 시 ", "상대의 [얼음] 상태를 해제합니다", 
				"-아이템을 들고 술래를 때릴 시 ", "술래를 잠시 [얼음] 상태로 만듭니다."));
		item.setItemMeta(meta);
		
		return item;
	}

	//========================
	
	public void setTaggerMap()
	{
		for(Player tagger : taggers)
		{
			taggerMap.put(tagger, new Tagger(tagger));
		}
	}
	
	public void resetTaggerMap()
	{
		taggerMap.clear();
	}
	
	public List<Player> getTaggers() 
	{
		return taggers;
	}

	public void addTagger(Player tagger) 
	{
		taggers.add(tagger);
	}
	
	public void removeTagger(Player tagger)
	{
		taggers.remove(tagger);
	}
	
	public void resetTaggers()
	{
		taggers.clear();
	}
	
	//=========================
	
	public void setRunnerMap()
	{
		for(Player runner : runners)
		{
			runnerMap.put(runner, new Runner(runner));
		}
	}
	
	public void resetRunnerMap()
	{
		runnerMap.clear();
	}
	
	public List<Player> getRunners()
	{
		return runners;
	}
	
	public void addRunner(Player runner)
	{
		runners.add(runner);
	}
	
	public void removeRunner(Player runner)
	{
		runners.remove(runner);
	}
	
	public void resetRunners()
	{
		runners.clear();
	}
	
	//==========================
	
	public void start()
	{
		process = true;
		timer.start();
		
		for(Player runner : runners)
		{
			runner.getInventory().addItem(getIceItem());
		}
		
		setRunnerMap();
		setTaggerMap();
	}
	
	public void stop() 
	{
		process = false;
		
		if(timer.process)
			timer.stop();
		
		new BukkitRunnable() 
		{
			@Override
			public void run()
			{
				if(isEveryRunnerOut())
				{
					if(!getRunners().isEmpty())
					{
						for(Player runner : getRunners())
						{
							runner.sendTitle(ChatColor.RED + "패배", "", 10, 80, 20);
							runner.playSound(runner.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
							runner.setGameMode(GameMode.SURVIVAL);
							runnerMap.get(runner).setFreeze(false);
							runner.getInventory().removeItem(getIceItem());
						}
					}
					
					if(!getTaggers().isEmpty())
					{	
						for(Player tagger : getTaggers())
						{
							tagger.sendTitle(ChatColor.AQUA + "승리", "", 10, 80, 20);
							tagger.playSound(tagger.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
						}
					}
				}
				
				else
				{
					if(!getRunners().isEmpty())
					{
						for(Player runner : getRunners())
						{
							runner.sendTitle(ChatColor.AQUA + "승리", "", 10, 80, 20);
							runner.playSound(runner.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
							runner.setGameMode(GameMode.SURVIVAL);
							runnerMap.get(runner).setFreeze(false);
							runner.getInventory().removeItem(getIceItem());
						}
					}
					
					if(!getTaggers().isEmpty())
					{
						for(Player tagger : getTaggers())
						{
							tagger.sendTitle(ChatColor.RED + "패배", "", 10, 80, 20);
							tagger.playSound(tagger.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
						}
					}
				}
				
				resetRunnerMap();
				resetRunners();
				resetTaggerMap();
				resetTaggers();
			}
		}.runTaskLater(plugin, 4);
	}
	
	public boolean isEveryRunnerOut()
	{
		for(Player player : getRunners())
		{
			Runner runner = runnerMap.get(player);
			
			if(!(runner.isFreeze || runner.isOut))
			{
				return false;
			}
		}	
		return true;
	}
}
