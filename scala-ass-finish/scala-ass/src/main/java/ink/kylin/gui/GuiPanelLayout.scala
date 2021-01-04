package ink.kylin.gui

import scala.swing.SimpleSwingApplication
import scala.swing.MainFrame
import scala.swing.Button
import scala.swing.Label
import scala.swing.BoxPanel
import scala.swing.Orientable
import scala.swing.Orientation
import scala.swing.Swing

object GuiPanelLayout extends SimpleSwingApplication {
  def top = new MainFrame {
    // 窗口标题
    title = "Second GUI";

    // 构建按钮组件
    val button = new Button {
      // 设置按钮上的文字
      text = "Scala"
    }

    // 构建标签
    val label = new Label {
      // 设置标签上的文字
      text = "Here is Spark!!!";
    }

    // 构建面页面，并将面板设置到窗口,BoxPanel=>Orientation.Vertical,垂直布局
    contents = new BoxPanel(Orientation.Vertical) {
      // 将按钮和标签添加到容中
      contents += button;
      contents += label;
      // 设置边框，上下左右都是50
      border = Swing.EmptyBorder(50, 50, 50, 50);
    }
  }
}