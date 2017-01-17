package me.Ckay.pvp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Map;
import java.util.Random;
//import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Ckay.pvp.SettingsManager;
import me.Ckay.pvp.Main;

public class Main extends JavaPlugin implements Listener {
	
	//private Map<Integer, List<Player>> inArena;
	private List<Player> inArena2;
	private List <Player> vampire;
	private List <Player> zombie;
	private List <Player> player;

	Boolean allowAll = false;
	boolean god = true;
	boolean lobby = true;
	boolean gameStarted = false;
	boolean gameEnded = false;
	boolean delay1done = false;
	boolean delay2done = false;
	boolean delay3done = false;
	boolean delay4done = false;
	boolean delay5done = false;
	boolean delay6done = false;
	boolean delay7done = false;
	boolean delay8done = false;
	boolean enableInv = false;
	boolean invisCooldown = true;
	
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Main plugin;
	
	private Map<String, Integer> cooldownTime;
	private Map<String, Integer> gracePeriod;
	private Map<String, Integer> invisPeriod;
	private Map<String, Integer> delayPeriod;
	private Map<String, Integer> startPeriod;
	private Map<String, Integer> endPeriod;
	private List<Player> banList;
	private Map<String, BukkitRunnable> invisTask;
	private Map<String, BukkitRunnable> borderTask;
	private Map<String, BukkitRunnable> nightTask;
	private Map<String, BukkitRunnable> vampireTask;
	private Map<String, BukkitRunnable> zombieTask;
	private Map<String, BukkitRunnable> startTask;
	private Map<String, BukkitRunnable> endTask;
	private Map<String, BukkitRunnable> playerTask;
	private Map<String, BukkitRunnable> delayTask;
	
	//private static Main instance;
	
	SettingsManager settings = SettingsManager.getInstance();

	public void onEnable()
	  {
		getServer().getPluginManager().registerEvents(this, this);
		
		settings.setup(this);
		settings.setupBanned(this);
		
		if (settings.getCfg().get("config.players") != (null)) {
			//already exists
		}
		else {

			int players = 20;
			
			settings.getCfg().set("config.players", players);
			settings.saveCfg();
		}
		
		
		if (settings.getCfg().get("config.radius") != (null)) {
			//already exists
		}
		else {

			//how high
			int y = 68;
			//z cord radius
			int z = 20;
			//x cord radius
			int x = 40;
			
			settings.getCfg().set("config.radius.x", x);
			settings.getCfg().set("config.radius.y", y);
			settings.getCfg().set("config.radius.z", z);
			settings.saveCfg();
		}
		
		World arena = Bukkit.getWorld("World");
        arena.setAutoSave(false);
		
		nightTask = new HashMap<String, BukkitRunnable>();
		vampireTask = new HashMap<String, BukkitRunnable>();
		playerTask = new HashMap<String, BukkitRunnable>();
		zombieTask = new HashMap<String, BukkitRunnable>();
		startTask = new HashMap<String, BukkitRunnable>();
		endTask = new HashMap<String, BukkitRunnable>();
		invisTask = new HashMap<String, BukkitRunnable>();
		borderTask = new HashMap<String, BukkitRunnable>();
		delayTask = new HashMap<String, BukkitRunnable>();
		
		//inArena = new HashMap<Integer, List<Player>>();
		inArena2 = new ArrayList<Player>();
		vampire = new ArrayList<Player>();
		zombie = new ArrayList<Player>();
		player = new ArrayList<Player>();

		banList = new ArrayList<Player>();
		cooldownTime = new HashMap <String, Integer>();
		gracePeriod = new HashMap <String, Integer>();
		invisPeriod = new HashMap <String, Integer>();
		delayPeriod = new HashMap <String, Integer>();
		startPeriod = new HashMap <String, Integer>();
		endPeriod = new HashMap <String, Integer>();
		
		for (Player playersOnline : Bukkit.getOnlinePlayers()) {
	        for(Entity en : playersOnline.getWorld().getEntities()){
	            if(!(en instanceof Player)) {

	            en.remove();
	            }
	      }
		}
		
		//instance = this; 
		
	  }
	
	public void onDisable() {
		
	for (Player playersOnline : Bukkit.getOnlinePlayers()) {
        for(Entity en : playersOnline.getWorld().getEntities()){
            if(!(en instanceof Player)) {

            en.remove();
            }
      }
	}
		
//		File folder = getServer().getWorld("World").getWorldFolder();
//
//		deleteDirectory(folder);
//
//		final File templateFolder = new File(getServer().getWorldContainer(), "WorldTemplate");
//		final File worldOneFolder = new File(getServer().getWorldContainer(), "World");
//
//		copyDir(templateFolder, worldOneFolder);
//		
//		System.out.println("Directories (Worlds Copied)");
		
		settings.getData().set("players", null);
        settings.saveData();
		
	}
	
//	public void onDeath(PlayerDeathEvent e) {
//		
//		Player p = e.getEntity();
//		
//		if (inArena2.contains(p)) {
//			System.out.println(p + " is in the arena, removing him");
//			System.out.println(inArena2.size());
//			inArena2.remove(p);
//			System.out.println("Removed player from arena.");
//			System.out.println(inArena2.size());
//			
//			
//			
//		}
//		
//	}
	
	
	@EventHandler
	  public void FixIt(FoodLevelChangeEvent e)
	  {
	    if ((e.getFoodLevel() < ((Player)e.getEntity()).getFoodLevel()) && 
	      (new Random().nextInt(100) > 4)) {
	      e.setCancelled(true);
	    }
	  }
	
	public void spawnBoat(Location l, Player p){
		 Entity e = l.getWorld().spawnEntity(l, EntityType.BOAT);
		 e.setPassenger(p);
		}
	

	@SuppressWarnings("unused")
	private static void copyDir(File source, File target) {
		try {
			ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
			if (!ignore.contains(source.getName())) {
				if (source.isDirectory()) {
					if (!target.exists())
						target.mkdirs();
					String files[] = source.list();
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyDir(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
				}
			}
		} catch (IOException e) {

		}
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				} // end else
			}
		}
		return (path.delete());
	}


	
	 public Player getNearest(Player p, Double range) {
		 double distance = Double.POSITIVE_INFINITY; // To make sure the first
		 // player checked is closest
		 Player target = null;
		 	for (Entity e : p.getNearbyEntities(range, range, range)) {
		 		if (!(e instanceof Player))
		 			continue;
		 		if(e == p) continue; //Added this check so you don't target yourself.
		 			double distanceto = p.getLocation().distance(e.getLocation());
		 		if (distanceto > distance)
		 			continue;
		 			distance = distanceto;
		 			//target = (Player) e;
		 				if (player.contains(target)) {
		 					target = (Player) e;
		 				}
		 }
		 return target;
		 }
	 
	 
	 Random rand = new Random();
	 int generateInt(int min, int max)
	 {
	     int randInt = rand.nextInt(min - max) + 1 + min;
	     return randInt;
	 }
	 
	 @SuppressWarnings("deprecation")
	public void startCommand() {
		 god = false;
		  final int arenaSize = inArena2.size();
		  final int vampireSize = vampire.size();
		  final int zombieSize = zombie.size();
		  gameStarted = true;
		  lobby = false;
		  Bukkit.setWhitelist(true);
		  
		  //p.sendMessage(ChatColor.GREEN + "Starting arena, teleporting players to random locations within 560 blocks radius.");
		  Bukkit.broadcastMessage(ChatColor.GOLD + "The game has started! Teleporting to player start!");
		  
		  //Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb World set 450 450 spawn");
		  //Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
		  
		  
		  
		 //double x = settings.getData().getDouble("warps.spawn.x");
		 //double z = settings.getData().getDouble("warps.spawn.z");
		  
		  for (int i =1; i <= vampireSize; i++) {
			  int playerSpot = i -1;
			  final Player playersOnline = vampire.get(playerSpot);
			  
			  playersOnline.setFoodLevel(20);
			  playersOnline.setMaxHealth(10);
			  playersOnline.setHealth(10);
		  
		 	ItemStack WSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
		 	SkullMeta meta = (SkullMeta) WSkull.getItemMeta();
		 	meta.setOwner("MHF_Enderman");
		 	meta.setDisplayName(ChatColor.DARK_PURPLE + "Vampire " + playersOnline.getName());
		 	WSkull.setItemMeta(meta);
			playersOnline.getInventory().setHelmet(WSkull);
			
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
			chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			chest.addEnchantment(Enchantment.DURABILITY, 2);
			meta2.setColor(Color.BLACK);
			chest.setItemMeta(meta2);
			playersOnline.getInventory().setChestplate(chest);
			
		     ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
			 sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			 sword.addEnchantment(Enchantment.DURABILITY, 1);
			 ItemMeta itemMeta4 = sword.getItemMeta();
			 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Sharpeness Stone Sword"));
			 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "Hunt for Blood!!"));
			 sword.setItemMeta(itemMeta4);
			 
			 ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 1);
			 ItemMeta itemMeta3 = carrot.getItemMeta();
			 itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Invisibility"));
			 itemMeta3.setLore(Arrays.asList(ChatColor.GREEN + "Right click to go Invisible!!"));
			 
			 ItemStack compass = new ItemStack(Material.COMPASS, 1);
			 ItemMeta itemMeta2 = compass.getItemMeta();
			 itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Track Player's"));
			 itemMeta2.setLore(Arrays.asList(ChatColor.GREEN + "Right click to track the nearest player to you!"));
			 compass.setItemMeta(itemMeta2);
			 
