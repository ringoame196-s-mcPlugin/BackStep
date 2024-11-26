package com.github.ringoame196_s_mcPlugin.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class EntityDamageEvent : Listener {
    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent) {
        // 落下ダメを無効化
        if (e.cause == EntityDamageEvent.DamageCause.FALL) e.isCancelled = true
    }
}
