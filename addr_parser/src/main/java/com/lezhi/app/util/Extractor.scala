package com.lezhi.app.util

/**
  * Created by Colin Yan on 2016/7/12.
  */
object Extractor {
  def extractCommunityAddress(content: String): Option[String] =
    P.expressions./:(None: Option[String])((i: Option[String], b: Expr) => {

      if (i.isDefined) {
        //println("already matched:" + i)
        i
      } else {
        if (b != null) {
          val x = b.accept(content)
          if (x.isDefined) {
            //println("found by " + b.getR + x)
            x
          } else
            None
        }
        else
          None
      }
    })
}