//			 if (!playersOnline.getInventory().contains(compass)) {
//				 playersOnline.getInventory().addItem(compass);
//				 }
			 
//			 if (!playersOnline.getInventory().contains(carrot)) {
//				 playersOnline.getInventory().addItem(carrot);
//			 }
			 
			 if (!playersOnline.getInventory().contains(sword)) {
				 playersOnline.getInventory().addItem(sword);
				 }
			 
			
			World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.vampire.world"));
	        double x = settings.getData().getDouble("warps.vampire.x");
	        double y = settings.getData().getDouble("warps.vampire.y");
	        double z = settings.getData().getDouble("warps.vampire.z");
	        playersOnline.teleport(new Location(w, x, y, z));
	        //p.setBedSpawnLocation(loc, true);
			
		  }
		  
		  for (int i =1; i <= zombieSize; i++) {
			  int playerSpot = i -1;
			  final Player playersOnline = zombie.get(playerSpot);
			  
			  playersOnline.setFoodLevel(20);
			  //playersOnline.setHealth(20);
		  
		 	ItemStack ZSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
		 	SkullMeta meta = (SkullMeta) ZSkull.getItemMeta();
		 	meta.setOwner("MHF_Zombie");
		 	meta.setDisplayName(ChatColor.DARK_GREEN + "Zombie " + playersOnline.getName());
		 	ZSkull.setItemMeta(meta);
			playersOnline.getInventory().setHelmet(ZSkull);
			
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
			meta2.setColor(Color.GREEN);
			chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			chest.addEnchantment(Enchantment.DURABILITY, 2);
			chest.setItemMeta(meta2);
			playersOnline.getInventory().setChestplate(chest);
			
			ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
			 sword.addEnchantment(Enchantment.KNOCKBACK, 2);
			 sword.addEnchantment(Enchantment.DURABILITY, 1);
			 sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			 ItemMeta itemMeta4 = sword.getItemMeta();
			 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Knockback Wood Sword"));
			 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "ATTACK!"));
			 sword.setItemMeta(itemMeta4);
			 
			 if (!playersOnline.getInventory().contains(sword)) {
				 playersOnline.getInventory().addItem(sword);
				 }
			
			World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.zombie.world"));
	        double x = settings.getData().getDouble("warps.zombie.x");
	        double y = settings.getData().getDouble("warps.zombie.y");
	        double z = settings.getData().getDouble("warps.zombie.z");
	        playersOnline.teleport(new Location(w, x, y, z));
	        //p.setBedSpawnLocation(loc, true);
	        
		  }
		  
		  for (int i = 1; i <= arenaSize; i++) {
			  int playerSpot = i -1;
			  final Player playerInArena = inArena2.get(playerSpot);
			  
			  	playerInArena.setFoodLevel(20);
				//playerInArena.setHealth(20);
				
				if (player.contains(playerInArena)) {
					System.out.println("Started player TP");
					tpDelay(playerInArena);
					 System.out.println("Done player TP");
					playerInArena.sendMessage(ChatColor.RED + "You have been teleported!");
				}

				 
				 ItemStack bow = new ItemStack(Material.BOW, 1);
				 bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
				 ItemMeta itemMeta3 = bow.getItemMeta();
				 itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Bow of DOOM"));
				 itemMeta3.setLore(Arrays.asList(ChatColor.GREEN + "Snipe those mobs!"));
				 bow.setItemMeta(itemMeta3);
				 
				 ItemStack cookedfish = new ItemStack(Material.COOKED_FISH, 32);
				 
				 ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
				 sword.addEnchantment(Enchantment.DURABILITY, 1);
				 ItemMeta itemMeta4 = sword.getItemMeta();
				 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Weak Sword"));
				 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "This is useless!!"));
				 sword.setItemMeta(itemMeta4);
				 
				 ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
				 ItemMeta itemMeta5 = chest.getItemMeta();
				 itemMeta5.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Leather Chest"));
				 itemMeta5.setLore(Arrays.asList(ChatColor.GREEN + "Lets hope this is thick enough!"));
				 chest.setItemMeta(itemMeta5);
				
//				 if (!playerInArena.getInventory().contains(arrows)) {
//					 playerInArena.getInventory().addItem(arrows);
//					 }
				 
				 if (!playerInArena.getInventory().contains(cookedfish)) {
					 playerInArena.getInventory().addItem(cookedfish);
					 }
				 
				 if (!playerInArena.getInventory().contains(chest)) {
						playerInArena.getInventory().setChestplate(chest);
					 }
				 
				 if (!playerInArena.getInventory().contains(sword)) {
					playerInArena.getInventory().addItem(sword);
					
				 }
//				 if (!playerInArena.getInventory().contains(bow)) {
//					 playerInArena.getInventory().addItem(bow);
//
//				    }
				 
				 settings.getData().set("players." + playerInArena.getName() + ".rank", 1);
				 settings.saveData();

	  } 
			  final int radius = 1200;
			  
			  cooldownTime.put("Countdown", radius);
			  final World world = Bukkit.getServer().getWorld("World");
			  
			  //final int night = 18000;
			  final int day = 6000;
			  
			  System.out.println("gamestarted on start = " + gameStarted);
			  
			  vampireEffect();
			  zombieEffect();
			  playerEffect();
			  
			  world.setTime(day);
			  
			  System.out.println("gracePeriod on start size: "+ gracePeriod.size());
			  System.out.println("inArena2 list on start size: "+ inArena2.size());
			  
			  
			  //if (gameStarted = true) {
				  
				  //String ckay = "ABkayCkay";
					 
				
				//Player playerInArena = Bukkit.getPlayer(ckay);
				  
					 //final World world = playerInArena.getWorld();
					 final int night = 18000;
					 //final int day = 6000;
					  
					  nightTask.put("night", new BukkitRunnable() {
	    					
							@Override
							public void run() {
								
								world.setTime(night);
								
								if (gameStarted = false) {
									world.setTime(day);
									nightTask.remove("night");
					    			nightTask.clear();
									cancel();
								}

							}
	    					
	    				});
	    				
	    				nightTask.get("night").runTaskTimer(this, 20, 20);
					  
