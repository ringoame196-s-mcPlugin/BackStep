package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.Data
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class BackStep(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onBackStepMove(e: PlayerMoveEvent) {
        val player = e.player
        val playerUUID = player.uniqueId.toString()
        val from = e.from
        val to = e.to ?: return
        if (player.isSneaking) return
        if (from.x == to.x && from.z == to.z) return
        // プレイヤーの移動方向ベクトル
        val moveX = to.x - from.x
        val moveY = to.y - from.y
        val moveZ = to.z - from.z

        // プレイヤーの向いている方向 (ヨー角度をラジアンに変換)
        val yaw = Math.toRadians(player.location.yaw.toDouble())
        val directionX = -sin(yaw)
        val directionZ = cos(yaw)

        // 内積を計算して、進行方向が向いている方向に対してどのくらい一致しているかを確認
        val dotProduct = (moveX * directionX) + (moveZ * directionZ)

        if (dotProduct < -0.1 && moveY > 0.4 && moveY < 0.5) {
            val backStemCount = Data.backStepCount[playerUUID] ?: 0

            if (backStemCount < Data.MAX) {
                Data.backStepCount[playerUUID] = backStemCount + 1
                backStep(player, yaw)
                val coolTimeCount = Data.coolTime[playerUUID] ?: 0

                Data.coolTime[playerUUID] = Data.COOL_TIME
                // クールタイム回復
                if (coolTimeCount == 0) {
                    startCoolTime(player)
                }
            }
            displayBackStepCount(player)
        }
    }

    private fun backStep(player: Player, yaw: Double) {
        val knockBack = Data.KNOCK_BACK
        // プレイヤーが向いている方向の逆方向を計算
        val backwardVector = Vector(sin(yaw), 0.3, -cos(yaw)).normalize().multiply(knockBack)
        // プレイヤーをノックバック
        player.velocity = backwardVector
    }

    private fun displayBackStepCount(player: Player) {
        val playerUUID = player.uniqueId.toString()
        val backStemCount = Data.backStepCount[playerUUID] ?: 0
        val message = "${ChatColor.YELLOW}[バックステップ] 残り${Data.MAX - backStemCount}回"
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }

    private fun startCoolTime(player: Player) {
        val playerUUID = player.uniqueId.toString()
        object : BukkitRunnable() {
            override fun run() {
                var coolTimeCount = Data.coolTime[playerUUID] ?: 0
                coolTimeCount--
                Data.coolTime[playerUUID] = coolTimeCount

                if (coolTimeCount == 0) {
                    Data.backStepCount[playerUUID] = 0
                    displayBackStepCount(player)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L) // 1秒間隔 (20 ticks) でタスクを実行
    }
}
