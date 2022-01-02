import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import poemGenerator.GeneratorService

object PoemGeneratorMain {
  def main(args: Array[String]) = {
    implicit val system = ActorSystem()
    implicit val executionContext = system.dispatcher

    val generatorService = new GeneratorService();

    val route: Route =
      pathPrefix("poem-generator") {
        generatorService.routes
      }

    val host = "0.0.0.0"
    val port = sys.env.getOrElse("PORT", "8080").toInt

    val bindingFuture = Http().bindAndHandle(route, host, port)
    println(s"Server online at http://localhost:${port}/poem-generator/")

  }
}
