
object HideText {

  import Parity.EVEN
  import Parity.ODD
  import java.awt.Image
  import java.io.File
  import javax.imageio.ImageIO
  import java.awt.image.BufferedImage

  val FILE_WITH_SECRET_FILE_NAME: String = "zwykly_obrazek"
  val SECRET_FILE_NAME: String = "sekret"
  val DEFAULT_OUTPUT_FILE_EXTENSION: String = "png"

  def hide(imgBase: BufferedImage, textSecret: String): BufferedImage = {
    val w = imgBase.getWidth
    val h = imgBase.getHeight
    val textLength = textSecret.length

    if(textLength*3 > w*h){
      println("Text is too long to hide it in this image !!")
      sys.exit(1)
    }

    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    var currCharIndex = 0
    var currCharValue = textSecret(0).toInt
    var step = 0

    for (x <- 0 until w)
      for (y <- 0 until h) {
        val colorBase = Color(imgBase.getRGB(x, y))
        if(currCharIndex < textLength) {
          val r = Parity.fromOrdinal(currCharValue%2)
          val g = Parity.fromOrdinal((currCharValue>>1)%2)
          var b = Parity.fromOrdinal((currCharValue>>2)%2)
          currCharValue  = currCharValue >> 3
          step = step + 1
          if(step == 3) {
            b = if(textLength > 0) EVEN else ODD
            currCharIndex = currCharIndex + 1
            if(currCharIndex < textLength) currCharValue = textSecret(currCharIndex).toInt
            step = 0
          }

          out.setRGB(x, y, colorBase.set_last_bits(r,g,b).RGB)
        }else
          out.setRGB(x, y, colorBase.RGB)
      }

    out
  }


  def reveal(imgWithText: BufferedImage): String = {
    val w = imgWithText.getWidth
    val h = imgWithText.getHeight

    var out = ""
    var charValue = 0;
    var pixelCount = 0;
    var increment = 1
    var running = true;

    var x = 0
    var y = 0
    while ((x < w) && (y < h) && running) {
      val colorBase = Color(imgWithText.getRGB(x, y))
      charValue = charValue + (colorBase.red % 2) * increment
      increment = increment * 2
      charValue = charValue + (colorBase.green % 2) * increment
      increment = increment * 2
      if (pixelCount != 2) {
        charValue = charValue + (colorBase.blue % 2) * increment
        increment = increment * 2
        pixelCount = pixelCount + 1
      } else {
        pixelCount = 0
        out = out + charValue.toChar
        charValue = 0
        increment = 1
        if (colorBase.blue % 2 == 1) running = false;
      }

      y = y + 1
      if (y >= w) {
        y = 0
        x = x + 1
      }
    }

//    for (x <- 0 until w)
//      for (y <- 0 until h) {
//        val colorBase = Color(imgWithText.getRGB(x, y))
//        charValue = charValue + (colorBase.red % 2) * increment
//        increment = increment * 2
//        charValue = charValue + (colorBase.green % 2) * increment
//        increment = increment * 2
//        if (pixelCount != 2) {
//          charValue = charValue + (colorBase.blue % 2) * increment
//          increment = increment * 2
//          pixelCount = pixelCount + 1
//        } else {
//          pixelCount = 0
//          println(charValue)
//          out = out + charValue.toChar
//          charValue = 0
//          increment = 1
//          if (colorBase.blue % 2 == 1) return out
//        }
//      }
    out
  }
}
