package ink.kylin.gui

import java.io.File;
import scala.swing.event.ButtonClicked;
import scala.swing.event.ActionEvent;
import scala.swing.SimpleSwingApplication;
import scala.swing.FileChooser;
import scala.swing.Button;
import scala.swing.Label;
import scala.swing.FlowPanel;
import scala.swing.MainFrame;

object GUIEvent extends SimpleSwingApplication {
  // 文件选择器
  var fileChooser = new FileChooser(new File("."));
  // 设置选择器的标题
  fileChooser.title = "fileChooser"

  // 定义按钮
  val button = new Button {
    // 设置按钮的文字
    text = "Choose a File from local";
  }

  // 设置标签，设置标签文本内容
  val lable = new Label {
    text = "No any file selected yet"
  }

  // 创建一个FlowPanel面板容器
  val mainPanel = new FlowPanel {
    // 将创建 的按钮和标签组件，添加到FlowPanel容器中
    contents += button;
    contents += lable;
  }

  def top = new MainFrame {
    // 设置窗口标题
    title = "Scala Gui Programing advanced!!!";
    // 将面板添加到窗体上
    contents = mainPanel;
    // 监听button
    listenTo(button);

    // 事件处理
    reactions += {
      // 匹配按钮点击事件
      case ButtonClicked(e) => {
        // 弹出打开选择文件弹出框
        val result = fileChooser.showOpenDialog(mainPanel);
        // 判断选择是文件
        if (result == FileChooser.Result.Approve) {
          // 改变lable文本为：选择文件的路径
          lable.text = fileChooser.selectedFile.getPath;
        }
      }
    }
  }
}