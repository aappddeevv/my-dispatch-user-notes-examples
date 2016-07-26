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

import spray.json._

object Example {
  type EntityId = Int

  case class Customer(id: EntityId, name: String, address: String)

  trait CustomerJsonProtocol extends DefaultJsonProtocol {
    implicit val customerFormat = jsonFormat3(Customer)
    implicit val customerListFormt = immSeqFormat[Customer]
  }

  case class Msg(from: EntityId, to: EntityId, content: String)

  trait MsgJsonProtocol extends DefaultJsonProtocol {
    implicit val msgsFormat = jsonFormat3(Msg)
    implicit val msgListFormat = immSeqFormat[Msg]
  }

  implicit object CustomerSource extends DataSource[EntityId, Customer] with CustomerJsonProtocol {
    override def fetchOne(id: EntityId): Query[Option[Customer]] = {
      Query.async((ok, err) => {
        val fut = Http(url(s"http://localhost:9000/customers/$id") OK as.String).
          map(body => spray.json.JsonParser(body).convertTo[Option[Customer]])
        import scala.util._
        fut.onComplete {
          case Success(c) => ok(c)
          case Failure(ex) => err(ex)
        }
      })
    }

    override def fetchMany(ids: NonEmptyList[EntityId]): Query[Map[EntityId, Customer]] =
      batchingNotSupported(ids)
  }
  // Or, Fetch(id)(CustomerSource) w/o implicit resolution
  def fetchCustomer(id: EntityId): Fetch[Customer] = Fetch(id)

  implicit object MsgToSource extends DataSource[EntityId, Seq[Msg]] with MsgJsonProtocol {
    override def fetchOne(id: EntityId): Query[Option[Seq[Msg]]] = {
      Query.async((ok, err) => {
        val fut = Http(url(s"http://localhost:9000/customers/posts-from/$id") OK as.String).
          map(body => spray.json.JsonParser(body).convertTo[Option[Seq[Msg]]])
        import scala.util._
        fut.onComplete {
          case Success(m) => ok(m)
          case Failure(ex) => err(ex)
        }
      })
    }

    override def fetchMany(ids: NonEmptyList[EntityId]): Query[Map[EntityId, Seq[Msg]]] =
      batchingNotSupported(ids)
  }
  def fetchMsgsFrom(id: EntityId): Fetch[Seq[Msg]] = Fetch(id)
}

object FetchMain {

  import Example._

  def main(args: Array[String]): Unit = {
    println("Fectching customers")
    val fetchCustomer1 = fetchCustomer(1)

    val result1 = Fetch.run[Future](fetchCustomer1)
    result1 onComplete println

    import cats.syntax.cartesian._
    val result2 = (fetchCustomer(1) |@| fetchCustomer(2)).tupled.runA[Future]
    result2 onComplete println

    val userAndMsgs = for {
      u <- fetchCustomer(2)
      from <- fetchMsgsFrom(u.id)
    } yield (u, from)
    val userMsgsFut = userAndMsgs.runA[Future]
    userMsgsFut onComplete println

    println("Hit ENTER to end program...")
    scala.io.StdIn.readLine
    println("Program End.")
    Http.shutdown
  }

}



