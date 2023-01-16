package org.waraccademy.playtime.task;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.waraccademy.playtime.PlayTime;
import org.waraccademy.playtime.database.DatabaseManager;
import org.waraccademy.playtime.utils.Utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class ResetTask implements Runnable{
    private final ConfigurationSection section;
    private final DatabaseManager manager = PlayTime.getInstance().getManager();

    public ResetTask(){
        YamlConfiguration config = PlayTime.getInstance().getConfig();

        section = config.getConfigurationSection("reset") == null ? config.createSection("reset") : config.getConfigurationSection("reset");
    }

    @Override
    public void run() {
        LocalDateTime date = LocalDateTime.now();

        LocalDateTime lastDayReset = Utils.deserializeDate(section.getString("day",""));
        LocalDateTime lastWeekReset = Utils.deserializeDate(section.getString("week", ""));
        LocalDateTime lastMonthReset = Utils.deserializeDate(section.getString("month", ""));

        if(date.getDayOfYear() != lastDayReset.getDayOfYear()){
            PlayTime.getInstance().getService().resetPlayers();

            manager.resetField("day");
            section.set("day", Utils.serializeDate(date));

            PlayTime.getInstance().saveConfig();
        }

        if(date.getDayOfYear() != lastWeekReset.getDayOfYear() && date.getDayOfWeek() == DayOfWeek.MONDAY){
            PlayTime.getInstance().getService().resetPlayers();

            manager.resetField("week");
            section.set("week", Utils.serializeDate(date));

            PlayTime.getInstance().saveConfig();
        }

        if(date.getDayOfYear() != lastMonthReset.getDayOfYear() && date.getDayOfMonth() == 1){
            PlayTime.getInstance().getService().resetPlayers();

            manager.resetField("month");
            section.set("month",Utils.serializeDate(date));

            PlayTime.getInstance().saveConfig();
        }

    }
}
