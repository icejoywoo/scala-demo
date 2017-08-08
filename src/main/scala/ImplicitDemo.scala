import java.io.File

object ImplicitDemo {

  class FileWrapper(val file: File) {
    def /(next: String) = new FileWrapper(new File(file, next))

    override def toString: String = file.getAbsolutePath
  }

  object FileWrapper {
    implicit def wrap(file: File): FileWrapper = new FileWrapper(file)
    implicit def unwrap(wrapper: FileWrapper): File = wrapper.file
  }

  def usePath(file: FileWrapper): Unit = println(file)

  def main(args: Array[String]): Unit = {
    val cur = new FileWrapper(new File("."))
    println(cur / "tmp.txt")

    usePath(new File("."))
  }
}
