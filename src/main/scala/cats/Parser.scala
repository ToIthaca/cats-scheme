package cats

import cats._
import cats.data._
import cats.implicits._

case class ParserError(s: String)

import Parser._

final class ParserOps[A](p: Parser[A]) {
  def parse(s: String): ParserError Xor A = p.run(s.toList).value._2
  def parseS(s: String): (String, ParserError Xor A) = {
    val (ca, xor) = p.run(s.toList).value
    (ca.toString, xor)
  }
}

object Parser {

  implicit def stateToParser[A](p: Parser[A]): ParserOps[A] = new ParserOps(p)

  type Parser[A] = State[List[Char], ParserError Xor A]

  def apply[A](f: List[Char] => (List[Char], A)) = State(f)

  def oneOf(pattern: String): Parser[Char]= State[List[Char], ParserError Xor Char] {
    case x :: xs => (xs, if(pattern.toArray.contains(x)) x.right else ParserError(s"not found $pattern").left)
    case Nil => (Nil, ParserError(s"not found $pattern").left)
  }

  val skip1Space: Parser[Unit] = oneOf(" ").map(x => x.map(_ => ()))
  val skipManySpaces: Parser[Unit] = apply { c =>
    skip1Space.run(c).value match {
      case (_, Xor.Left(_)) => (c, ().right)
      case (c2, Xor.Right(_)) => skipManySpaces.run(c2).value
    }
  }

}
