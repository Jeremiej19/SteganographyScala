import Parity.ODD

import java.awt.Color
enum Parity:
  case ODD ,EVEN
class Color(val RGB: Int) {
  def this(red: Int, green: Int, blue: Int) = {
    this((red * 65536) + (green * 256) + blue)
  }

  val red: Int = (RGB & 0xff0000) / 65536
  val green: Int = (RGB & 0xff00) / 256
  val blue: Int = (RGB & 0xff)

  def >>(shift: Int): Color = new Color(((red >> shift) * 65536) + ((green >> shift) * 256) + (blue >> shift))

  def <<(shift: Int): Color = new Color(((red << shift) * 65536) + ((green << shift) * 256) + (blue << shift))

  def +(other: Color): Color = new Color(((red + other.red) * 65536) + ((green + other.green) * 256) + (blue + other.blue))

  def &(mask: Int): Color = new Color(((red & mask) * 65536) + ((green & mask) * 256) + (blue & mask))

  def set_last_bits(r: Parity, g: Parity, b: Parity) =
    new Color(if (r == ODD) red | 1 else red & 0xfffffffe,
      if (g == ODD) green | 1 else green & 0xfffffffe,
      if (b == ODD) blue | 1 else blue & 0xfffffffe)
}

