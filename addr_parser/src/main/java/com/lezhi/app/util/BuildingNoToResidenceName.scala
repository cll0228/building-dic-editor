package com.lezhi.app.util

/**
  * Created by Colin Yan on 2016/7/13.
  */
class BuildingNoToResidenceName(val f: (Int) => Boolean, val str: String) {

  def apply(no: String): Boolean = {
    try {
      f(no.toInt)
    } catch {
      case _: Exception => false
    }
  }
  def value = Some(this.str)
}

object BuildingNoToResidenceName {
  def apply(f: (Int) => Boolean, value: String) = new BuildingNoToResidenceName(f, value)

  def apply(value: String) = new BuildingNoToResidenceName((_) => true, value)
}