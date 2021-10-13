package com.williamfzc.callreco.core.instr

@kotlinx.serialization.Serializable
data class CrClazzInfo(
        val clazzId: Long,
        val clazzName: String,
        val methodList: List<CrMethodInfo>
)

@kotlinx.serialization.Serializable
data class CrMethodInfo(
        val name: String,
        val desc: String,
        val signature: String
)
