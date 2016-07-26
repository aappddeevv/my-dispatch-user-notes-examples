```tut:silent:reset
import scala.async.Async._
import dispatch._, Defaults._
import scala.concurrent.Await
import scala.concurrent.duration._
import net.liftweb.json._
import JsonDSL._
```

```tut:book
def callit() = Http(url(s"http://localhost:9000/json") OK as.lift.Json)

val fut = callit()
//fut onComplete { r => println(s"Result $r")}
Await.result(fut, 10.seconds)
```

```tut:silent:invisible
Http.shutdown
```
