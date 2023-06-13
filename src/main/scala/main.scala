import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

val DIFF_LENGTH = 6
val SECRET_LENGTH = 2
val BASE_MASK = 0xfcfcfc
val SECRET_MASK = 0x030303
val READ_MASK = 0xc0c0c0
/*val BASE_MASK = 0x3f3f3f
val SECRET_MASK = 0xc0c0c0*/
val FILLER = 0

/*val DIFF_LENGTH = 4
val BASE_MASK = 0xf0f0f0
val SECRET_MASK = 0x0f0f0f
val FILLER = 0xF*/


def magick(imgBase: BufferedImage,imgSecret: BufferedImage): BufferedImage = {
  // obtain width and height of image
  val w = imgBase.getWidth
  val h = imgBase.getHeight

  // create new image of the same size
  val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

//  setRGB( kolor bazy & & 0xF8F8F8 + kolor_ sekretu & 0x070707 )
//  out.setRGB(x, y, imgBase.getRGB(x, y) & 0xf8f8f8 + imgSecret.getRGB(x, y) & 0x070707 )
  // copy pixels (mirror horizontally)
  for (x <- 0 until w)
    for (y <- 0 until h)
//      out.setRGB(x, y, 0xff000000 + ((imgBase.getRGB(x, y) & BASE_MASK) + (imgSecret.getRGB(x, y) & SECRET_MASK)) )
/*      out.setRGB(x, y, ((imgBase.getRGB(x, y) & BASE_MASK) + ((imgSecret.getRGB(x, y) & 0xffffff ) >> DIFF_LENGTH)) )*/
      val colorBase = Color(imgBase.getRGB(x,y)) >> SECRET_LENGTH << SECRET_LENGTH
      val colorSecret = Color(imgSecret.getRGB(x,y)) >> DIFF_LENGTH
      out.setRGB(x,y, (colorBase + colorSecret).RGB )
      println(imgBase.getRGB(x,y).toBinaryString)
      println(imgSecret.getRGB(x,y).toBinaryString)
      println(out.getRGB(x,y).toBinaryString)
      println()
  println( ((imgSecret.getRGB(200, 200) & 0xffffff ) >> DIFF_LENGTH).toBinaryString )
  out
}
def unmagick(imgWithCode: BufferedImage): BufferedImage = {
  // obtain width and height of image
  val w = imgWithCode.getWidth
  val h = imgWithCode.getHeight

  // create new image of the same size
  val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

  //  setRGB( kolor bazy & & 0xF8F8F8 + kolor_ sekretu & 0x070707 )
  //  out.setRGB(x, y, imgBase.getRGB(x, y) & 0xf8f8f8 + imgSecret.getRGB(x, y) & 0x070707 )
  // copy pixels (mirror horizontally)
  for (x <- 0 until w)
    for (y <- 0 until h) {
/*      println((imgWithCode.getRGB(x, y) ).toBinaryString)
      println((imgWithCode.getRGB(x, y) & 0x070707).toBinaryString)
      println(((imgWithCode.getRGB(x, y) & 0x070707) << (8-3)).toBinaryString)
      println((0xff000000 + ((imgWithCode.getRGB(x, y) & 0x070707) << (8-3))).toBinaryString)
      println(0xff000000.toBinaryString)
      println()*/
      val color = imgWithCode.getRGB(x, y)
      val red = ((((color & 0xff0000) / 65536) & SECRET_MASK) << DIFF_LENGTH) + FILLER
      val green = ((((color & 0xff00) / 256) & SECRET_MASK) << DIFF_LENGTH) + FILLER
      val blue = (((color & 0xff) & SECRET_MASK) << DIFF_LENGTH) + FILLER
      val newColor = (red * 65536) + (green * 256) + blue


/*      val red = ((((color & 0xff0000) / 65536) & READ_MASK) ) + FILLER
      val green = ((((color & 0xff00) / 256) & READ_MASK) ) + FILLER
      val blue = (((color & 0xff) & READ_MASK) ) + FILLER
      val newColor = (red * 65536) + (green * 256) + blue*/

/*      val red = (((color & 0xff0000) / 65536) ) >> 1
      val green = (((color & 0xff00) / 256) ) >> 1
      val blue = ((color & 0xff) ) >> 1
      val newColor = (red * 65536) + (green * 256) + blue*/

      out.setRGB(x, y,  newColor )
/*      println(imgWithCode.getRGB(x, y).toBinaryString)
      println((0xff000000 +newColor).toBinaryString)
      println()*/
//      out.setRGB(x, y, ( ((imgWithCode.getRGB(x, y) & 0x070707) << (8-3))) )
    }

  val color = imgWithCode.getRGB(200, 200)
        val red = ((((color & 0xff0000) / 65536) & SECRET_MASK) << DIFF_LENGTH) + FILLER
        val green = ((((color & 0xff00) / 256) & SECRET_MASK) << DIFF_LENGTH) + FILLER
        val blue = (((color & 0xff) & SECRET_MASK) << DIFF_LENGTH) + FILLER
        val newColor = (red * 65536) + (green * 256) + blue
  println(newColor.toBinaryString)
  out
}

def check(img1: BufferedImage,img2: BufferedImage) = {
  val w = img1.getWidth
  val h = img1.getHeight
  for (x <- 0 until w)
    for (y <- 0 until h) {
      if(img1.getRGB(x,y) != img2.getRGB(x,y)){
        print(x)
        println(y)
      }

/*      println(img1.getRGB(x,y))
      println(img2.getRGB(x,y))
      println()*/
    }
}

def test() = {
  // read original image, and obtain width and height
  val photo1 = ImageIO.read(new File("baza.png"))
  val photo2 = ImageIO.read(new File("kotek.png"))

  val photo3 = magick(photo1,photo2)


  // save image to file "test.jpg"
  ImageIO.write(photo3, "png", new File("test.png"))

  val photo4 = unmagick(ImageIO.read(new File("test.png")))
  ImageIO.write(photo4, "png", new File("secret.png"))
}


@main
def main(): Unit = {
  test()
}