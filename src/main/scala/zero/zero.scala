package zero

import cats._
import cats.data._
import cats.implicits._
import cats.syntax._


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

object ZeroExample {
  def main(args: Array[String]): Unit = {
    for {
      as <- getArgs(args)
      _ <- IO(_.putStrLn(s"Hello, ${args.head}"))
    } yield ()
  }
}
