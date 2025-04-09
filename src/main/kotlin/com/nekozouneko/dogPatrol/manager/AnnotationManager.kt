package com.nekozouneko.dogPatrol.manager

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.models.Checks
import kotlin.reflect.KClass

class AnnotationManager {
    private val annotations: MutableMap<KClass<out CheckManager.CheckHandler>, DogPatrol.CheckInfo?> = mutableMapOf()
    fun getAnnotation(clazz: KClass<out CheckManager.CheckHandler>) : DogPatrol.CheckInfo?{ return annotations[clazz] }
    fun initialize(){
        val checks = Checks.get()
        for(check in checks) {
            val kFunction = check::class.members.find { it.name == "handle" }
            annotations[check::class] = kFunction?.annotations?.find { it is DogPatrol.CheckInfo } as? DogPatrol.CheckInfo
        }
    }
}