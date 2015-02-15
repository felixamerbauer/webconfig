package at.amerbauer

import akka.actor.ActorSystem
import akka.util.Timeout
import spray.routing.SimpleRoutingApp
import scala.concurrent.duration._
import java.io.File
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport._

case class KeyValue(key: String, value: String)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val PersonFormat = jsonFormat2(KeyValue)
}

object Server extends App with SimpleRoutingApp {
  // setup
  implicit val actorSystem = ActorSystem()
  implicit val timeout = Timeout(1.second)

  startServer(interface = "0.0.0.0", port = 8080) {
    import MyJsonProtocol._
    // Let's play web server
    pathPrefix("angularjs12") {
      getFromDirectory("""src/main/resources/angularjs12""")
    } ~
      path("service") {
        post {
          entity(as[KeyValue]) { keyValue =>
            complete {
              println(s"got $keyValue")
              "OK"
            }
          }
        }
      }
  }
}
