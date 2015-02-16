package at.amerbauer

import akka.actor.ActorSystem
import akka.util.Timeout
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol
import spray.routing.SimpleRoutingApp

import scala.concurrent.duration._

case class CheckModel(left: Boolean, middle: Boolean, right: Boolean)

case class Data(singleModel: Int, radioModel: String, checkModel: CheckModel)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val CheckModelFormat = jsonFormat3(CheckModel)
  implicit val DataFormat = jsonFormat3(Data)
}

object Server extends App with SimpleRoutingApp {
  // setup
  implicit val actorSystem = ActorSystem()
  implicit val timeout = Timeout(1.second)

  // data
  var data = Data(0, "Middle", CheckModel(true, false, false))

  startServer(interface = "127.0.0.1", port = 8080) {
    import MyJsonProtocol.DataFormat
    // Let's play web server
    pathPrefix("angularjs12") {
      getFromDirectory( """src/main/resources/angularjs12""")
    } ~
      path("service") {
        post {
          entity(as[Data]) { data =>
            complete {
              println(s"got $data")
              "OK"
            }
          }
        } ~
          get {
            complete {
              println(s"serving $data")
              data
            }
          }
      }
  }
}
