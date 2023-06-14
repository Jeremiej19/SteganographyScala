import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@main
def main(a: String*): Unit = {
  val args = a.toArray
  if (args.length == 0)
    ArgumentParser.parse("help", args.slice(1 , args.length)*)
  else
    ArgumentParser.parse(args(0), args.slice(1 , args.length)*)
}
