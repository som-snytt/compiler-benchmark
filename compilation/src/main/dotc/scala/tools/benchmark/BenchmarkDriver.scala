package scala.tools.benchmark

import java.io.File
import scala.tools.nsc.BaseBenchmarkDriver
import dotty.tools.dotc.core.Contexts.ContextBase

trait BenchmarkDriver extends BaseBenchmarkDriver {
  def compileImpl(): Unit = {
    implicit val ctx = new ContextBase().initialCtx.fresh
    ctx.setSetting(ctx.settings.usejavacp, true)
    if (depsClasspath != null) {
      ctx.setSetting(ctx.settings.classpath,
                     depsClasspath.mkString(File.pathSeparator))
    }
    ctx.setSetting(ctx.settings.migration, false)
    ctx.setSetting(ctx.settings.outputDir, tempDir.getAbsolutePath)
    ctx.setSetting(ctx.settings.language, List("Scala2"))
    val compiler = new dotty.tools.dotc.Compiler
    val reporter = dotty.tools.dotc.Bench.doCompile(compiler, allArgs)
    assert(!reporter.hasErrors)
  }
}
