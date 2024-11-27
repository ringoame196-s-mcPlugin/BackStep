package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.Data
import com.github.ringoame196_s_mcPlugin.DataManager
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class BackStep : Listener {
    private val max = Data.MAX
    private val coolTime = Data.COOL_TIME
    private val knockBack = Data.KNOCK_BACK

    @EventHandler
    fun onBackStepMove(e: PlayerMoveEvent) {
        val player = e.player
        val to = e.to ?: return
        if (player.isSneaking || e.from.x == to.x && e.from.z == to.z) return

        val moveX = to.x - e.from.x
        val moveY = to.y - e.from.y
        val moveZ = to.z - e.from.z

        val yaw = Math.toRadians(player.location.yaw.toDouble())
        val directionX = -sin(yaw)
        val directionZ = cos(yaw)
        val dotProduct = moveX * directionX + moveZ * directionZ

        if (dotProduct < -0.1 && moveY in 0.4..0.5) {
            val uuid = player.uniqueId.toString()
            val backStepCount = DataManager.getBackStepCount(uuid)

            if (backStepCount < max) {
                DataManager.incrementBackStepCount(uuid)
                backStep(player, yaw)
                DataManager.setCoolTime(uuid, coolTime)
                displayBackStepCount(player)
            }
        }
    }

    private fun backStep(player: Player, yaw: Double) {
        // プレイヤーが向いている方向の逆方向を計算
        val backwardVector = Vector(sin(yaw), 0.3, -cos(yaw)).normalize().multiply(knockBack)
        // プレイヤーをノックバック
        player.velocity = backwardVector
    }

    fun displayBackStepCount(player: Player) {
        val playerUUID = player.uniqueId.toString()
        val backStemCount = Data.backStepCount[playerUUID] ?: 0
        val message = "${ChatColor.YELLOW}[バックステップ] 残り${max - backStemCount}回"
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }
}
