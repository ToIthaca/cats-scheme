package cats

import cats._
import cats.data._
import cats.syntax._
import cats.implicits._

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
