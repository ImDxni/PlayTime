package org.waraccademy.playtime.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.waraccademy.playtime.PlayTime;
import org.waraccademy.playtime.database.DatabaseManager;
import org.waraccademy.playtime.services.PlayerService;
import org.waraccademy.playtime.utils.Utils;

import static org.waraccademy.playtime.utils.Utils.color;
import static org.waraccademy.playtime.utils.Utils.convertMillis;

public class Tempo implements CommandExecutor {
    private final YamlConfiguration config = PlayTime.getInstance().getConfig();
    private final PlayerService service = PlayTime.getInstance().getService();
    private final DatabaseManager manager = PlayTime.getInstance().getManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return true;

        StringBuilder builder = new StringBuilder();
        String path = "messages";
        String target;
        if(args.length == 0){
            target = sender.getName();
            path += ".self";
        } else {
            target = args[0];
            if(sender.hasPermission("metropolis.tempo.admin"))
                path += ".admin";
            else if(sender.hasPermission("metropolis.tempo.player"))
                path += ".player";
        }

        if(path.equals("messages"))
            return true;

        final String finalPath = path;
        manager.getPlayer(target).whenComplete((result,error) -> {
            if(error != null){
                error.printStackTrace();
                return;
            }

            if(result == null){
                sender.sendMessage(color(config.getString("messages.player-not-exists")));
                return;
            }


            Player p = Bukkit.getPlayer(target);
            String lastSeen = convertMillis(result.getLastSeen());
            long day = 0,month = 0, week = 0;
            if(p != null && p.isOnline()){
                lastSeen = color(config.getString("messages.online"));
                day += service.getTime(target);

                month += day;
                week += day;
            }

            day += result.getDay();
            month += result.getMonth();
            week += result.getWeek();

            for (String line : config.getStringList(finalPath)) {
                builder.append(color(line.replace("%player%",target)
                        .replace("%day%",convertMillis(day))
                        .replace("%week%",convertMillis(week))
                        .replace("%month%",convertMillis(month))
                        .replace("%lastSeen%",lastSeen)))
                        .append("\n");
            }

            sender.sendMessage(builder.toString());
        });

        return true;
    }
}
