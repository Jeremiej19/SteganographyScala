import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@main
def main(command: String, a: String*): Unit = {
  ArgumentParser.parse(command, a*);
}