//					  Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
//						    public void run() {
//						    	
//						    	world.setTime(night);
//						    	
//						    }
//						}, 20, 20);
					  
					  
					  borderTask.put("border", new BukkitRunnable() {
						    public void run() {
						    	
						    	final int arenaSize = inArena2.size();
						    	
						    	if (cooldownTime.get("Countdown") != null) {
						   
						    	cooldownTime.put("Countdown", cooldownTime.get("Countdown") - 1);

						    	if (cooldownTime.get("Countdown") == 1080) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Confusion event for 30 seconds!");
						    	}
						    		 
//						    		for (Player playersOnline : Bukkit.getOnlinePlayers()) {
//						    			
//						    			if (!(playersOnline.getName().equalsIgnoreCase("ABkayCkay") || playersOnline.getName().equalsIgnoreCase("MikeSN") || playersOnline.getName().equalsIgnoreCase("killzyazmadness") || playersOnline.getName().equalsIgnoreCase("Vegeta") || playersOnline.getName().equalsIgnoreCase("Hacko_Jacko") || playersOnline.getName().equalsIgnoreCase("PokeKhan") || playersOnline.getName().equalsIgnoreCase("lod77") || playersOnline.getName().equalsIgnoreCase("Pagub"))) {	
//						    				if (!(playersOnline.getGameMode() == GameMode.SPECTATOR)) {
//						    					if (player.contains(playersOnline)) {
//						    						playersOnline.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
//						    					}
//						    				}
//						    		
//						    			}
//						    		}
						    	
						    	
						    	if (cooldownTime.get("Countdown") == 960) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Weakness event for 20 seconds!");
						    	}
						    	
						    	if (cooldownTime.get("Countdown") == 840) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 600, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Health Boost event for 30 seconds!");
						    	}
						    	
						    	if (cooldownTime.get("Countdown") == 720) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 600, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Glowing event for 30 seconds!");
						    	}
						    	
						    	if (cooldownTime.get("Countdown") == 600) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Confusion event for 10 seconds!");
						    	}
						    	
						    	
						    	if (cooldownTime.get("Countdown") == 480) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Hunger Boost event for 30 seconds!");
						    	}
						    	
						    	if (cooldownTime.get("Countdown") == 360) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Poison event for 10 seconds!");
						    	}
						    	
						    	if (cooldownTime.get("Countdown") == 240) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Regeneration event for 10 seconds!");
						    	}
						    	
						    	if (cooldownTime.get("Countdown") == 120) {
						    		
						    		for (int i = 1; i <= arenaSize; i++) {
										  int playerSpot = i -1;
										  final Player playerInArena = inArena2.get(playerSpot);
										  if (player.contains(playerInArena)) {
					    						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 1));
					    					}
										  
										  
					    			 }
						    		Bukkit.broadcastMessage(ChatColor.RED + "Glowing event for 60 seconds!");
						    	}
						    	
						    	
						    		if (cooldownTime.get("Countdown") == 60) {
											  
								                cooldownTime.remove("Countdown");
								                borderTask.remove("border");
								    			borderTask.clear();
								                cancel();
											 
						    			}
						    			
						    		}
						    }
						});
					  borderTask.get("border").runTaskTimer(this, 20, 20);
	 }
	 
	//Unloading maps, to rollback maps. Will delete all player builds until last server save
	    public static void unloadMap(String mapname){
	        if(Bukkit.getServer().unloadWorld(Bukkit.getServer().getWorld(mapname), false)){
	            plugin.getLogger().info("Successfully unloaded " + mapname);
	        }else{
	            plugin.getLogger().severe("COULD NOT UNLOAD " + mapname);
	        }
	    }
	    
	  //Loading maps (MUST BE CALLED AFTER UNLOAD MAPS TO FINISH THE ROLLBACK PROCESS)
	    public static void loadMap(String mapname){
	    	World w = Bukkit.getServer().createWorld(new WorldCreator(mapname));
	        w.setAutoSave(false);
	    }
	    
	  //Maprollback method, because were too lazy to type 2 lines
	    public static void rollback(String mapname){
	        unloadMap(mapname);
	        loadMap(mapname);
	    }
	 
	 public void endGame() {
		 
		 endPeriod.put("End", 10);
		 
		endTask.put("end", new BukkitRunnable() {
				
				@Override
				public void run() {
							endPeriod.put("End", endPeriod.get("End") - 1);
							Bukkit.broadcastMessage(ChatColor.GOLD + "Game ending in " + endPeriod.get("End") + " seconds!");
							if (endPeriod.get("End") == 0) {
								
								for (Player players : Bukkit.getOnlinePlayers()) {
									
									World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.lobby.world"));
							        double x = settings.getData().getDouble("warps.lobby.x");
							        double y = settings.getData().getDouble("warps.lobby.y");
							        double z = settings.getData().getDouble("warps.lobby.z");
							        players.teleport(new Location(w, x, y, z));
							        
							        players.setGameMode(GameMode.SURVIVAL);
						            players.getInventory().clear();
						            players.getInventory().setArmorContents(null);					               
						            players.setFoodLevel(20);
									
									//players.playSound(players.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 2F, 1F);
								}
								endTask.remove("end");
								endTask.clear();
								endPeriod.remove("End");
								endPeriod.clear();	
								cooldownTime.remove("Countdown");
								cooldownTime.clear();
								inArena2.clear();
								System.out.println("inArena2 list size: "+ inArena2.size());
								gracePeriod.remove("Grace");
								gracePeriod.clear();
								System.out.println("gracePeriod on reset size: "+ gracePeriod.size()); 
								god = true;
								lobby = true;
								gameStarted = false;
								Bukkit.setWhitelist(false);
								zombie.clear();
								vampire.clear();
								player.clear();
								banList.clear();
								invisTask.clear();
								borderTask.clear();
								nightTask.clear();
								vampireTask.clear();
								zombieTask.clear();
								startTask.clear();
								playerTask.clear();
								delayTask.clear();
								invisPeriod.clear();
								delayPeriod.clear();
								startPeriod.clear();

								rollback("WorldTemplate");
								Bukkit.broadcastMessage(ChatColor.GREEN + "World Reloaded, join the game back again by typing /hunt join!");

								cancel();
							}


				}

			});
			
		 endTask.get("end").runTaskTimer(this, 20, 20);
	 }
	 
	 public void startGame() {
		 
		 startPeriod.put("Start", 10);
		 
		 startTask.put("start", new BukkitRunnable() {
				
				@Override
				public void run() {
					
					if (vampire.size() == 1) {
						if (player.size() > 15) {
							startPeriod.put("Start", startPeriod.get("Start") - 1);
							Bukkit.broadcastMessage(ChatColor.GOLD + "Game Starting in " + startPeriod.get("Start") + " seconds!");
							if (startPeriod.get("Start") == 0) {
								startCommand();
								startTask.remove("start");
								startTask.clear();
								startPeriod.remove("Start");
								startPeriod.clear();
								cancel();
							}
						}
					}
					
					if (gameStarted = true) {
						startTask.remove("start");
						startTask.clear();
						cancel();
					}

				}

			});
			
		 startTask.get("start").runTaskTimer(this, 20, 20);
	 }
	 
	 public void vampireEffect() {
		 vampireTask.put("vampire", new BukkitRunnable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					final int arenaSize = vampire.size();
					
					for (int i = 1; i <= arenaSize; i++) {
						int playerSpot = i -1;
								  
						 final Player playerInArena = vampire.get(playerSpot);
					
					if (vampire.contains(playerInArena)) {
						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1), true);
						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 0), true);
						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 400, 1), true);
						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 400, 1), true);
						//playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1), true);
						playerInArena.setFoodLevel(20);
						
						for (Player players : Bukkit.getOnlinePlayers()) {
							players.playEffect(playerInArena.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
							}
						}
					}
					
					if (gameStarted = false) {
						vampireTask.remove("vampire");
						vampireTask.clear();
						cancel();
					}

				}

			});
			
		 vampireTask.get("vampire").runTaskTimer(this, 1, 189);
	 }
	 
	 public void zombieEffect() {
		 zombieTask.put("zombie", new BukkitRunnable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					
					final int arenaSize = zombie.size();
						
				for (int i = 1; i <= arenaSize; i++) {
					int playerSpot = i -1;
							  
					 final Player playerInArena = zombie.get(playerSpot);
							
					 if (zombie.contains(playerInArena)) {
						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 1), true);
						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 0), true);
						playerInArena.setFoodLevel(20);
						
						for (Player players : Bukkit.getOnlinePlayers()) {
							players.playEffect(playerInArena.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
						}
					}
					
				}
					
					 	if (gameStarted = false) {
					 		zombieTask.remove("zombie");
					 		zombieTask.clear();
					 		cancel();
					 	}

				}
				
			});
			
		 zombieTask.get("zombie").runTaskTimer(this, 1, 189);
	 }
	 
	 public void playerEffect() {
		 playerTask.put("player", new BukkitRunnable() {
				
				@Override
				public void run() {
					final int arenaSize = player.size();
					
					for (int i = 1; i <= arenaSize; i++) {
						int playerSpot = i -1;
								  
						 final Player playerInArena = player.get(playerSpot);
					
					if (player.contains(playerInArena)) {
						playerInArena.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 3), true);
						
						}
					
					}

					if (gameStarted = false) {
						playerTask.remove("player");
						playerTask.clear();
						cancel();
					}
				}
				
			});
			
		 playerTask.get("player").runTaskTimer(this, 1, 189);
	 }
	 
	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void noPickup(PlayerPickupItemEvent e){
	        e.setCancelled(true);
	    }
	 
	 @SuppressWarnings("deprecation")
	public void playerToVamp(Player p) {
		 System.out.println("Is Vampire");
		 
		 p.getInventory().clear();
         p.getInventory().setArmorContents(null);
		 
		 p.setFoodLevel(20);
		 p.setMaxHealth(10);
		 p.setHealth(10);
	  
	 	ItemStack WSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
	 	SkullMeta meta = (SkullMeta) WSkull.getItemMeta();
	 	meta.setOwner("MHF_Enderman");
	 	meta.setDisplayName(ChatColor.DARK_PURPLE + "Vampire " + p.getName());
	 	WSkull.setItemMeta(meta);
		p.getInventory().setHelmet(WSkull);
		
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
		meta2.setColor(Color.BLACK);
		chest.setItemMeta(meta2);
		p.getInventory().setChestplate(chest);
		
		 ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
		 sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		 sword.addEnchantment(Enchantment.DURABILITY, 1);
		 ItemMeta itemMeta4 = sword.getItemMeta();
		 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Sharpeness Stone Sword"));
		 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "Hunt for Blood!!"));
		 sword.setItemMeta(itemMeta4);
		 
		 ItemStack compass = new ItemStack(Material.COMPASS, 1);
		 ItemMeta itemMeta2 = compass.getItemMeta();
		 itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Track Player's"));
		 itemMeta2.setLore(Arrays.asList(ChatColor.GREEN + "Right click to track the nearest player to you!"));
		 compass.setItemMeta(itemMeta2);
		 
		 ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 1);
		 ItemMeta itemMeta3 = carrot.getItemMeta();
		 itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Invisibility"));
		 itemMeta3.setLore(Arrays.asList(ChatColor.GREEN + "Right click to go Invisible!!"));
		 
