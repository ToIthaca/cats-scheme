package two

import cats._
import cats.data._
import cats.implicits._

object Parser {
  type ParseResult[A] = String Xor A
  type Parser[A] = XorT[State[List[Char], ?], String, A]

  def parse[A](parser: Parser[A], name: String)(input: String): ParseResult[A] = parser.leftMap(err => s"error parsing $name with err: $err").value.runA(input.toList).value

  def parser[A](f: List[Char] => (List[Char], ParseResult[A])): Parser[A] = XorT[State[List[Char], ?], String, A](State(f))

  val skip1Space = oneOf(" ")
  def oneOf(symbols: String): Parser[Char] = parser { chars =>
    val symbol = s"([$symbols])".r
    chars.head match {
      case symbol(c) => (chars.tail, c.right)
      case _ => (chars, s"could not find one of $symbols".left)
    }
  }

  implicit def parserOps[A](parser: Parser[A]): ParserOps[A] = new ParserOps(parser)
}

import Parser._

final class ParserOps[A](parser: Parser[A]) {
  def <|>[B](that: Parser[B]): Parser[A Xor B] = parser.map(_.left[B]) orElse that.map(_.right[A])
  def >>[B](that: Parser[B]): Parser[Unit] = for {
    _ <- parser
    _ <- that
  } yield ()
}

sealed trait World {
  def putStrLn(s: String): Unit
}

object World extends World {
  def putStrLn(s: String): Unit = println(s)
}

object IO {
  type IO[A] = Reader[World, A]
  def apply[A](f: World => A): IO[A] = Reader(f)
  def getArgs(args: Array[String]): IO[List[String]] = args.pure[IO].map(_.toList)
}

import IO._
import Parser._

object OneExample {

  val symbol = oneOf("!#$%&|*+-/:<=>?@^_~")
  def readExpr(input: String): String = parse(symbol, "lisp")(input) match {
    case Xor.Left(err) => s"No match: ${err.show}"
    case Xor.Right(v) => "Found value"
  }

  def main(args: Array[String]): Unit = {
    for {
      as <- getArgs(args)
      _ <- IO(_.putStrLn(s"Hello, ${args.head}"))
    } yield ()
  }
}
