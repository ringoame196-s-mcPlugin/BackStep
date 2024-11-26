package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.events.BackStep
import com.github.ringoame196_s_mcPlugin.events.EntityDamageEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    override fun onEnable() {
        super.onEnable()
        val plugin = this
        server.pluginManager.registerEvents(BackStep(plugin), plugin)
        server.pluginManager.registerEvents(EntityDamageEvent(), plugin)
    }
}
