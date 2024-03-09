package com.github.hynj01.hyperhard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Hyperhard extends JavaPlugin {
    @Override
    public void onEnable() {
        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(Difficulty.HARD);

            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, false);
            world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false);
            world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, false);
        }

        new BukkitRunnable() {
            private final long time = System.currentTimeMillis();

            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - time;
                long removeEntityTime = 1000L * 60L * 30L;

                if (elapsedTime >= removeEntityTime) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(Component.text("버러진 아이템이 곧 삭제됩니다."));
                    }

                    for (World world: Bukkit.getWorlds()) {
                        for (Entity entity : world.getEntities()) {
                            if (entity instanceof Item) {
                                entity.remove();
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendMessage(Component.text("버러진 아이템이 삭제되었습니다."));
                                }
                            }
                        }
                    }
                } else if (elapsedTime >= removeEntityTime - 60000L) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(Component.text("1분 뒤 버러진 아이템이 제거됩니다."));
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L * 60L);

        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        for (World world : Bukkit.getWorlds()) {
            world.save();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.saveData();
        }
    }
}
