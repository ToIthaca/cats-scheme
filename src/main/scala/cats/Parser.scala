package cats

import cats._
import cats.data._
import cats.implicits._

case class ParserError(s: String)

trait Parser[A] {
  def apply(s: String): ParserError Xor A
}

object Parser {
  def oneOf(pattern: String): Parser[Char] = new Parser[Char] {
    def apply(s: String): ParserError Xor Char = {
      val c = s.toCharArray()(0)
      if(pattern.toArray.contains(c)) c.right else Xor.left(ParserError(s"not found $c"))
    }
  }
}