//		 if (!p.getInventory().contains(carrot)) {
//			 p.getInventory().addItem(carrot);
//		 }
		 
		 if (!p.getInventory().contains(compass)) {
			 p.getInventory().addItem(compass);
				
			 }

		 if (!p.getInventory().contains(sword)) {
			 p.getInventory().addItem(sword);
			 }
		 
		 System.out.println("Gave Items");
		 //vampireEffect(p);
		
		World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.vampire.world"));
        double x = settings.getData().getDouble("warps.vampire.x");
        double y = settings.getData().getDouble("warps.vampire.y");
        double z = settings.getData().getDouble("warps.vampire.z");
        Location loc = new Location(w, x, y, z);
        p.teleport(loc);
        //pre.setRespawnLocation(loc);
	 }
	 
	 public void rankUpPlayer(Player p) {
		 
		 System.out.println("Got to Rank up player");	
		 
		 int currentRank = (int) settings.getData().get("players." + p.getName()+ ".rank");
		 int nextRank = currentRank +1;
		 
		 settings.getData().set("players." + p.getName()+ ".rank", nextRank);
		 settings.saveData();
		 
		 int newRank = (int) settings.getData().get("players." + p.getName()+ ".rank");
		 
		 ItemStack woodSword = new ItemStack(Material.WOOD_SWORD, 1);
		 ItemStack stoneSword = new ItemStack(Material.STONE_SWORD, 1);
		 ItemStack ironLegs = new ItemStack(Material.IRON_LEGGINGS, 1);
		 ItemStack fish = new ItemStack(Material.COOKED_FISH, 15);
		 ItemStack ironHelm = new ItemStack(Material.IRON_HELMET, 1);
		 ItemStack goldSword = new ItemStack(Material.GOLD_SWORD, 1);
		 //fish
		 ItemStack ironChest = new ItemStack(Material.IRON_CHESTPLATE, 1);
		 //arrows
		 ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		 ItemStack diamondHelm = new ItemStack(Material.DIAMOND_HELMET, 1);
		 //fish
		 //arrows
		 ItemStack diamondLegs = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
		 ItemStack diamondChest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		 ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD, 1);
		 
		 p.sendMessage(ChatColor.GREEN + "You leveled up from " +currentRank+ " to " +nextRank);
		 
		 if (newRank == 1) {
			 p.getInventory().addItem(woodSword);
		 }
		 if (newRank == 2) {
			 p.getInventory().addItem(stoneSword);
		 }
		 if (newRank == 3) {
			 p.getInventory().addItem(ironLegs);
		 }
		 if (newRank == 4) {
			 p.getInventory().addItem(fish);
		 }
		 if (newRank == 5) {
			 p.getInventory().addItem(ironHelm);
		 }
		 if (newRank == 6) {
			 p.getInventory().addItem(goldSword);
		 }
		 if (newRank == 7) {
			 p.getInventory().addItem(fish);
		 }
		 if (newRank == 8) {
			 p.getInventory().addItem(ironChest);
		 }
		 if (newRank == 9) {
			 p.getInventory().addItem(diamondBoots);
		 }
		 if (newRank == 10) {
			 p.getInventory().addItem(diamondHelm);
		 }
		 if (newRank == 11) {
			 p.getInventory().addItem(fish);
		 }
		 if (newRank == 12) {
			 p.getInventory().addItem(diamondLegs);
		 }
		 if (newRank == 13) {
			 p.getInventory().addItem(diamondChest);
		 }
		 if (newRank == 14) {
			 p.getInventory().addItem(diamondSword);
		 }
		 
	 }
	 
	 @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	    public void onPlayerRespawn(PlayerRespawnEvent pre) {
		 Player p = (Player) pre.getPlayer();
		 
		 System.out.println("Got to repsawn Event");
		 
		 if (zombie.contains(p)) {
			 
			 System.out.println("Is Zombie");
			 
			 p.getInventory().clear();
	         p.getInventory().setArmorContents(null);
	         
	         System.out.println("Cleared");
			 
			  p.setFoodLevel(20);
			  //p.setHealth(20);
			  
			  System.out.println("Replenished");
		  
		 	ItemStack ZSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
		 	SkullMeta meta = (SkullMeta) ZSkull.getItemMeta();
		 	meta.setOwner("MHF_Zombie");
		 	meta.setDisplayName(ChatColor.DARK_GREEN + "Zombie " + p.getName());
		 	ZSkull.setItemMeta(meta);
			p.getInventory().setHelmet(ZSkull);
			
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
			meta2.setColor(Color.GREEN);
			chest.setItemMeta(meta2);
			p.getInventory().setChestplate(chest);
			
			ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
			 sword.addEnchantment(Enchantment.KNOCKBACK, 2);
			 sword.addEnchantment(Enchantment.DURABILITY, 1);
			 sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			 ItemMeta itemMeta4 = sword.getItemMeta();
			 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Knockback Wood Sword"));
			 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "ATTACK!"));
			 sword.setItemMeta(itemMeta4);
			 
			 if (!p.getInventory().contains(sword)) {
				 p.getInventory().addItem(sword);
				 }
			 
			 System.out.println("Gave Items");
			 
			 //zombieEffect(p);
			
			World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.zombie.world"));
	        double x = settings.getData().getDouble("warps.zombie.x");
	        double y = settings.getData().getDouble("warps.zombie.y");
	        double z = settings.getData().getDouble("warps.zombie.z");
	        Location loc = new Location(w, x, y, z);
	        pre.setRespawnLocation(loc);
	        
	        //p.teleport(new Location(w, x, y, z));
		 }
		 
		 if (vampire.contains(p)) {
			 
			 System.out.println("Is Vampire");
			 
			 p.getInventory().clear();
	         p.getInventory().setArmorContents(null);
			 
			 p.setFoodLevel(20);
			 p.setMaxHealth(10);
			 p.setHealth(10);
		  
		 	ItemStack WSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
		 	SkullMeta meta = (SkullMeta) WSkull.getItemMeta();
		 	meta.setOwner("MHF_Enderman");
		 	meta.setDisplayName(ChatColor.DARK_PURPLE + "Vampire " + p.getName());
		 	WSkull.setItemMeta(meta);
			p.getInventory().setHelmet(WSkull);
			
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
			meta2.setColor(Color.BLACK);
			chest.setItemMeta(meta2);
			p.getInventory().setChestplate(chest);
			
			ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
			 sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			 sword.addEnchantment(Enchantment.DURABILITY, 1);
			 ItemMeta itemMeta4 = sword.getItemMeta();
			 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Sharpeness Stone Sword"));
			 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "Hunt for Blood!!"));
			 sword.setItemMeta(itemMeta4);
			 
			 ItemStack compass = new ItemStack(Material.COMPASS, 1);
			 ItemMeta itemMeta2 = compass.getItemMeta();
			 itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Track Player's"));
			 itemMeta2.setLore(Arrays.asList(ChatColor.GREEN + "Right click to track the nearest player to you!"));
			 compass.setItemMeta(itemMeta2);
			 
			 ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 1);
			 ItemMeta itemMeta3 = carrot.getItemMeta();
			 itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Invisibility"));
			 itemMeta3.setLore(Arrays.asList(ChatColor.GREEN + "Right click to go Invisible!!"));
			 
