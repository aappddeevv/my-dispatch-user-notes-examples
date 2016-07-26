```tut:silent:reset
import scala.async.Async._
import dispatch._, Defaults._
import scala.concurrent.Await
import scala.concurrent.duration._
```


```tut:book
def callit(n: Long) =
  Http(url(s"http://localhost:9000/delay/$n") OK as.String).map(_.toLong)

val d1 = 5000
val d2 = 2000

println(s"Delay 1 is $d1, Delay 2 is $d2")

val resultFuture = async {
  val future1 = callit(d1)
  val future2 = callit(d2)
  await(future1) + await(future2)
}

Await.result(resultFuture, 30.seconds)
```

```tut:silent:invisible
Http.shutdown
```
