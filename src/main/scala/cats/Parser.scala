package cats

import cats._
import cats.data._
import cats.implicits._

case class ParserError(s: String)

import Parser._

final class ParserOps[A](p: Parser[A]) {
  def parse(s: String): ParserError Xor A = p.run(s.toArray.toList).value._2
}

object Parser {

  implicit def stateToParser[A](p: Parser[A]): ParserOps[A] = new ParserOps(p)

  type Parser[A] = State[List[Char], ParserError Xor A]

  def oneOf(pattern: String): Parser[Char]= State[List[Char], ParserError Xor Char] {
    case x :: xs => (xs, if(pattern.toArray.contains(x)) x.right else Xor.left(ParserError(s"not found $x")))
  }

}
