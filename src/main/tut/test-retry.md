```tut:silent:reset
import scala.async.Async._
import dispatch._, Defaults._
import scala.concurrent.Await
import scala.concurrent.duration._

import retry.Success._
```

```scala:book
val myhttp = Http.configure(_.setRequestTimeout(2000))

def callit(n: Long) = {
  println("callit!")
  myhttp(url(s"http://localhost:9000/delay/$n") OK as.String).either.right.map(x => x.toLong)
}


val result = retry.Directly(5)(() => callit(5000))
Await.result(result, 30.seconds)
```

```tut:silent:invisible
Http.shutdown
```
