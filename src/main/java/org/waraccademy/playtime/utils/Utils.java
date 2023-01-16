package org.waraccademy.playtime.utils;

import org.bukkit.ChatColor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public static String serializeDate(LocalDateTime date){
        return formatter.format(date);
    }

    public static LocalDateTime deserializeDate(String date){
        if(date.isEmpty())
            return LocalDateTime.now().minusDays(1);

        return LocalDate.parse(date,formatter).atStartOfDay();
    }

    public static String convertMillis(long millis){
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return(hours +
                " h " +
                minutes +
                " m " +
                seconds +
                " s");
    }

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }
}
