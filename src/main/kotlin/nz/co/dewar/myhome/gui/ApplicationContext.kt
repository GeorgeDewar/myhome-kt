package nz.co.dewar.myhome.gui

import nz.co.dewar.myhome.model.Plan

object ApplicationContext {
    var plan: Plan = Plan(listOf())
    var level: Int = 0
}