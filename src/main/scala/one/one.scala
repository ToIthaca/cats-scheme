package one

import cats._
import cats.data._
import cats.implicits._

object Parser {
  type ParseResult[A] = String Xor A
  type Parser[A] = (List[Char] => ParseResult[A])

  def parse[A](parser: Parser[A], name: String)(input: String): ParseResult[A] = parser(input.toList).leftMap(err => s"error parsing $name with err: $err") 

  def oneOf(symbols: String): Parser[Char] = (chars: List[Char]) => {
    val symbol = s"([$symbols])".r
    chars.head match {
      case symbol(c) => c.right
      case _ => s"could not find one of $symbols".left
    }
  }
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

