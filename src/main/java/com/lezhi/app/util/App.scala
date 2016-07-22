package com.lezhi.app.util

import scala.util.matching.Regex

/**
  * Created by Colin Yan on 2016/7/12.
  */
object App extends App {
  val input: Option[String] = Extractor.extractCommunityAddress("武夷路555弄13号")

  print(input)

}
