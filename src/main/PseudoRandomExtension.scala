package org.nlogo.extensions.pseudorandom

import org.nlogo.{ agent, api, core, nvm }
import core.Syntax._
import api.ScalaConversions._  // implicits
import org.nlogo.core.AgentKind

import spire._

class PseudoRandomExtension extends api.DefaultClassManager {

  def load(manager: api.PrimitiveManager) {
    manager.addPrimitive("active-algorithm", GetActiveAlgorithm)
    manager.addPrimitive("random", Random)
    manager.addPrimitive("random-float", RandomFloat)
    manager.addPrimitive("random-normal", RandomGaussian)
    manager.addPrimitive("random-gaussian", RandomGaussian)
    manager.addPrimitive("use-pcg", UsePCG)
    manager.addPrimitive("use-mersenne", UseMersenne)
  }
}

// Set-up

object ActiveAlgorithm{
  var rng = "mersenne"
}

object GetActiveAlgorithm extends api.Reporter {
  override def getSyntax =
    reporterSyntax(ret = StringType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    ActiveAlgorithm.rng.toLogoObject
  }
}

object UsePCG extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    ActiveAlgorithm.rng = "pcg"
  }
}

object UseMersenne extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    ActiveAlgorithm.rng = "mersenne"
  }
}

// Generators

object Random extends api.Reporter {
  override def getSyntax =
    reporterSyntax(right = List(NumberType), ret = ListType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    val n = try args(0).getIntValue
    catch {
      case e: api.LogoException =>
        throw new api.ExtensionException(e.getMessage)
    }
    if (n < 0)
      throw new api.ExtensionException("input must be positive")
//    val rng = spire.random.rng.PcgXshRr64_32()
//    rng.nextInt(n).toLogoObject
    var num = 0
    if (ActiveAlgorithm.rng == "pcg")
      num = spire.random.rng.PcgXshRr64_32().nextInt(n)
    if (ActiveAlgorithm.rng == "mersenne")
      num = spire.random.rng.MersenneTwister32().nextInt(n)
    num.toLogoObject
  }
}

object RandomFloat extends api.Reporter {
  override def getSyntax =
    reporterSyntax(right = List(NumberType), ret = ListType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    val n = try args(0).getIntValue
    catch {
      case e: api.LogoException =>
        throw new api.ExtensionException(e.getMessage)
    }
    if (n < 0)
      throw new api.ExtensionException("input must be positive")
    val rng = spire.random.rng.PcgXshRr64_32()
    rng.nextInt(n).toLogoObject
  }
}

object RandomGaussian extends api.Reporter {
  override def getSyntax =
    reporterSyntax(right = List(NumberType), ret = ListType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    val n = try args(0).getIntValue
    catch {
      case e: api.LogoException =>
        throw new api.ExtensionException(e.getMessage)
    }
    if (n < 0)
      throw new api.ExtensionException("input must be positive")
    val rng = spire.random.rng.PcgXshRr64_32()
    rng.nextInt(n).toLogoObject
  }
}