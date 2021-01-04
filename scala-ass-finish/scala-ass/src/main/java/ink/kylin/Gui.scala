package ink.kylin

import scala.swing._

import scala.swing._
import scala.swing.event._

class Gui extends MainFrame {
  def restrictHeight(s: Component) {
    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
  }

  title = "airports reports and query"

  val airportsField = new TextField { columns = 32 }
  val runwaysField = new TextField { columns = 32 }
  val airportsButton = new Button("query")
  val runwaysButton = new Button("query")

  val gender = new ComboBox(List("airports_top10", "airports_low10", "runways_top10","runways_surface_type"))
  val commentField = new TextArea { rows = 8; lineWrap = true; wordWrap = true }

  restrictHeight(airportsField)
  restrictHeight(runwaysField)

  restrictHeight(gender)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("airports")
      contents += Swing.HStrut(5)
      contents += airportsField
      contents += Swing.HStrut(5)
      contents += airportsButton
    }
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("runways")
      contents += Swing.HStrut(5)
      contents += runwaysField
      contents += Swing.HStrut(5)
      contents += runwaysButton
    }

    contents += Swing.VStrut(5)
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("reports")
      contents += Swing.HStrut(20)
      contents += gender
    }
    contents += Swing.VStrut(5)
    contents += new Label("Comments")
    contents += Swing.VStrut(3)
    contents += new ScrollPane(commentField)
    contents += Swing.VStrut(5)
    contents += new BoxPanel(Orientation.Horizontal) {
     // contents += pressMe
      contents += Swing.HGlue
      contents += Button("Close") { reportAndClose() }
    }
    for (e <- contents)
      e.xLayoutAlignment = 0.0
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  listenTo(airportsField)
  listenTo(commentField)
  listenTo(gender.selection)
  listenTo(airportsButton)
  listenTo(runwaysButton)


  reactions += {

    case SelectionChanged(`gender`) =>
      val item = gender.selection.item
      if("airports_top10".equals(item)) commentField.text=DataQuery.getAirPortTop10()
      if("airports_low10".equals(item)) commentField.text=DataQuery.getAirPortLow10()
      if("runways_top10".equals(item)) commentField.text=DataQuery.getRunwaysTop10()
      if("runways_surface_type".equals(item)) commentField.text=DataQuery.getRunwaysSurfaceType()

    case ButtonClicked(`airportsButton`) =>
      val query = airportsField.text
      commentField.text=DataQuery.airPortsQuery(query.toUpperCase)
    case ButtonClicked(`runwaysButton`) =>
      val query = runwaysField.text
      commentField.text=DataQuery.runwaysQuery(query.toUpperCase)
  }

  def reportAndClose() {
    sys.exit(0)
  }
}

object GuiProgramSix {
  def main(args: Array[String]) {
    val ui = new Gui
    ui.visible = true
  }
}