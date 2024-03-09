package com.github.hynj01.hyperhard;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EventListener implements Listener {
    private final Random random = new Random();

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setHealthScale(player.getHealth() / 2);
        }

        event.joinMessage(null);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPaperServerListPing(PaperServerListPingEvent event) {
        event.setHidePlayers(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.deathMessage(null);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            event.allow();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Monster) {
            double baseHealth = event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHealth * 5);

            double baseAttackDam = event.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
            event.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(baseAttackDam * 5);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        Creeper entity = (Creeper) event.getEntity();
        entity.setExplosionRadius(entity.getExplosionRadius() * 10);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        double airDropChance = 0.005;
        if (random.nextDouble() < airDropChance) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                double damageDoubleChance = 0.02;
                if (random.nextDouble() < damageDoubleChance) {
                    event.setDamage(event.getDamage() * 2);
                }
            }
        }
    }
}
