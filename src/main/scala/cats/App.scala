package cats

import cats._
import cats.data._
import cats.syntax._
import cats.implicits._

import  IO._
import Parser._

object App {

  //https://en.wikibooks.org/wiki/Write_Yourself_a_Scheme_in_48_Hours/First_Steps

  def main(args: Array[String]): Unit = {

    val getArgs = IO.getArgs(args)

    val m = for {
      args <- getArgs
      _ <- IO(_.putStrLn("Hello, "))
    } yield ()

    Option(1) >>= (_ => Option(3))

    //m.run(World)

    println(oneOf("!#$%&|*+-/:<=>?@^_~").apply("!"))
  }
}
