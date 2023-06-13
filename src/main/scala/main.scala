import java.awt.Image
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

val FILE_WITH_SECRET_FILE_NAME: String = "zwykly_obrazek"
val SECRET_FILE_NAME: String = "sekret"
val DEFAULT_OUTPUT_FILE_EXTENSION: String = "png"
val PRODUCTION = true

@main
def main(command: String, a: String*): Unit = {
  command match
    case "hide" =>
      try {
        val args = a.toArray
        val baseFilePath: String = args(0)
        val secretFilePath: String = args(1)
        val outputFilePath: String = args(2)
        hideImage( baseFilePath, secretFilePath, outputFilePath)
      } catch {
        case ex: ArrayIndexOutOfBoundsException =>
          // Exception handling code
          println("Caught an ArrayIndexOutOfBoundsException!")
          if(PRODUCTION) ex.printStackTrace()
      }

    case "hide_text" =>
      try {
        val args = a.toArray
        val baseFilePath: String = args(0)
        val outputFilePath: String = args(2)
        val secretText: String = args(1)
        hideText( baseFilePath, outputFilePath, secretText)
      } catch {
        case ex: ArrayIndexOutOfBoundsException =>
          // Exception handling code
          println("Caught an ArrayIndexOutOfBoundsException!")
          if (PRODUCTION) ex.printStackTrace()
      }

    case "show_text" =>
      try {
        val args = a.toArray
        val secretFilePath: String = args(0)
        showText(secretFilePath)
      } catch {
        case ex: ArrayIndexOutOfBoundsException =>
          // Exception handling code
          println("Caught an ArrayIndexOutOfBoundsException!")
          if (PRODUCTION) ex.printStackTrace()
      }

    case "show" =>
      try {
        val args = a.toArray
        val secretFilePath: String = args(0)
        val outputFilePath: String = args(1)
        showImage(secretFilePath, outputFilePath)
      } catch {
        case ex: ArrayIndexOutOfBoundsException =>
          // Exception handling code
          println("Caught an ArrayIndexOutOfBoundsException!")
          if(PRODUCTION) ex.printStackTrace()
      }

    case _ => showHelp()

}

def hideImage(baseFilePath :String, secretFilePath :String, outputFilePath: String): Unit = {
  val photo1 = ImageIO.read(new File(baseFilePath))
  val photo2 = ImageIO.read(new File(secretFilePath))
  val photo3 = HideImage.hide(photo1, photo2)

  ImageIO.write(photo3, DEFAULT_OUTPUT_FILE_EXTENSION, new File( outputFilePath))
}
def hideText(baseFilePath :String, outputFilePath: String, secretText: String): Unit = {
  val photo1 = ImageIO.read(new File(baseFilePath))
  val photo3 = HideText.hide(photo1, secretText)

  ImageIO.write(photo3, DEFAULT_OUTPUT_FILE_EXTENSION, new File( outputFilePath))
}

def showText(secretFilePath :String): Unit = {
  val photo1 = ImageIO.read(new File(secretFilePath))
  val text = HideText.reveal(photo1)
  println(text)
}

def showImage(secretFilePath :String, outputFilePath: String): Unit = {
  val photo4 = HideImage.reveal(ImageIO.read(new File(secretFilePath)))

  ImageIO.write(photo4, DEFAULT_OUTPUT_FILE_EXTENSION, new File(outputFilePath))
}

def showHelp(): Unit = {
  println("Available Commands:\n" +
    "- hide : Hides one image into another.\n" +
    "  Usage: hide <source_image_path> <hidden_image_path> <output_image_path>\n" +
    "  Example: hide image1.jpg image2.png output.jpg\n\n" +
    "- show : Retrieves the hidden image from an image.\n" +
    "  Usage: show <image_with_hidden_message_path> <output_img_path>\n" +
    "  Example: show steganography_image.jpg hidden.jpg\n\n" +
    "- hide_text : Hides text in an image.\n" +
    "  Usage: hide_text <source_image_path> <text> <output_image_path>\n" +
    "  Example: hide_text image.jpg \"Secret message\" output.jpg\n\n" +
    "- show_text : Retrieves the hidden text from an image.\n" +
    "  Usage: show_text <image_path>\n" +
    "  Example: show_text steganography_image.jpg")

}