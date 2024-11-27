package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.events.BackStep
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

object CoolTimeTask : BukkitRunnable() {
    private val backStep = BackStep()
    override fun run() {
        val iterator = Data.coolTime.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val uuid = entry.key
            val remaining = entry.value - 1
            if (remaining <= 0) {
                DataManager.resetBackStepCount(uuid)
                iterator.remove()
                val player = Bukkit.getPlayer(UUID.fromString(uuid)) ?: return
                backStep.displayBackStepCount(player)
            } else {
                DataManager.setCoolTime(uuid, remaining)
            }
        }
    }
}
