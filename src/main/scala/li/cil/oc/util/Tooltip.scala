package li.cil.oc.util

import li.cil.oc.Localization
import li.cil.oc.client.KeyBindings
import net.minecraft.client.Minecraft
import org.lwjgl.input.Keyboard

import scala.collection.convert.WrapAsJava._
import scala.collection.convert.WrapAsScala._

object Tooltip {
  private val maxWidth = 220

  private val nl = """\[nl\]"""

  private def font = Minecraft.getMinecraft.fontRenderer

  def get(name: String, args: Any*): java.util.List[String] = {
    val tooltip = Localization.localizeImmediately("tooltip." + name).
      format(args.map(_.toString): _*)
    val isSubTooltip = name.contains(".")
    val shouldShorten = (isSubTooltip || font.getStringWidth(tooltip) > maxWidth) && !KeyBindings.showExtendedTooltips
    if (shouldShorten) {
      if (isSubTooltip) Seq.empty[String]
      else Seq(Localization.localizeImmediately("tooltip.TooLong", Keyboard.getKeyName(KeyBindings.extendedTooltip.getKeyCode)))
    }
    else tooltip.
      split(nl).
      map(font.listFormattedStringToWidth(_, maxWidth).map(_.asInstanceOf[String].trim() + " ")).
      flatten.
      toList
  }

  def extended(name: String, args: Any*): java.util.List[String] =
    if (KeyBindings.showExtendedTooltips) {
      Localization.localizeImmediately("tooltip." + name).
        format(args.map(_.toString): _*).
        split(nl).
        map(font.listFormattedStringToWidth(_, maxWidth).map(_.asInstanceOf[String].trim() + " ")).
        flatten.
        toList
    }
    else Seq.empty[String]
}
