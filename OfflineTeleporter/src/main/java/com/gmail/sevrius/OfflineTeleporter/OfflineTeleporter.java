package com.gmail.sevrius.OfflineTeleporter;
import java.io.*;
import java.nio.file.Files;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;

public class OfflineTeleporter extends JavaPlugin implements Listener{
	public File data = (new File(getDataFolder(), "/Data"));
	private FileConfiguration config = null;
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		if(!new File(getDataFolder(), "config.yml").exists()){ //Check for our config
			getLogger().info("Creating Config.yml file..");
			saveDefaultConfig();
			}
		if(!new File(getDataFolder(), "Data").exists()){ //check for our Data Folder
			getLogger().info("Creating Data Folder..");
			new File(getDataFolder(), "Data").mkdir();
			}
		reloadConfig(); //needed or does it autoload?
		config = getConfig();
		
	}
	
	@Override
	public void onDisable(){
		//TODO CLeanup
	}
	
	
	
	
	
	
	@EventHandler
	public void joinEvent(PlayerJoinEvent event){ //check if the player has a user file, if not create one AND TP him to a possible loc!
		String player = event.getPlayer().getName();
		if (!new File(data, player + ".yml").exists()){
			try{createFile(player);}catch(IOException rr) {getLogger().info("Couldn't close the file");}
			letsConf(player).set("name", player); //doesn't work for some reason..
			letsSave(player);
			}
		if (letsConf(player).getList("newPosition") != null){ //this somehow screws with the format i think..
			getLogger().info("Not Null"); //Dbug
			//TODO Add TP to loc
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){ //put the player's loc in his file
		String player = event.getPlayer().getName();
		Location loc = event.getPlayer().getLocation();
		
		getLogger().info(player+" Quits tha game!");//Dbug
		getLogger().info(event.getPlayer().getLocation().toString());
		letsConf(player).set("lastPosition", loc);
		letsSave(player);
	}	
	
	public FileConfiguration letsConf(String player){// return a FileConfig variable (to play around with)
		File playerFile = new File(getDataFolder(), "/Data/"+player+".yml");
		FileConfiguration playerc = YamlConfiguration.loadConfiguration(playerFile);
		return playerc;
	}
	
	public void letsSave(String player){ //saves the player.yml
		File playerFile = new File(getDataFolder(), "/Data/"+player+".yml");
		try {letsConf(player).save(playerFile);} catch(IOException ex) {getLogger().info("Player file not found! | My fault! :(  l62");}
	}
	
	public void createFile(String name) throws IOException{// "throws" is basically a try statement, right? :s | Had too add "throws" cause of l95 and l96 :s
		InputStream src = this.getResource("user.yml");
		OutputStream os = null;
		int readb;
		byte[] buffer = new byte[4096];
					
		try{
			getLogger().info("Creating new user file for "+name);
			os = new FileOutputStream(new File(getDataFolder(),"/Data/"+name+".yml"));
			while ((readb = src.read(buffer)) > 0){
				os.write(buffer, 0,readb);
			  	}
			} catch (IOException error) { 				  //Why is this needed since line 80 catches all the exceptions in this method anyway..?
				getLogger().info("I did it! :( (l56-57)");//Like if something in this method throws an IOException it'll fall back to the try loop at l48, no?
			} finally {
				src.close(); 
				os.close();
			}
	}
}
