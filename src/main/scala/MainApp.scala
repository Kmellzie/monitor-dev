import kamon.Kamon


import scala.io.StdIn


object MainApp {
  def main(args: Array[String]) {
    Kamon.start()

    StdIn.readLine()

    Kamon.shutdown()
  }
}
