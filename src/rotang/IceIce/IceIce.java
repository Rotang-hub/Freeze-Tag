package rotang.IceIce;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import rotang.IceIce.manager.Events;
import rotang.IceIce.manager.Manager;

public class IceIce extends JavaPlugin
{
	private Manager manager;
	
	@Override
	public void onEnable()
	{
		if(manager == null)
			manager = new Manager();
		
		getServer().getPluginManager().registerEvents(new Events(manager), this);
		getCommand("ice").setTabCompleter(new CommandTab());
		
		manager.timer.setTime(0, 10, 0);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if(label.equalsIgnoreCase("ice"))
		{
			if(args[0].equalsIgnoreCase("settime"))
			{
				int hour = Integer.parseInt(args[1]);
				int minute = Integer.parseInt(args[2]);
				int second = Integer.parseInt(args[3]);
				
				sender.sendMessage(ChatColor.GREEN + "[Ice] 제한시간을 " + ChatColor.YELLOW 
						+ hour + "시간 " + minute + "분 " + second + "초" + ChatColor.GREEN + "로 설정했습니다.");
				
				manager.timer.setTime(hour, minute, second);
			}
			
			if(args[0].equalsIgnoreCase("start"))
			{
				manager.start();
				sender.sendMessage(ChatColor.GREEN + "[Ice] 게임을 시작합니다.");
			}
			
			if(args[0].equalsIgnoreCase("stop"))
			{
				manager.stop();
				sender.sendMessage(ChatColor.GREEN + "[Ice] 게임을 종료합니다.");
			}
			
			if(args[0].equalsIgnoreCase("tagger"))
			{
				int length = args.length;
				
				if(length == 1)	//Not Put Player
				{
					String taggerList = "";
					
					for(Player tagger : manager.getTaggers())
					{
						taggerList = taggerList + tagger.getName() + ", ";
					}
					
					sender.sendMessage(ChatColor.GREEN + "[Ice] 술래: " + ChatColor.YELLOW + taggerList);
					return false;
				}
				
				sender.sendMessage(ChatColor.GREEN + "[Ice] 다음 플레이어들을 술래 목록에 추가했습니다.");
				for(int i = 1; i < length; i++)	//Put Player
				{
					Player tagger = getServer().getPlayer(args[i]);
					
					if(manager.getRunners().contains(tagger))
					{
						manager.removeRunner(tagger);
					}
					manager.addTagger(tagger);
					sender.sendMessage(ChatColor.YELLOW + tagger.getName());
				}
			}
			
			if(args[0].equalsIgnoreCase("runner"))
			{
				int length = args.length;
				
				if(length == 1)	//Not Put Player
				{
					String runnerList = "";
					
					for(Player runner : manager.getRunners())
					{
						runnerList = runnerList + runner.getName() + ", ";
					}
					
					sender.sendMessage(ChatColor.GREEN + "[Ice] 러너: " + ChatColor.YELLOW + runnerList);
					return false;
				}
				
				sender.sendMessage(ChatColor.GREEN + "[Ice] 다음 플레이어들을 러너 목록에 추가했습니다.");
				for(int i = 1; i < length; i++)	//Put Player
				{
					Player runner = getServer().getPlayer(args[i]);
					
					if(manager.getTaggers().contains(runner))
					{
						manager.removeTagger(runner);
					}
					manager.addRunner(runner);
					sender.sendMessage(ChatColor.YELLOW + runner.getName());
				}
			}
			
			if(args[0].equalsIgnoreCase("help"))
			{
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice tagger: 현재 술래인 플레이어들을 확인합니다.");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice tagger [player] [player]...: 플레이어들을 술래로 설정합니다.");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice runner: 현재 러너인 플레이어들을 확인합니다.");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice runner [player] [player]...: ");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice settime: 플레이어들을 러너로 설정합니다");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice start: 게임을 시작합니다.");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice stop: 게임을 종료합니다.");
			}
		}
		
		return true;
	}
}
