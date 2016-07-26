package example

import dispatch._, Defaults._

import cats.Eval
import cats.data.NonEmptyList
import cats.std.list._
import cats.Id
import cats.syntax.cartesian._

import fetch._
import fetch.unsafe.implicits._
import fetch.implicits._
import fetch.syntax._

object Example1 {

  implicit object ToStringSource extends DataSource[Int, String] {
    override def fetchOne(id: Int): Query[Option[String]] = {
      Query.sync({
        println(s"[${Thread.currentThread.getId}] One ToString $id")
        Option(id.toString)
      })
    }
    override def fetchMany(ids: NonEmptyList[Int]): Query[Map[Int, String]] = {
      Query.sync({
        println(s"[${Thread.currentThread.getId}] Many ToString $ids")
        ids.unwrap.map(i => (i, i.toString)).toMap
      })
    }
  }

  def fetchString(n: Int): Fetch[String] = Fetch(n)(ToStringSource) // or, more explicitly: Fetch(n)(ToStringSource)

  implicit object LengthSource extends DataSource[String, Int] {
    override def fetchOne(id: String): Query[Option[Int]] = {
      Query.async((ok, fail) => {
        println(s"[${Thread.currentThread.getId}] One Length $id")
        ok((Option(id.size)))
      })
    }
    override def fetchMany(ids: NonEmptyList[String]): Query[Map[String, Int]] = {
      Query.async((ok, fail) => {
        println(s"[${Thread.currentThread.getId}] Many Length $ids")
        ok(ids.unwrap.map(i => (i, i.size)).toMap)
      })
    }
  }

  def fetchLength(s: String): Fetch[Int] = Fetch(s)

}

object Main1 {

  import Example1._

  def main(args: Array[String]): Unit = {
    val fetchOne: Fetch[String] = fetchString(1)
    val fetchThree: Fetch[(String, String, String)] = (fetchString(1) |@| fetchString(2) |@| fetchString(3)).tupled
    val fetchMulti: Fetch[(String, Int)] = (fetchString(1) |@| fetchLength("one")).tupled

    val f1 = Fetch.run[Eval](fetchOne).value
    println(s"f1= $f1")
    val f3 = fetchThree.runA[Eval].value
    println(s"f3= $f3")
    val fm = fetchMulti.runA[Eval].value
    println(s"fm= $fm")

    val fetchTwice: Fetch[(String, String)] = for {
      one <- fetchString(1)
      two <- fetchString(1)
    } yield (one, two)
    
    val f2 = fetchTwice.runA[Id]
    println(s"f2= $f2")
  }

}



