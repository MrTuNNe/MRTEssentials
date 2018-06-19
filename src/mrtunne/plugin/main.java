package mrtunne.plugin;
import java.util.ArrayList;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import mrtunne.mysql.MySQL;

public class main extends JavaPlugin implements Listener {
	public static main plugin;
	public MySQL mysql = new MySQL("", "", "", "");
    @Override
    public void onEnable() {
    	Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("[MRTEssentials] Has been enabled!");
        plugin = this;
    }
    @Override
    public void onDisable() {
        getLogger().info("[MRTEssentials] Has been disabled!");
        plugin = null;
    }
    @EventHandler
    public void OnPlayerConnect(PlayerJoinEvent e) {
    	Player p = e.getPlayer();
    	p.sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.WHITE + "Wizzard " + ChatColor.LIGHT_PURPLE + "Craft");
    	p.sendMessage(ChatColor.YELLOW + "If you have a problem please use /report and your problem.");
    	p.sendMessage(ChatColor.YELLOW + "You can try " + ChatColor.GREEN + "/elytrafall" + ChatColor.YELLOW + " for some fun!(Type " + ChatColor.GREEN + "/elytra" + ChatColor.YELLOW + " to get an elytra)");
    	
    }
    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!p.hasPlayedBefore()){
			World w = getServer().getWorld("roleplay");
			int x = getConfig().getInt("Spawn.X");
			int y = getConfig().getInt("Spawn.Y");
			int z = getConfig().getInt("Spawn.Z");
			Location t = new Location(w,x,y,z);
			p.teleport(t);
			p.getInventory().addItem(new ItemStack(Material.COOKED_CHICKEN, 64));
        }
    }
    
    public ArrayList<Player> mute = new ArrayList<Player>();
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
    	if(mute.contains(event.getPlayer())) {
    		event.setCancelled(true);
    		event.getPlayer().sendMessage(ChatColor.RED + "You are muted. You can't speak in the public chat.");
    	}
    }
    
    
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = (Player) sender;
		// Commands
		if(cmd.getName().equalsIgnoreCase("anno")) {
			if(player.hasPermission("mrtunne.ess.anno")) {
				String globalMessage = "";   
	
				for(int i = 0; i < args.length; i++){
				    String arg = args[i] + " ";
				    globalMessage = globalMessage + arg;
				}
				Bukkit.broadcastMessage(ChatColor.YELLOW + "ANNO: " + ChatColor.RED + globalMessage);
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("gmc")) {
			if(player.hasPermission("mrtunne.ess.gmc")) {
				player.sendMessage(ChatColor.RED + "GameMode " + ChatColor.YELLOW + "Creative" + ChatColor.RED + " has been activated.");
				player.setGameMode(GameMode.CREATIVE);
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("gms")) {
			if(player.hasPermission("mrtunne.ess.gms")) {
				player.sendMessage(ChatColor.RED + "GameMode " + ChatColor.YELLOW + "Survival" + ChatColor.RED + " has been activated.");
				player.setGameMode(GameMode.SURVIVAL);
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("day")) {
			if(player.hasPermission("mrtunne.ess.day")) {
				player.sendMessage(ChatColor.RED + "World time has been set to " + ChatColor.YELLOW + "1000" + ChatColor.RED + ".");
				for(World w : Bukkit.getServer().getWorlds()){
					w.setTime(1000L);
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("kick")) {
			if(player.hasPermission("mrtunne.ess.kick")) {
				if(args.length > 0) {
					Player playerkicked = Bukkit.getPlayerExact(args[0]);
					String kickMessage = args[1];
					playerkicked.kickPlayer(kickMessage);
					Bukkit.broadcastMessage(ChatColor.RED + "Adm: Player " + ChatColor.YELLOW + args[0] +  ChatColor.RED + " was kicked by " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " reason:" + ChatColor.YELLOW + kickMessage);
				}
				if(args.length < 1) {
					player.sendMessage(ChatColor.RED + "Use: " + ChatColor.YELLOW + "/kick and player name & reason");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("ban")) {
			if(player.hasPermission("mrtunne.ess.ban")) {
				if(args.length > 0) {
					String banReason = args[1];
					Bukkit.getBanList(Type.NAME).addBan(args[0], banReason, null, player.getName());
					Player target = Bukkit.getPlayerExact(args[0]);
					target.kickPlayer(args[1]);
					Bukkit.broadcastMessage(ChatColor.RED + "Adm: Player " + ChatColor.YELLOW + args[0] +  ChatColor.RED + " was banned by " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " reason:" + ChatColor.YELLOW + banReason);
				}
				if(args.length < 1) {
					player.sendMessage(ChatColor.RED + "Use: " + ChatColor.YELLOW + "/ban and player name & reason");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("unban")) {
			if(player.hasPermission("mrtunne.ess.unban")) {
				if(args.length > 0) {
					Bukkit.getBanList(Type.NAME).pardon(args[0]);
					Bukkit.broadcastMessage(ChatColor.RED + "Adm: Player " + ChatColor.YELLOW + args[0] +  ChatColor.RED + " was unbanned by " + ChatColor.YELLOW + player.getName());
				}
				if(args.length < 1) {
					player.sendMessage(ChatColor.RED + "Use: " + ChatColor.YELLOW + "/unban and player name");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("sunny")) {
			if(player.hasPermission("mrtunne.ess.sunny")) {
				player.sendMessage(ChatColor.RED + "Sunny was activated.");
				for(World w : Bukkit.getServer().getWorlds()){
					w.setStorm(false);
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("setspawn")) {
			if(player.hasPermission("mrtunne.ess.setspawn")) {
				getConfig().set("Spawn.X", player.getLocation().getBlockX());
				getConfig().set("Spawn.Y", player.getLocation().getBlockY());
				getConfig().set("Spawn.Z", player.getLocation().getBlockZ());
				saveConfig();
				player.sendMessage(ChatColor.RED + "Spawn is now configured.");
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("spawn")) {
			player.sendMessage(ChatColor.RED + "You have been teleported to spawn.");
			World w = getServer().getWorld("world");
			int x = getConfig().getInt("Spawn.X");
			int y = getConfig().getInt("Spawn.Y");
			int z = getConfig().getInt("Spawn.Z");
			Location t = new Location(w,x,y,z);
			player.teleport(t);
		}
		else if(cmd.getName().equalsIgnoreCase("ram")) {
			if(player.hasPermission("mrtunne.ess.ram")) {
				player.sendMessage(ChatColor.RED + "Total free ram: " + ChatColor.YELLOW + Runtime.getRuntime().freeMemory());
				player.sendMessage(ChatColor.RED + "Total ram: " + ChatColor.YELLOW + Runtime.getRuntime().totalMemory());
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("mute")) {
			Player mutePlayer = Bukkit.getPlayer(args[0]);
			if(player.hasPermission("mrtunne.ess.mute")) {
				if(args.length < 1) {
					player.sendMessage(ChatColor.YELLOW + "You have to specify a player first!");
					player.sendMessage(ChatColor.RED + "Use: " + ChatColor.YELLOW + "/mute player");
				}
				else {
					mute.add(mutePlayer);
					player.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + args[0] + ChatColor.RED + " have been muted!");
					Bukkit.broadcastMessage(ChatColor.RED + "Adm: Player " + ChatColor.YELLOW + args[0] +  ChatColor.RED + " was muted by " + ChatColor.YELLOW + player.getName());
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("unmute")) {
			Player mutePlayer = Bukkit.getPlayer(args[0]);
			if(player.hasPermission("mrtunne.ess.unmute")) {
				if(args.length == 0) {
					player.sendMessage(ChatColor.YELLOW + "You have to specify a player first!");
					player.sendMessage(ChatColor.RED + "Use: " + ChatColor.YELLOW + "/unmute player");
				}
				else {
					mute.remove(mutePlayer);
					player.sendMessage(ChatColor.RED + "Player " + ChatColor.YELLOW + args[0] + ChatColor.RED + " have been unmuted!");
					Bukkit.broadcastMessage(ChatColor.RED + "Adm: Player " + ChatColor.YELLOW + args[0] +  ChatColor.RED + " was unmuted by " + ChatColor.YELLOW + player.getName());
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("cc")) {
			if(player.hasPermission("mrtunne.ess.cc")) {
				int cc;
				for(cc = 0; cc<120; cc++) {
					Bukkit.broadcastMessage(" ");
				}
				Bukkit.broadcastMessage(ChatColor.RED + "Chat was cleared by " + ChatColor.YELLOW + player.getName());
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not admin.");
			}
		}
		else if(cmd.getName().equalsIgnoreCase("report")) {
			if(!player.isOp())
				{
					for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						if(!p.isOp() || p.hasPermission("mrtunne.ess.reports")) {
							String globalMessage = "";   
							for(int i = 0; i < args.length; i++){
							    String arg = args[i] + " ";
							    globalMessage = globalMessage + arg;
							}
						    String playername = player.getName();
						    if(p.isOp() || p.hasPermission("mrtunne.ess.reports")) {
						    	p.sendMessage(ChatColor.YELLOW + "REPORT(" + ChatColor.GREEN + playername + ChatColor.YELLOW + "): " + ChatColor.RED + globalMessage);
						    }
						    else {
						    	if(args.length > 0) {
						    		player.sendMessage(ChatColor.RED + "Your report was sent to admins.");
						    	}
						    }
						}
					}
				}
			    if(args.length == 0) {
			    	player.sendMessage(ChatColor.RED + "Use: " + ChatColor.YELLOW + "/report and your message for admins.");
			    }
			}
		else if(cmd.getName().equalsIgnoreCase("a")) {
			{
			if(player.isOp() || player.hasPermission("mrtunne.ess.a")) {
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						if(player.hasPermission("mrtunne.ess.a")) {
						    if(args.length < 1) {
						    	player.sendMessage(ChatColor.RED + "Use: " + ChatColor.YELLOW + "/a and your message.");
						    }
						}
					    if(player.hasPermission("mrtunne.ess.a")) {
							String globalMessage = "";   
							for(int i = 0; i < args.length; i++){
							    String arg = args[i] + " ";
							    globalMessage = globalMessage + arg;
							}
						    String playername = player.getName();
					    	p.sendMessage(ChatColor.RED + playername + ChatColor.YELLOW + ": " + ChatColor.YELLOW + globalMessage);
					    }
					}
				}
			    else {
			    	player.sendMessage(ChatColor.RED + "You don't have permission.");
			    }
			}
		}
		else if(cmd.getName().equalsIgnoreCase("tp")) {
			if(player.hasPermission("mrtunne.ess.tp")) {
				if(args.length < 1) {
					player.sendMessage(ChatColor.RED + "Use: " + ChatColor.YELLOW + "/tp and player name.");
				}
				else {
					player.teleport(Bukkit.getPlayer(args[0]));
					player.sendMessage(ChatColor.RED + "You have been teleported to " + ChatColor.YELLOW + ChatColor.BOLD + args[0]);
				}
			}
			else {
				player.sendMessage("You are not admin.");
			}
		}
		return false;
		
	}
}
