package com.github.ringoame196_s_mcPlugin

object DataManager {
    fun getBackStepCount(uuid: String): Int = Data.backStepCount[uuid] ?: 0
    fun incrementBackStepCount(uuid: String) {
        Data.backStepCount[uuid] = getBackStepCount(uuid) + 1
    }

    fun resetBackStepCount(uuid: String) {
        Data.backStepCount[uuid] = 0
    }

    fun getCoolTime(uuid: String): Int = Data.coolTime[uuid] ?: 0
    fun setCoolTime(uuid: String, value: Int) {
        Data.coolTime[uuid] = value
    }
}
