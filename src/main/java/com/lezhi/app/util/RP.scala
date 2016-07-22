package com.lezhi.app.util

/**
  * Created by Colin Yan on 2016/7/12.
  */
class RP {
  val rp1 = """\\d+(\\-|\\/|\\\\|,|\\.|、|\\*|／|，|＊)\\d+(\\-|\\/|\\\\|,|\\.|、|\\*|／|，|＊)\\d+(\\-|\\/|\\\\|,|\\.|、|\\*|／|，|＊)(\\d+)""".r
  val rp2 = """(\\d+[a-z]?)(（|\\()复式(）|\\))(室)?""".r
  val rp3 = """([0-9\\-a-z]+)室/i""".r
  val rp4 = """(\\d+[a-z]?)(甲|乙|丙|丁)?室""".r
  val rp5 = """\\d+号(\\d+)(（复式）)?$""".r
  val rp6 = """^(\\d+)$""".r
  val rp7 = """(东|西|南|北)楼（.*?）(\\d+(\\(.*?\\))?)""".r
  val rp8 = """[0-9a-z]+楼([0-9a-z]+)$""".r
  val rp9 = """号(甲|乙|丙|丁)?(?<room>[0-9a-z]+)(\\W+)?$""".r
  val rp10 = """幢(\\d+)$""".r
  val rp11 = """m#\\d+/\\d+/([0-9a-z]+)#i""".r
  val rp12 = """(.*?\\d+)(\\-|\\/|\\\\|,|\\.|、|\\*|／|，|＊)\\d+(\\-|\\/|\\\\|,|\\.|、|\\*|／|，|＊)(\\d+)""".r
  val rp13 = """(\\d+)(\\-|\\/|\\\\|,|，|\\.|、|\\*|／|—)(甲|乙|丙|丁)?(\\d+)(.*?)""".r
  val rp14 = """单元(\\d+)$""".r
  val rp15 = """(\\d+)(（|\\()[A-Z](）|\\))(室)?$""".r
  val rp16 = """(幢|楼|座|区|号)(\\d+[A-Z])""".r
  val rp17 = """(\\d+)层([A-Z])$""".r
  val rp18 = """(东|西|南|北|中)(一|-|一|二|三|四)?(\\d+)$""".r
  val rp19 = """(\\d+)层([A-Za-z]室)""".r
}
