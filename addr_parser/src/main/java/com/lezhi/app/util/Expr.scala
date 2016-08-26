package com.lezhi.app.util

import scala.util.matching.Regex

/**
  * Created by Colin Yan on 2016/7/12.
  */
class Expr(val regex: Regex, val group: Option[Int], val str: Option[String], val f: Array[BuildingNoToResidenceName]) {

  def getR = regex

  def accept(input: String): Option[String] = {
    if (group.isDefined) {
      input match {
        case regex(ax) =>
          Some(ax)
        case _ =>
          None
      }
    } else if (str.isDefined) {
      input match {
        case regex() =>
          str
        case _ =>
          None
      }
    } else if (f != null) {
      input match {
        case regex(ax) =>
          //println(ax)
          f./:(None: Option[String])((i: Option[String], b: BuildingNoToResidenceName) => {
            if (i.isDefined) {
              i
            } else {
              if (b(ax))
                b.value
              else
                None
            }
          })
        case _ =>
          None
      }
    } else {
      throw new RuntimeException("nothing is defined")
    }
  }
}

object Expr {
  //def apply(r: Regex, g: Option[Int], s: Option[String]) = new Expr(r, g, s, null)

  def apply(r: Regex, g: Int) = new Expr(r, Some(g), None, null)

  def apply(r: Regex, s: String) = new Expr(r, None, Some(s), null)

  //def apply(r: Regex, f: Map[(Int) => Boolean, String]) = new Expr(r, None, None, f)

  def apply(r: Regex, f: Array[BuildingNoToResidenceName]) = new Expr(r, None, None, f)

  def between(a: Int, b: Int, c: Int): Boolean = {
    a >= b && a <= c
  }

  def in(a: Int, b: Int*): Boolean = b.contains(a)
}