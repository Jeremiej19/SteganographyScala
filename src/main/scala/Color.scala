class Color(val RGB : Int) {
  val red: Int = (RGB & 0xff0000) / 65536
  val green: Int = (RGB & 0xff00) / 256
  val blue: Int = (RGB & 0xff)
  def >>(shift :Int): Color = Color(((red >> shift) * 65536) + ((green >> shift) * 256) + (blue >> shift))
  def <<(shift :Int): Color = Color(((red << shift) * 65536) + ((green << shift) * 256) + (blue << shift))
  def +(other :Color): Color = Color(((red + other.red ) * 65536) + ((green + other.green ) * 256) + (blue + other.blue ))
}
