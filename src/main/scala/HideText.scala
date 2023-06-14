
object HideText {

  import Parity.EVEN
  import Parity.ODD
  import java.awt.Image
  import java.io.File
  import javax.imageio.ImageIO
  import java.awt.image.BufferedImage
  import scala.util.control.Breaks._

  val FILE_WITH_SECRET_FILE_NAME: String = "zwykly_obrazek"
  val SECRET_FILE_NAME: String = "sekret"
  val DEFAULT_OUTPUT_FILE_EXTENSION: String = "png"

  def hide(imgBase: BufferedImage, textSecret: String): BufferedImage = {
    val w = imgBase.getWidth
    val h = imgBase.getHeight
    val textLength = textSecret.length

    if (textLength * 3 > w * h) {
      println("Text is too long to hide it in this image !!")
      sys.exit(1)
    }

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    var currCharIndex = 0
    var currCharValue = textSecret(0).toInt
    // Every ascii character is represented by last bits of 3 pixels
    var currPixelNumber = 0

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val colorBase = Color(imgBase.getRGB(x, y))
        if (currCharIndex < textLength) {
          val r = Parity.fromOrdinal(currCharValue % 2)
          currCharValue = currCharValue >> 1
          val g = Parity.fromOrdinal(currCharValue % 2)
          currCharValue = currCharValue >> 1
          var b = Parity.fromOrdinal(currCharValue % 2)
          currCharValue = currCharValue >> 1

          currPixelNumber = currPixelNumber + 1
          if (currPixelNumber == 3) {
            currCharIndex = currCharIndex + 1
            //Check if it is the end of message set last bit of 3 pixel sequence to 1
            b = if (currCharIndex >= textLength) ODD else EVEN
            if (currCharIndex < textLength) currCharValue = textSecret(currCharIndex).toInt
            currPixelNumber = 0
          }
          out.setRGB(x, y, colorBase.set_last_bits(r, g, b).RGB)
        } else
          out.setRGB(x, y, colorBase.RGB)
      }

    out
  }


  def reveal(imgWithText: BufferedImage): String = {
    val w = imgWithText.getWidth
    val h = imgWithText.getHeight

    var out = ""
    var charValue = 0
    // Every ascii character is represented by last bits of 3 pixels
    var currPixelNumber = 0
    var increment = 1

    breakable{
      for (x <- 0 until w)
        for (y <- 0 until h) {
          currPixelNumber = currPixelNumber + 1
          val colorBase = Color(imgWithText.getRGB(x, y))
          charValue = charValue + (colorBase.red % 2) * increment
          increment = increment << 1
          charValue = charValue + (colorBase.green % 2) * increment
          increment = increment << 1
          if (currPixelNumber != 3) {
            charValue = charValue + (colorBase.blue % 2) * increment
            increment = increment << 1
          } else {
            currPixelNumber = 0
            out = out + charValue.toChar
            charValue = 0
            increment = 1
            if (colorBase.blue % 2 == 1) break
          }
        }
    }

    out
  }
}
