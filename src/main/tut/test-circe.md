```tut:silent:reset
import dispatch._, Defaults._
import scala.concurrent.Await
import scala.concurrent.duration._

import com.ning.http.client.Response
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{ Decoder, Encoder, Json }
import cats.data._
```

```tut:book
object CirceJson extends (Response => cats.data.Xor[ParsingFailure, Json]) {
  def apply(r: Response) =
    (dispatch.as.String andThen parse)(r)
}

def callit() = Http(url(s"http://localhost:9000/json") OK CirceJson)

val fut = callit()
Await.result(fut, 30.seconds)
```

```tut:silent:invisible
Http.shutdown
```
