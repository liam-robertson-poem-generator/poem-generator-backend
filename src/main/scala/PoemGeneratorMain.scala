import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import poemGenerator.GeneratorService

object PoemGeneratorMain {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val generatorService = new GeneratorService();

    val route: Route =
      pathPrefix("poem-generator") {
        generatorService.routes
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/poem-generator/")
    println(s"Resources at http://localhost:8080/poem-generator/poems/1-1-1.xml")
    println(s"Resources at http://localhost:8080/poem-generator/glyphs/1-1-1.jpg")

  }
}
