package org.waraccademy.playtime.database;

import com.glyart.mystral.data.ClassMapper;
import com.glyart.mystral.database.AsyncDatabase;
import com.glyart.mystral.database.Credentials;
import com.glyart.mystral.database.Mystral;
import com.glyart.mystral.datasource.DataSourceFactory;
import com.glyart.mystral.datasource.HikariFactory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.intellij.lang.annotations.Language;
import org.waraccademy.playtime.PlayTime;
import org.waraccademy.playtime.services.TimePlayer;

import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;

import static java.sql.Types.VARCHAR;

public class DatabaseManager {
    private AsyncDatabase database;
    private final YamlConfiguration config;

    public DatabaseManager(YamlConfiguration config) {
        this.config = config;
        setupDatabase();
    }

    private void setupDatabase() {
        Credentials credentials = Credentials.builder()
                .host(config.getString("database.hostname"))
                .user(config.getString("database.username"))
                .password(config.getString("database.password"))
                .schema(config.getString("database.databaseName"))
                .pool("playtime")
                .build();

        HikariFactory factory = new HikariFactory();
        factory.setCredentials(credentials);
        factory.setProperty("useSSL",false);
        database = Mystral.newAsyncDatabase(factory, (command) -> Bukkit.getScheduler().runTaskAsynchronously(PlayTime.getInstance(), command));


    }

    public CompletableFuture<Integer> setupTable(){
        return database.update("CREATE TABLE IF NOT EXISTS playtime(id VARCHAR(16) NOT NULL," +
                "                        lastSeen TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "                        day BIGINT NOT NULL DEFAULT 0," +
                "                        week BIGINT NOT NULL DEFAULT 0," +
                "                        month BIGINT NOT NULL DEFAULT 0)", false);
    }

    public CompletableFuture<TimePlayer> getPlayer(String name){
        return database.queryForObjectOrElseGet("SELECT * FROM playtime WHERE id='"+name+"';", (resultSet, rowNumber) -> {
            TimePlayer player = new TimePlayer();
            player.setDay(resultSet.getLong("day"));
            player.setWeek(resultSet.getLong("week"));
            player.setMonth(resultSet.getLong("month"));
            player.setLastSeen(resultSet.getTimestamp("lastSeen"));

            return player;
        },() -> null);
    }
    public CompletableFuture<Integer> insertPlayer(String name){
        return database.update("INSERT INTO playtime(id) VALUES('"+name+"');",false);
    }

    public CompletableFuture<Boolean> playerExists(String player){
        return database.query("SELECT * FROM playtime WHERE id = ?;", new Object[]{player}, ResultSet::next, VARCHAR);
    }

    public CompletableFuture<Integer> updateLastSeen(String name){
        return database.update("UPDATE playtime SET lastSeen = CURRENT_TIMESTAMP WHERE id= '" + name + "';", false);
    }

    public CompletableFuture<Void> updatePlayer(String name, long amount){
        @Language("MySQL")
        String query = String.format("UPDATE playtime SET day=day + %1$d, week = week + %1$d, month = month + %1$d WHERE id='%2$s'",amount,name);

        return database.update(query,false).thenRun(() -> updateLastSeen(name));
    }

    public CompletableFuture<Integer> resetField(String field){
        @Language("MySQL")
        String query = String.format("UPDATE playtime SET %1$s=0",field);

        return database.update(query,false);
    }


    public AsyncDatabase getDatabase() {
        return database;
    }
}