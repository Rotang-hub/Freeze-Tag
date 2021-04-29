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
				
				sender.sendMessage(ChatColor.GREEN + "[Ice] ���ѽð��� " + ChatColor.YELLOW 
						+ hour + "�ð� " + minute + "�� " + second + "��" + ChatColor.GREEN + "�� �����߽��ϴ�.");
				
				manager.timer.setTime(hour, minute, second);
			}
			
			if(args[0].equalsIgnoreCase("start"))
			{
				manager.start();
				sender.sendMessage(ChatColor.GREEN + "[Ice] ������ �����մϴ�.");
			}
			
			if(args[0].equalsIgnoreCase("stop"))
			{
				manager.stop();
				sender.sendMessage(ChatColor.GREEN + "[Ice] ������ �����մϴ�.");
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
					
					sender.sendMessage(ChatColor.GREEN + "[Ice] ����: " + ChatColor.YELLOW + taggerList);
					return false;
				}
				
				sender.sendMessage(ChatColor.GREEN + "[Ice] ���� �÷��̾���� ���� ��Ͽ� �߰��߽��ϴ�.");
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
					
					sender.sendMessage(ChatColor.GREEN + "[Ice] ����: " + ChatColor.YELLOW + runnerList);
					return false;
				}
				
				sender.sendMessage(ChatColor.GREEN + "[Ice] ���� �÷��̾���� ���� ��Ͽ� �߰��߽��ϴ�.");
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
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice tagger: ���� ������ �÷��̾���� Ȯ���մϴ�.");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice tagger [player] [player]...: �÷��̾���� ������ �����մϴ�.");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice runner: ���� ������ �÷��̾���� Ȯ���մϴ�.");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice runner [player] [player]...: ");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice settime: �÷��̾���� ���ʷ� �����մϴ�");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice start: ������ �����մϴ�.");
				sender.sendMessage(ChatColor.GREEN + "[Ice] /ice stop: ������ �����մϴ�.");
			}
		}
		
		return true;
	}
}
