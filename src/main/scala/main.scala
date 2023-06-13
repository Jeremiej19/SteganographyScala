import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

val SECRET_LENGTH = 2
val BASE_LENGTH: Int = 8 - SECRET_LENGTH
val SECRET_MASK: Int = (1 << SECRET_LENGTH) - 1
val FILE_WITH_SECRET_FILE_NAME: String = "zwykly_obrazek"
val SECRET_FILE_NAME: String = "sekret"
val DEFAULT_OUTPUT_FILE_EXTENSION: String = "png"

def magick(imgBase: BufferedImage, imgSecret: BufferedImage): BufferedImage = {
  val w = imgBase.getWidth
  val h = imgBase.getHeight
  val secretContent = imgSecret.getScaledInstance(w, h, Image.SCALE_FAST)
  val imgSecretScaled = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB)
  imgSecretScaled.getGraphics.drawImage(secretContent, 0,0, null)
  val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

  for (x <- 0 until w)
    for (y <- 0 until h) {
      val colorBase = Color(imgBase.getRGB(x, y)) >> SECRET_LENGTH << SECRET_LENGTH
      val colorSecret = Color(imgSecretScaled.getRGB(x, y)) >> BASE_LENGTH
      out.setRGB(x, y, (colorBase + colorSecret).RGB)
    }

  out
}
def unmagick(imgWithCode: BufferedImage): BufferedImage = {
  val w = imgWithCode.getWidth
  val h = imgWithCode.getHeight

  val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

  for (x <- 0 until w)
    for (y <- 0 until h) {
      val newColor = (Color(imgWithCode.getRGB(x, y)) & SECRET_MASK) << BASE_LENGTH
      out.setRGB(x, y, newColor.RGB)
    }

  out
}


@main
def main(a: String*): Unit = {
  val args = a.toArray
  val baseFileName :String = args(0)
  val secretFileName :String = args(1)

  val photo1 = ImageIO.read(new File(baseFileName))
  val photo2 = ImageIO.read(new File(secretFileName))
  val photo3 = magick(photo1, photo2)

  ImageIO.write(photo3, DEFAULT_OUTPUT_FILE_EXTENSION, new File(FILE_WITH_SECRET_FILE_NAME+"."+DEFAULT_OUTPUT_FILE_EXTENSION))

  val photo4 = unmagick(ImageIO.read(new File(FILE_WITH_SECRET_FILE_NAME+"."+DEFAULT_OUTPUT_FILE_EXTENSION)))

  ImageIO.write(photo4, DEFAULT_OUTPUT_FILE_EXTENSION, new File(SECRET_FILE_NAME+"."+DEFAULT_OUTPUT_FILE_EXTENSION))
}