//			 if (!p.getInventory().contains(carrot)) {
//				 p.getInventory().addItem(carrot);
//			 }
			 
			 if (!p.getInventory().contains(compass)) {
				 p.getInventory().addItem(compass);
					
				 }

			 if (!p.getInventory().contains(sword)) {
				 p.getInventory().addItem(sword);
				 }
			 
			 System.out.println("Gave Items");
			 //vampireEffect(p);
			
			World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.vampire.world"));
	        double x = settings.getData().getDouble("warps.vampire.x");
	        double y = settings.getData().getDouble("warps.vampire.y");
	        double z = settings.getData().getDouble("warps.vampire.z");
	        Location loc = new Location(w, x, y, z);
	        pre.setRespawnLocation(loc);
	        
	        //p.teleport(new Location(w, x, y, z));
		 }
		 
	 }
	 
public void tpDelay(final Player p) {
		 
		 System.out.println("Got to tpDelay");
		 
		 final Random random = new Random();
		 
		 delayPeriod.put("Delay", 1);
		  
		  delayTask.put("delay", new BukkitRunnable() {
			  
			    public void run() {
			    	
			    	System.out.println("Got to Run");
			    	
			    	if (delayPeriod.get("Delay") != null) {
			    		
			    		System.out.println("Got to null check");
						   
			    		delayPeriod.put("Delay", delayPeriod.get("Delay") - 1);
			    		System.out.println("zone Delay: " + delayPeriod.get("Delay"));
			    		
			    		if (delayPeriod.get("Delay") == 0) {
			    			
			    			//delay2done = true;
			    			System.out.println("zone Delay Done");

								 double xMansion = settings.getData().getDouble("warps.player.x");
								 double zMansion = settings.getData().getDouble("warps.player.z");								 
								 
								 if (player.contains(p)) {
									 
									 int XRad = settings.getCfg().getInt("config.radius.x");
									 int ZRad = settings.getCfg().getInt("config.radius.z");
									 int YRad = settings.getCfg().getInt("config.radius.y");
									 
									 int numberBetweenX = random.nextInt(XRad) +1;
									 int numberBetweenZ = random.nextInt(ZRad) +1;
									 //int numberBetweenY = random.nextInt(81) + 61;
									 int y = YRad;
								 
									 int x12 = (int) xMansion;
									 int z12 = (int) zMansion;
								 
									 int RanX = x12 + numberBetweenX;
									 int RanZ = z12 + numberBetweenZ;
								 
									 Location teleportLocation1 = new Location(p.getWorld(), RanX, y, RanZ);
									 p.teleport(teleportLocation1);
								 }
								 

			    			delayTask.remove("delay");
			    			delayPeriod.put("Delay", 1);
			    			delayTask.clear();
			    			cancel();
			    			
			    			}
			    		
			    		}
			    	else {
			    		System.out.println("Delay is null");
			    	}
			    }

			});
		  delayTask.get("delay").runTaskTimer(this, 1, 2);
		 
	 }

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		  
		  if (event.getInventory().getHolder() instanceof Player) {
			  
			Player holder = (Player) event.getInventory().getHolder();
			
		if (enableInv == false) {
			if (vampire.contains(holder)) {
				event.setCancelled(true);
			}
			if (zombie.contains(holder)) {
				event.setCancelled(true);
			}
		}
		else {
			//do nothing
		}
		  }
	}

	 
	 @SuppressWarnings("deprecation")
	@EventHandler
	    public void onCompassTracker(PlayerInteractEvent e){
	       final Player p = e.getPlayer();
//	         ItemStack bone = new ItemStack(Material.BONE, 1);
//			 ItemMeta itemMeta3 = bone.getItemMeta();
//			 itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Spawn Pet Wolf"));
//			 itemMeta3.setLore(Arrays.asList(ChatColor.GREEN + "Right click to spawn a pet wolf!"));
//			 bone.setItemMeta(itemMeta3);
			 
	       if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && p.getItemInHand().getType() == Material.ENDER_PEARL){
	    	// Remove the pearl.
		        //p.getInventory().removeItem(new ItemStack[] {new ItemStack(Material.ENDER_PEARL, 1)});
	    	   if(p.getItemInHand().getAmount() > 1) {
	    		   p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
	    		 } else {
	    		   p.getItemInHand().setAmount(0); 
	    		 }
			        	e.setCancelled(true);  
			 }

	     // Your method stuff
	        final Player target = getNearest(p, 50.0);
	        //Then you need to make sure there was a player
	        
	     if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && p.getItemInHand().getType() == Material.COMPASS){
	        if(target == null) {
	          //Uh-oh, there was not a player in that range
	        	p.sendMessage(ChatColor.RED + "There are no players within a 50 block radius!");
	        	ItemMeta itemMeta3 = p.getInventory().getItemInHand().getItemMeta();
	        	
	        	itemMeta3.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Track Player's"));
	        	p.getInventory().getItemInHand().setItemMeta(itemMeta3);
	        }
	        else {
	          //There was a player so set the compass
		             p.setCompassTarget(target.getLocation());
		             //ItemStack compass = new ItemStack(Material.COMPASS, 1);
					 ItemMeta itemMeta2 = p.getInventory().getItemInHand().getItemMeta();
					 //final ItemMeta itemMeta3 = p.getInventory().getItemInHand().getItemMeta();

					 itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + " " + target.getName()));
					 p.getInventory().getItemInHand().setItemMeta(itemMeta2);
					 //itemMeta3.getDisplayName().replace("CraftPlayer{name=", "").replace("}", "");
					 //p.getInventory().getItemInHand().setItemMeta(itemMeta3);
					 Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
						    public void run() {
						    	 p.setCompassTarget(target.getLocation());
						    	 //itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + " " + target.getName()));
						    	 //p.getInventory().getItemInHand().setItemMeta(itemMeta2);
						    	 //itemMeta3.getDisplayName().replace("CraftPlayer{name=", "").replace("}", "");
						    }
						}, 1, 1);

	        }
	        
	        
	        }
	     if((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && p.getItemInHand().getType() == Material.GOLDEN_CARROT){
	    	
	    	 if (invisCooldown == false) {
	    		 p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 2), true);
	    	 }
	    	 
	    	 invisCooldown = true;
	    	 
	    	 invisPeriod.put("Invis", 30);
			  
			  invisTask.put("invis", new BukkitRunnable() {
				  
				    public void run() {
				    	
				    	if (invisPeriod.get("Invis") != null) {
							   
				    		invisPeriod.put("Invis", invisPeriod.get("Invis") - 1);
				    	
				    		if (invisPeriod.get("Invis") > 0) {
				    	
				    			p.sendMessage(ChatColor.RED + "You can not use this abillity for  " + (invisPeriod.get("Invis") + " more seconds!"));

				    		}
				    		
				    		if (invisPeriod.get("Invis") == 0) {
				    			
				    			invisCooldown = false;

				    			p.sendMessage(ChatColor.GREEN + "You can now use your Invis Ability!");
				    			invisTask.remove("invis");
				    			invisTask.clear();
				    			cancel();
				    			
				    			}
				    		}
				    	
				    }
				});
			  
			  invisTask.get("invis").runTaskTimer(this, 20, 20);
	    	 
	     }
	       
	    }
	 
	 
	@EventHandler
	public void onCancelFallDamage(EntityDamageEvent e) {
	    
		
		if(e.getEntity() instanceof Player) { //Checks to see if the entity that is taking damage is a player
	    	
			Player p = (Player) e.getEntity();
			
	        if(e.getCause() == DamageCause.SUFFOCATION) { 
	        	Location loc = p.getLocation().add(0 , 5 , 0);
	            p.teleport(loc);
	            
	            e.setCancelled(true);
	        	}
	        if (god == true) {
	        	e.setCancelled(true);
	        }
	    
	    
	    	}
	    }
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin (PlayerJoinEvent e) {
		
		final Player p = e.getPlayer();
		
		
		
		System.out.println("Teleporting to lobby");
		
		World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.lobby.world"));
		double x = settings.getData().getDouble("warps.lobby.x");
		double y = settings.getData().getDouble("warps.lobby.y");
		double z = settings.getData().getDouble("warps.lobby.z");
		p.teleport(new Location(w, x, y, z));
    
		
	}
	
	
	@EventHandler
	public void onLeave (PlayerQuitEvent e) {
		
		Player p = e.getPlayer();
		
		if (inArena2.contains(p)) {
			inArena2.remove(p);

		if (!(lobby == true)) {
			
			for (Player players : Bukkit.getOnlinePlayers()) {
				
				players.playSound(players.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 2F, 1F);
			}
			
			Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + " has left and has forfitted the match! There are " + inArena2.size() + " Players left.");
			
			if (inArena2.size() == 1) {
				
				Player playerWinner = inArena2.get(0).getPlayer();
				
				cooldownTime.remove("Countdown");
				Bukkit.broadcastMessage(ChatColor.GOLD + "We have our winner! " + inArena2.get(0).getPlayer().getDisplayName());
				
				World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.win.world"));
                double x = settings.getData().getDouble("warps.win.x");
                double y = settings.getData().getDouble("warps.win.y");
                double z = settings.getData().getDouble("warps.win.z");
                playerWinner.teleport(new Location(w, x, y, z));
                playerWinner.sendMessage(ChatColor.GREEN + "Teleported to winning area!");
                
                int currentWins = settings.getData().getInt("wins.player."+playerWinner.getUniqueId()+".wins");
                int nextWin = currentWins +1;
                
                settings.getData().set("wins.players."+playerWinner.getUniqueId(), playerWinner.getDisplayName()+": "+nextWin);
                settings.getData().set("wins.players."+playerWinner.getUniqueId()+".wins", nextWin);
    			settings.saveCfg();
                
                god = true;
                
                endGame();
                
               for (Player players : Bukkit.getOnlinePlayers()) {
                if (players != p.getPlayer()) {
                	World ww = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
			        double xx = settings.getData().getDouble("warps.waitarea.x");
			        double yy = settings.getData().getDouble("warps.waitarea.y");
			        double zz = settings.getData().getDouble("warps.waitarea.z");
			        players.teleport(new Location(ww, xx, yy, zz));
        			players.playSound(players.getLocation(), Sound.ENTITY_FIREWORK_BLAST, 2F, 1F);
                	}
                }
				
			}
			
			World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
	        double x = settings.getData().getDouble("warps.waitarea.x");
	        double y = settings.getData().getDouble("warps.waitarea.y");
	        double z = settings.getData().getDouble("warps.waitarea.z");
	        p.teleport(new Location(w, x, y, z));
			}
		}
		
		if (vampire.contains(p)) {
			vampire.remove(p);
			Bukkit.broadcastMessage(ChatColor.RED + "There are no Vampire's left, Selecting new vampire!!");
			if (vampire.size() < 1) {
				Player newVamp = player.get(0);
				player.remove(newVamp);
				inArena2.remove(newVamp);
				vampire.add(newVamp);
				playerToVamp(newVamp);
				newVamp.sendMessage(ChatColor.DARK_PURPLE+"You are the new Vampire!");
			}
			
		}
		if (zombie.contains(p)) {
			zombie.remove(p);
		}
		if (player.contains(p)) {
			player.remove(p);
		}
		
