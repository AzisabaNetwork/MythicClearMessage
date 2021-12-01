package com.github.sirokuri_.MythicClearMessage;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;


public final class MythicClearMessage extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("com/github/sirokuri_/MythicClearMessage") || cmd.getName().equalsIgnoreCase("mcm")) {
            //プレイヤー以外の入力阻止
            if (!(sender instanceof Player)) return false;
            if (args.length <= 0) {
                return true;
            }
            //コンフィグリロードのコマンド
            if (args[0].equalsIgnoreCase("reload")) {
                //OP以外起動しないように設定
                if (sender.hasPermission("MythicClearMessage.permission.Admin")) {
                    reloadConfig();
                    sender.sendMessage("configリロードしました");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @SuppressWarnings({"deprecation"})
    @EventHandler
    public void onEntity(EntityDeathEvent e){
        Entity entity = e.getEntity();
        Entity killer = e.getEntity().getKiller();
        String entityName = entity.getName();
        if(killer == null) return;
        for(String key : getConfig().getConfigurationSection("Mob").getKeys(false)) {
            String Name = getConfig().getString("Mob." + key + ".Name");
            if(Name == null) return;
            if(entityName.equals(ChatColor.translateAlternateColorCodes('&',"" + Name))) {
                String text1 = getConfig().getString("Mob." + key + ".text1");
                String text2 = getConfig().getString("Mob." + key + ".text2");
                if (text1 == null || text2 == null) return;
                BaseComponent[] hover = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', text2)).create();
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
                BaseComponent[] message = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', killer.getName() + "は" + text1)).event(hoverEvent).create();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.spigot().sendMessage(message);
                }
            }
        }
    }
}
