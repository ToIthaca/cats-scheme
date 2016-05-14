package cats

import cats._
import cats.data._
import cats.syntax._
import cats.implicits._

import  IO._

object App {
  def main(args: Array[String]): Unit = {
    val getArgs = IO.getArgs(args)

    val m = for {
      args <- getArgs
      _ <- IO(_.putStrLn("Hello, "))
    } yield ()

    m.run(World)
  }
}