//	if (lobby != true) {
//		if (!(p.getName().equalsIgnoreCase("ABkayCkay") || p.getName().equalsIgnoreCase("MikeSN") || p.getName().equalsIgnoreCase("killzyazmadness") || p.getName().equalsIgnoreCase("Vegeta") || p.getName().equalsIgnoreCase("Hacko_Jacko") || p.getName().equalsIgnoreCase("PokeKhan") || p.getName().equalsIgnoreCase("lod77") || p.getName().equalsIgnoreCase("Pagub"))) {
//		
//			banList.add(p);
//		
//			settings.getBan().set("players", p.getName());
//        
//			settings.saveBan();
//
//			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + p.getName() + " Better luck next time!");
//		}
//	}
		
	if (inArena2.contains(p)) {
		inArena2.remove(p);
	//inArena2.remove(p); 
	}
	// do nothing
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void ItemDrop(PlayerDropItemEvent event){
            event.setCancelled(true);
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		
		Player p = (Player) e.getEntity();
		
		System.out.println("Triggered OnKill");
		
		if (inArena2.contains(p)) {
			System.out.println(p + " is in the arena, removing him");
			System.out.println(inArena2.size());
			inArena2.remove(p);
			System.out.println("Removed player from arena.");
			System.out.println(inArena2.size());

			Random random = new Random();
			int randomNum = random.nextInt(2) +1;
			
			System.out.println(randomNum);
			
		if (zombie.size() == 0) {
				zombie.add(p);
				
	            p.getInventory().clear();
	            p.getInventory().setArmorContents(null);
				
				//p.setHealth(20);

				ItemStack ZSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
			 	SkullMeta meta = (SkullMeta) ZSkull.getItemMeta();
			 	meta.setOwner("MHF_Zombie");
			 	meta.setDisplayName(ChatColor.DARK_GREEN + "Zombie " + p.getName());
			 	ZSkull.setItemMeta(meta);
				p.getInventory().setHelmet(ZSkull);
				
				ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
				meta2.setColor(Color.GREEN);
				chest.setItemMeta(meta2);
				p.getInventory().setChestplate(chest);
				
				ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
				 sword.addEnchantment(Enchantment.KNOCKBACK, 2);
				 sword.addEnchantment(Enchantment.DURABILITY, 1);
				 sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				 ItemMeta itemMeta4 = sword.getItemMeta();
				 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Knockback Wood Sword"));
				 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "ATTACK!"));
				 sword.setItemMeta(itemMeta4);
				 
				 if (!p.getInventory().contains(sword)) {
					 p.getInventory().addItem(sword);
					 }
				
				World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.zombie.world"));
		        double x = settings.getData().getDouble("warps.zombie.x");
		        double y = settings.getData().getDouble("warps.zombie.y");
		        double z = settings.getData().getDouble("warps.zombie.z");
		        p.teleport(new Location(w, x, y, z));
		        Bukkit.broadcastMessage(ChatColor.GREEN + p.getName() + " has been killed! And has become a Zombie!.");
		}
		else {
			if (randomNum == 1) {
				
				System.out.println("They are vampire");
				
				vampire.add(p);
				
				p.getInventory().clear();
	            p.getInventory().setArmorContents(null);
				
				ItemStack WSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
			 	SkullMeta meta = (SkullMeta) WSkull.getItemMeta();
			 	meta.setOwner("MHF_Enderman");
			 	meta.setDisplayName(ChatColor.DARK_PURPLE + "Vampire " + p.getName());
			 	WSkull.setItemMeta(meta);
				p.getInventory().setHelmet(WSkull);
				
				ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
				meta2.setColor(Color.BLACK);
				chest.setItemMeta(meta2);
				p.getInventory().setChestplate(chest);
				
				ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
				 sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
				 ItemMeta itemMeta4 = sword.getItemMeta();
				 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Sharpeness Stone Sword"));
				 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "Hunt for Blood!!"));
				 sword.setItemMeta(itemMeta4);
				 
				 ItemStack compass = new ItemStack(Material.COMPASS, 1);
				 ItemMeta itemMeta2 = compass.getItemMeta();
				 itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Track Player's"));
				 itemMeta2.setLore(Arrays.asList(ChatColor.GREEN + "Right click to track the nearest player to you!"));
				 compass.setItemMeta(itemMeta2);
				 
				 if (!p.getInventory().contains(compass)) {
					 p.getInventory().addItem(compass);
						
					 }
				 
				 if (!p.getInventory().contains(sword)) {
					 p.getInventory().addItem(sword);
					 }
				 
				 System.out.println("Gave Items");
				World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.vampire.world"));
		        double x = settings.getData().getDouble("warps.vampire.x");
		        double y = settings.getData().getDouble("warps.vampire.y");
		        double z = settings.getData().getDouble("warps.vampire.z");
		        p.teleport(new Location(w, x, y, z));
		        Bukkit.broadcastMessage(ChatColor.RED + p.getName() + " has been killed! And has become a Vampire!.");
		        
		        System.out.println("TP'd");
			}
			if (randomNum == 2) {
				zombie.add(p);
				
	            p.getInventory().clear();
	            p.getInventory().setArmorContents(null);
				
				//p.setHealth(20);

				ItemStack ZSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
			 	SkullMeta meta = (SkullMeta) ZSkull.getItemMeta();
			 	meta.setOwner("MHF_Zombie");
			 	meta.setDisplayName(ChatColor.DARK_GREEN + "Zombie " + p.getName());
			 	ZSkull.setItemMeta(meta);
				p.getInventory().setHelmet(ZSkull);
				
				ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
				meta2.setColor(Color.GREEN);
				chest.setItemMeta(meta2);
				p.getInventory().setChestplate(chest);
				
				ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
				 sword.addEnchantment(Enchantment.KNOCKBACK, 2);
				 sword.addEnchantment(Enchantment.DURABILITY, 1);
				 sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				 ItemMeta itemMeta4 = sword.getItemMeta();
				 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Knockback Wood Sword"));
				 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "ATTACK!"));
				 sword.setItemMeta(itemMeta4);
				 
				 if (!p.getInventory().contains(sword)) {
					 p.getInventory().addItem(sword);
					 }
				
				World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.zombie.world"));
		        double x = settings.getData().getDouble("warps.zombie.x");
		        double y = settings.getData().getDouble("warps.zombie.y");
		        double z = settings.getData().getDouble("warps.zombie.z");
		        p.teleport(new Location(w, x, y, z));
		        Bukkit.broadcastMessage(ChatColor.GREEN + p.getName() + " has been killed! And has become a Zombie!.");
			}
		}
			
			
			Bukkit.broadcastMessage(ChatColor.GOLD + "There are " + inArena2.size() + " Players left alive.");
			
			Location loc = e.getEntity().getLocation();

			 for (Player players : Bukkit.getOnlinePlayers()) {
					
					players.playSound(loc, Sound.ENTITY_LIGHTNING_THUNDER, 2F, 1F);
				}
			
	
	if (inArena2.size() == 1) {
		
		Player playerWinner = inArena2.get(0).getPlayer();
		
		cooldownTime.remove("Countdown");
		Bukkit.broadcastMessage(ChatColor.GOLD + "We have our winner! " + inArena2.get(0).getPlayer().getDisplayName());
		
		World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.win.world"));
        double x = settings.getData().getDouble("warps.win.x");
        double y = settings.getData().getDouble("warps.win.y");
        double z = settings.getData().getDouble("warps.win.z");
        playerWinner.teleport(new Location(w, x, y, z));
        playerWinner.sendMessage(ChatColor.GREEN + "Teleported to winning area!");
        
        int currentWins = settings.getData().getInt("wins.player."+playerWinner.getUniqueId()+".wins");
        int nextWin = currentWins +1;
        
        settings.getData().set("wins.players."+playerWinner.getUniqueId(), playerWinner.getDisplayName()+": "+nextWin);
        settings.getData().set("wins.players."+playerWinner.getUniqueId()+".wins", nextWin);
		settings.saveCfg();
        
        if (player.contains(playerWinner)) {
        	player.remove(playerWinner);
        }
        
        god = true;
        
        endGame();
        
       for (Player players : Bukkit.getOnlinePlayers()) {
        if (players != p.getPlayer()) {
        	World ww = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
	        double xx = settings.getData().getDouble("warps.waitarea.x");
	        double yy = settings.getData().getDouble("warps.waitarea.y");
	        double zz = settings.getData().getDouble("warps.waitarea.z");
	        players.teleport(new Location(ww, xx, yy, zz));
			players.playSound(players.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 2F, 1F);
        	}
        }
       
       vampire.clear();
   	zombie.clear();
		
	}
	
	if (e.getEntity().getKiller() != null) {
		
		if (e.getEntity().getKiller().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER) {

			Player pkilled = (Player) e.getEntity();
			Player pkiller = (Player) e.getEntity().getKiller();	
	
	Player killerName = e.getEntity().getKiller();
	
	if (killerName instanceof Player ) {	
		Bukkit.broadcastMessage(ChatColor.GREEN + pkilled.getName() + ChatColor.GOLD + " has been slain by " + ChatColor.RED + pkiller.getName() + ChatColor.GOLD + " with a " + killerName.getInventory().getItemInHand().getType());
	//Bukkit.broadcastMessage(ChatColor.GOLD + "There are " + inArena2.size() + " Players left alive.");
	
	}
		}
		else {
			//do nothing
		}
		
	}
	if (player.contains(p)) {
    	player.remove(p);
    }
	
		}
		else {
			Player pkilled = (Player) e.getEntity();
			
			System.out.println("Got to Vampire and Zombie Check");	
			
			if (vampire.contains(p)) {
	            p.getInventory().clear();
	            pkilled.getInventory().setArmorContents(null);
	            p.getInventory().setHelmet(null);
	            
				//p.setHealth(20);
				
				System.out.println("Spawned Vampire");	
				System.out.println(p.getName() + pkilled.getName());	
				
				ItemStack WSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
			 	SkullMeta meta = (SkullMeta) WSkull.getItemMeta();
			 	meta.setOwner("MHF_Enderman");
			 	meta.setDisplayName(ChatColor.DARK_PURPLE + "Vampire " + p.getName());
			 	WSkull.setItemMeta(meta);
				p.getInventory().setHelmet(WSkull);
				
				ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
				meta2.setColor(Color.BLACK);
				chest.setItemMeta(meta2);
				p.getInventory().setChestplate(chest);
				
				ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
				 sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
				 ItemMeta itemMeta4 = sword.getItemMeta();
				 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Sharpeness Stone Sword"));
				 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "Hunt for Blood!!"));
				 sword.setItemMeta(itemMeta4);
				 
				 ItemStack compass = new ItemStack(Material.COMPASS, 1);
				 ItemMeta itemMeta2 = compass.getItemMeta();
				 itemMeta2.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Track Player's"));
				 itemMeta2.setLore(Arrays.asList(ChatColor.GREEN + "Right click to track the nearest player to you!"));
				 compass.setItemMeta(itemMeta2);
				 
				 if (!p.getInventory().contains(compass)) {
					 p.getInventory().addItem(compass);
						
					 }

				 if (!p.getInventory().contains(sword)) {
					 p.getInventory().addItem(sword);
					 }
				
				 System.out.println("Gave Vampire Items");	
				
				World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.vampire.world"));
		        double x = settings.getData().getDouble("warps.vampire.x");
		        double y = settings.getData().getDouble("warps.vampire.y");
		        double z = settings.getData().getDouble("warps.vampire.z");
		        p.teleport(new Location(w, x, y, z));
			}
			if (zombie.contains(p)) {
				
				System.out.println("Died player is Zombie");
				
	            p.getInventory().clear();
	            p.getInventory().setArmorContents(null);
				
				ItemStack ZSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 2);
			 	SkullMeta meta = (SkullMeta) ZSkull.getItemMeta();
			 	meta.setOwner("MHF_Zombie");
			 	meta.setDisplayName(ChatColor.DARK_GREEN + "Zombie " + p.getName());
			 	ZSkull.setItemMeta(meta);
				p.getInventory().setHelmet(ZSkull);
				
				ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
				LeatherArmorMeta meta2 = (LeatherArmorMeta) chest.getItemMeta();
				meta2.setColor(Color.GREEN);
				chest.setItemMeta(meta2);
				p.getInventory().setChestplate(chest);
				
				ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
				 sword.addEnchantment(Enchantment.KNOCKBACK, 2);
				 sword.addEnchantment(Enchantment.DURABILITY, 1);
				 sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				 ItemMeta itemMeta4 = sword.getItemMeta();
				 itemMeta4.setDisplayName(ChatColor.BLUE + (ChatColor.BOLD + "Knockback Wood Sword"));
				 itemMeta4.setLore(Arrays.asList(ChatColor.GREEN + "ATTACK!"));
				 sword.setItemMeta(itemMeta4);
				 
				 if (!p.getInventory().contains(sword)) {
					 p.getInventory().addItem(sword);
					 }
				 
				 System.out.println("Gave Zombie Items");
	            
				World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.zombie.world"));
		        double x = settings.getData().getDouble("warps.zombie.x");
		        double y = settings.getData().getDouble("warps.zombie.y");
		        double z = settings.getData().getDouble("warps.zombie.z");
		        p.teleport(new Location(w, x, y, z));
			}
			
			Player pkiller = (Player) e.getEntity().getKiller();
			if (player.contains(pkiller)) {
				System.out.println("Ranked Up player");
				rankUpPlayer(pkiller);	
			}
		}
		
			}
	
	 @EventHandler
	    public void onTestEntityDamage(EntityDamageByEntityEvent event) {
	        
		 if (event.getDamager() instanceof Player){
	            if (event.getEntity() instanceof Player) {
	            	
		            Player pDamager = (Player) event.getDamager();
		            Player pHit = (Player) event.getEntity();
		            
		           
		            
		            if (player.contains(pDamager) && player.contains(pHit)) {
		            	event.setCancelled(true);
		            }
		            if (zombie.contains(pDamager) && zombie.contains(pHit)) {
		            	event.setCancelled(true);
		            }
		            if (vampire.contains(pDamager) && vampire.contains(pHit)) {
		            	event.setCancelled(true);
		            }
		            if (zombie.contains(pDamager) && vampire.contains(pHit)) {
		            	event.setCancelled(true);
		            }
		            if (vampire.contains(pDamager) && zombie.contains(pHit)) {
		            	event.setCancelled(true);
		            }
		            
		            if(event.getCause().equals(DamageCause.PROJECTILE)){
			        	if (player.contains(pHit)) {
			        		event.setCancelled(true);
			        	}
	                
	            }
	        }

	        	}
	        	
	       }
	   
	@Override
	 public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
		  final Player p = (Player)sender;
		  
		  if (cmd.getName().equalsIgnoreCase("hunt")) {
			  if (args.length == 0) {
				  if (p.hasPermission("boat.admin")) {
					  p.sendMessage(ChatColor.GREEN + "/hunt setwarp <lobby, waitarea, vampire, zombie, win, player> - Sets the waiting area location");
					  p.sendMessage(ChatColor.GREEN + "/hunt start - Stats the game by teleporting players to the random locations");
					  p.sendMessage(ChatColor.GREEN + "/hunt inv - Enables Inv Sorting for Vampires and Zombies.");
					  //p.sendMessage(ChatColor.GREEN + "/boat fillslots - Allows any players to join the arena without the permission");
				  }
				  else {
				  p.sendMessage(ChatColor.GREEN + "/hunt join");
				  }
			  	}
			  else if (args.length == 1) {
				  
				  if (args[0].equalsIgnoreCase("inv")) {
					  if (p.hasPermission("hunt.admin")) {
						  enableInv = true;
						  
					  }
				  }
				  
				  if (args[0].equalsIgnoreCase("admincheck")) {
					  if (p.hasPermission("hunt.admin")) {
						  
						  boolean whitelist = Bukkit.hasWhitelist();
						  
						  p.sendMessage("Zombie List Size: " + zombie.size());
						  p.sendMessage("Vampire List Size: " + vampire.size());
						  p.sendMessage("Player List Size: " + player.size());
						  p.sendMessage("Whitelist: " + whitelist);
						  p.sendMessage("God = " + god);
						  p.sendMessage("Game Started = " + gameStarted);
					  }
				  }
				  
				  if (args[0].equalsIgnoreCase("random")) {
					  Random random = new Random();
						int randomNum = random.nextInt(2) +1;
						p.sendMessage("Random is: " + randomNum);
				  }
				  
				  if (args[0].equalsIgnoreCase("reset")) {
					  if (p.hasPermission("hunt.admin")) {
						  endGame();
					  }
				  }
				  
				  if (args[0].equalsIgnoreCase("gamestarted")) {
					  p.sendMessage(ChatColor.GOLD + " " + gameStarted);
				  }
				  
				  if (args[0].equalsIgnoreCase("join")) {
					 if (!inArena2.contains(p)) {
						 Random random = new Random();
							int randomNum = random.nextInt(2) +1;
							System.out.println("If value = 2 then they are vampire: "+randomNum);
							
							if (randomNum == 2 && vampire.size() < 1) {
								vampire.add(p);
								p.sendMessage(ChatColor.DARK_PURPLE + "You are the Vampire!");
								Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "A Vampire has joined the game!");
							}
							
							if (p.getName().equalsIgnoreCase("ABkayCkay")) {
								Bukkit.broadcastMessage(ChatColor.GREEN + "The Creator of Vampire Hunt," + ChatColor.BOLD+ " ABkayCkay" + ChatColor.RESET+ ChatColor.GREEN +" has joined the game");
							}
							
							if (!vampire.contains(p)) {

								if (lobby = true) {
								//MikeSN
								//killzyazmadness
								//Hacko_Jacko
								//Pagub
								//Vegeta
								//p.getName().equalsIgnoreCase("ABkayCkay") 
								
								inArena2.add(p);
								player.add(p);
								
								p.setFoodLevel(20);
								//p.setHealth(10);
								
								p.setGameMode(GameMode.SURVIVAL);
								
								p.getInventory().clear();
					            p.getInventory().setArmorContents(null);
							
								p.sendMessage(ChatColor.GREEN + "You have sucesfully joined the arena, teleporting to waiting area.");
								
								
							 if (inArena2.size() == 25) {
								 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
							 }
							 else if (inArena2.size() == 50) {
								 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
							 }
							 else if (inArena2.size() == 75) {
								 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
							 }
							 else if (inArena2.size() == 100) {
								 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
							 }
							 else if (inArena2.size() == 125) {
								 Bukkit.broadcastMessage(ChatColor.GOLD + "There are now " + inArena2.size() + " players in the arena waiting for the game to start.");
							 }

					          Bukkit.broadcastMessage(ChatColor.BLUE + "A challenger has joined the arena (" + p.getName() + "), there are now " + inArena2.size() + " People in the arena.");
							 		
							}
								else {
									p.setGameMode(GameMode.SPECTATOR);
									World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.spectator.world"));
							        double x = settings.getData().getDouble("warps.spectator.x");
							        double y = settings.getData().getDouble("warps.spectator.y");
							        double z = settings.getData().getDouble("warps.spectator.z");
							        p.teleport(new Location(w, x, y, z));
							        
								}

							}
							
							if (lobby = true) {
								System.out.println("Teleporting to waitarea");
								
								World w = Bukkit.getServer().getWorld(settings.getData().getString("warps.waitarea.world"));
								double x = settings.getData().getDouble("warps.waitarea.x");
								double y = settings.getData().getDouble("warps.waitarea.y");
								double z = settings.getData().getDouble("warps.waitarea.z");
								p.teleport(new Location(w, x, y, z));
					        
								Location loc = new Location(w, x, y, z);
					        
								//p.setBedSpawnLocation(loc, true);
					        
								System.out.println(loc);
								System.out.println("Teleported to " + loc);
							
							}
				  }
			  }
				  if (args[0].equalsIgnoreCase("fillslots")) {
					  allowAll = true;
					  p.sendMessage(ChatColor.GREEN + "Allowed all players to join arena");
				  }
				  
				  if (args[0].equalsIgnoreCase("start")) {
					  if (p.hasPermission("arena.admin")) {
						startCommand();    
					  }  
  
				  }
			  }
			  else if (args.length == 2) {
				  if ((args[0].equalsIgnoreCase("setwarp"))) {
						 if (p.hasPermission("arena.admin")) {
							if (settings.getData().get("warps." +args[1]) != (null)) {
								p.sendMessage(ChatColor.RED + args[1] + " warp already exists. If you want to overwrite it, do /arena delwarp "+args[1] + ". And then re-set the new warp.");
							}
							else {
			        	 	
			        		settings.getData().set("warps." + args[1] + ".world", p.getLocation().getWorld().getName());
			                settings.getData().set("warps." + args[1] + ".x", p.getLocation().getX());
			                settings.getData().set("warps." + args[1] + ".y", p.getLocation().getY());
			                settings.getData().set("warps." + args[1] + ".z", p.getLocation().getZ());
			                settings.saveData();
			                p.sendMessage(ChatColor.GREEN + "Set warp " + args[1] + "!");
							}	
			        	  }
						 else {
							 p.sendMessage(ChatColor.RED + "You do not have permission to set a warp!");
						 }
						}
			        	
			        	if ((args[0].equalsIgnoreCase("delwarp"))) {
			        	  if (p.hasPermission("arena.admin")) {
			        		if (settings.getData().getConfigurationSection("warps." + args[1]) == null) {
			                    p.sendMessage(ChatColor.RED + "Warp " + args[1] + " does not exist!");
			                    return true;
			            }
			        		settings.getData().set("warps." + args[1], null);
			                settings.saveData();
			                p.sendMessage(ChatColor.GREEN + "Removed warp " + args[1] + "!");
			        	  }
			        	  else {
								 p.sendMessage(ChatColor.RED + "You do not have permission to delete a warp!");
							 }
			        	}
			        	
			        	if ((args[0].equalsIgnoreCase("warp"))) {
			        	  if (p.hasPermission("arena.admin")) {
			        		if (settings.getData().getConfigurationSection("warps." + args[1]) == null) {
			                    p.sendMessage(ChatColor.RED + "Warp " + args[1] + " does not exist!");
			                    return true;
			            }
			        		World w = Bukkit.getServer().getWorld(settings.getData().getString("warps." + args[1] + ".world"));
			                double x = settings.getData().getDouble("warps." + args[1] + ".x");
			                double y = settings.getData().getDouble("warps." + args[1] + ".y");
			                double z = settings.getData().getDouble("warps." + args[1] + ".z");
			                p.teleport(new Location(w, x, y, z));
			                p.sendMessage(ChatColor.GREEN + "Teleported to " + args[1] + "!");
			        		
			        	  }
			        	  else {
			        		  p.sendMessage(ChatColor.RED + "You do not have permission to warp to a spawn location!");
			        	  }
			        	}
			  }
		     }
		 
		  
		  return false;
	 }
	
    }
	
