package ink.kylin.gui
// 导入scala.swing下所有的包

import scala.swing._

import scala.swing.event._

// 实现SimpleSwingApplication抽象类  // 重写top方法，返回Frame类型
object gui extends SimpleSwingApplication {
  // MainFrame是一个容器
  def top = new MainFrame {
    // title、content空器的属性
    title = "First Swing App"
    contents = new Button {
      text = "Click me"
    }
  }
}