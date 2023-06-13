import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

val SECRET_LENGTH = 2
val BASE_LENGTH: Int = 8 - SECRET_LENGTH
val SECRET_MASK: Int = (1 << SECRET_LENGTH) - 1

def magick(imgBase: BufferedImage, imgSecret: BufferedImage): BufferedImage = {
  val w = imgBase.getWidth
  val h = imgBase.getHeight

  val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

  for (x <- 0 until w)
    for (y <- 0 until h) {
      val colorBase = Color(imgBase.getRGB(x, y)) >> SECRET_LENGTH << SECRET_LENGTH
      val colorSecret = Color(imgSecret.getRGB(x, y)) >> BASE_LENGTH
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

def test() = {
  val photo1 = ImageIO.read(new File("baza.png"))
  val photo2 = ImageIO.read(new File("kotek.png"))

  val photo3 = magick(photo1, photo2)

  
  ImageIO.write(photo3, "png", new File("test.png"))

  val photo4 = unmagick(ImageIO.read(new File("test.png")))
  ImageIO.write(photo4, "png", new File("secret.png"))
}

@main
def main(): Unit = {
  test()
}