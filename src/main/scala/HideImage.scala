object HideImage {
  import java.awt.Image
  import java.io.File
  import javax.imageio.ImageIO
  import java.awt.image.BufferedImage

  private val SECRET_LENGTH = 3
  private val BASE_LENGTH: Int = 8 - SECRET_LENGTH
  private val SECRET_MASK: Int = (1 << SECRET_LENGTH) - 1
  val FILE_WITH_SECRET_FILE_NAME: String = "zwykly_obrazek"
  val SECRET_FILE_NAME: String = "sekret"
  val DEFAULT_OUTPUT_FILE_EXTENSION: String = "png"

  def hide(imgBase: BufferedImage, imgSecret: BufferedImage): BufferedImage = {
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
  def reveal(imgWithCode: BufferedImage): BufferedImage = {
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

}
