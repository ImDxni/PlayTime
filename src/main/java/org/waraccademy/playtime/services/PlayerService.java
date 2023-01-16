package org.waraccademy.playtime.services;


import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.waraccademy.playtime.PlayTime;
import org.waraccademy.playtime.database.DatabaseManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PlayerService implements Listener {
    private final Map<String,Long> playerJoins = new HashMap<>();
    private final DatabaseManager manager = PlayTime.getInstance().getManager();

    public PlayerService(){
        Bukkit.getOnlinePlayers().forEach(p -> playerJoins.put(p.getName(),System.currentTimeMillis()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        String name = e.getPlayer().getName();

        manager.playerExists(name).thenCompose(exists -> {
            if(!exists){
                return manager.insertPlayer(name);
            }
            return CompletableFuture.completedFuture(1);
        });

        playerJoins.put(e.getPlayer().getName(),System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        updatePlayer(e.getPlayer().getName());
        playerJoins.remove(e.getPlayer().getName());
    }

    public long getTime(String player){
        return System.currentTimeMillis() - playerJoins.get(player);
    }

    public void resetPlayers(){
        //Brutto ma funzionante
        playerJoins.replaceAll((k,v) -> {
            updatePlayer(k);
            return System.currentTimeMillis();
        });
    }


    private void updatePlayer(String player){
        long time = System.currentTimeMillis() - playerJoins.get(player);

        PlayTime.getInstance().getManager().updatePlayer(player, time);
    }
}
