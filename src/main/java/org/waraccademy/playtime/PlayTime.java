package org.waraccademy.playtime;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.waraccademy.playtime.commands.Tempo;
import org.waraccademy.playtime.configuration.FileManager;
import org.waraccademy.playtime.database.DatabaseManager;
import org.waraccademy.playtime.services.PlayerService;
import org.waraccademy.playtime.task.ResetTask;

import java.io.File;
import java.io.IOException;

public final class PlayTime extends JavaPlugin {
    private static PlayTime instance;

    private YamlConfiguration config;
    private File configFile;
    private DatabaseManager manager;

    private PlayerService service;

    @Override
    public void onEnable() {
        instance = this;

        FileManager configManager = new FileManager("config",this);
        configManager.saveDefault();

        configFile = configManager.getFile();

        config = configManager.getConfig();

        manager = new DatabaseManager(config);

        manager.setupTable();


        service = new PlayerService();

        Bukkit.getPluginManager().registerEvents(service,this);

        Bukkit.getScheduler().runTaskTimer(this,new ResetTask(),0,1200);

        getCommand("tempo").setExecutor(new Tempo());
    }

    public static PlayTime getInstance() {
        return instance;
    }

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }

    public DatabaseManager getManager() {
        return manager;
    }

    public PlayerService getService() {
        return service;
    }

    public void saveConfig(){
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
