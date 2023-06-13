import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

val FILE_WITH_SECRET_FILE_NAME: String = "zwykly_obrazek"
val SECRET_FILE_NAME: String = "sekret"
val DEFAULT_OUTPUT_FILE_EXTENSION: String = "png"
@main
def main(a: String*): Unit = {
  val args = a.toArray
  val baseFileName :String = args(0)
  val secretFileName :String = args(1)

  val photo1 = ImageIO.read(new File(baseFileName))
  val photo2 = ImageIO.read(new File(secretFileName))
  val photo3 = HideImage.hide(photo1, photo2)

  ImageIO.write(photo3, DEFAULT_OUTPUT_FILE_EXTENSION, new File(FILE_WITH_SECRET_FILE_NAME+"."+DEFAULT_OUTPUT_FILE_EXTENSION))

  val photo4 = HideImage.reveal(ImageIO.read(new File(FILE_WITH_SECRET_FILE_NAME+"."+DEFAULT_OUTPUT_FILE_EXTENSION)))

  ImageIO.write(photo4, DEFAULT_OUTPUT_FILE_EXTENSION, new File(SECRET_FILE_NAME+"."+DEFAULT_OUTPUT_FILE_EXTENSION))
}