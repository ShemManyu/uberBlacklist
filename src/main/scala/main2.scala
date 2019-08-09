import akka.http.scaladsl.server._

object main2 {
  def main(args: Array[String]): Unit = {
    /*implicit val system           = ActorSystem()
    implicit val materializer     = ActorMaterializer()
    implicit val executionContext = system.dispatcher*/

    val port: Int = sys.env.getOrElse("PORT", "8080").toInt
    WebServer.startServer("0.0.0.0", port)
  }
}
object WebServer extends HttpApp {
  override protected def routes: Route =
    pathEndOrSingleSlash {
      get {
        complete("It works fine")
      }
    }
}