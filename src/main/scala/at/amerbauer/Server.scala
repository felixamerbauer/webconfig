package at.amerbauer

import akka.actor.{Props, Actor, ActorSystem}
import akka.util.Timeout
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol
import spray.routing.SimpleRoutingApp
import spray.http.StatusCodes.OK

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
  actorSystem.actorOf(Props(classOf[DataManipulator]))
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
          entity(as[Data]) { updatedData =>
            complete {
              println(s"got $updatedData")
              data = updatedData
              OK
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

class DataManipulator extends Actor {

  import context.dispatcher

  val tick = context.system.scheduler.schedule(1 second, 7 seconds, self, "tick")

  val change = Map("Left" -> "Middle", "Middle" -> "Right", "Right" -> "Left")

  override def postStop() = tick.cancel()

  def receive = {
    case "tick" =>
      val newData = Server.data.copy(radioModel = change(Server.data.radioModel))
      println(s"Updating data\n${Server.data} ->\n$newData")
      Server.data = newData
  }
}
