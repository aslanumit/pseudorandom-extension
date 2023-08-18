package org.nlogo.extensions.pseudorandom

import org.nlogo.{ agent, api, core, nvm }
import core.Syntax._
import api.ScalaConversions._  // implicits
import org.nlogo.core.AgentKind

import spire._

class PseudoRandomExtension extends api.DefaultClassManager {

  def load(manager: api.PrimitiveManager) {
    // generate numbers
    manager.addPrimitive("random", Random)
//    manager.addPrimitive("random-float", RandomFloat)
    manager.addPrimitive("random-normal", RandomGaussian)
//    manager.addPrimitive("random-gaussian", RandomGaussian)
    // switch algorithm
    manager.addPrimitive("use-mersenne", UseMersenne)
    manager.addPrimitive("use-pcg", UsePCG)
    manager.addPrimitive("use-well", UseWell)
    manager.addPrimitive("use-xorshift", UseXorshift)
    // utilities
    manager.addPrimitive("active-algorithm", GetActiveAlgorithm)
  }
}

// Set-up


object UsePCG extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    ActiveAlgorithm.rng = spire.random.rng.PcgXshRr64_32().asInstanceOf[spire.random.Generator]
  }
}

object UseMersenne extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    ActiveAlgorithm.rng = spire.random.rng.MersenneTwister64().asInstanceOf[spire.random.Generator]
  }
}

object UseWell extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    ActiveAlgorithm.rng = spire.random.rng.Well512a().asInstanceOf[spire.random.Generator]
  }
}

object UseXorshift extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    ActiveAlgorithm.rng = spire.random.rng.Marsaglia32a6().asInstanceOf[spire.random.Generator]
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
    ActiveAlgorithm.rng.nextInt(n).toLogoObject
  }
}

object RandomGaussian extends api.Reporter {
  override def getSyntax =
    reporterSyntax(right = List(NumberType, NumberType), ret = ListType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    val m = try args(0).getIntValue
    catch {
      case e: api.LogoException =>
        throw new api.ExtensionException(e.getMessage)
    }

    val sd = try args(1).getIntValue
    catch {
      case e: api.LogoException =>
        throw new api.ExtensionException(e.getMessage)
    }

    ActiveAlgorithm.rng.nextGaussian(m, sd).toLogoObject
  }
}

// UTILITIES
object ActiveAlgorithm{
  var rng = spire.random.rng.MersenneTwister32().asInstanceOf[spire.random.Generator]
}

object GetActiveAlgorithm extends api.Reporter {
  override def getSyntax =
    reporterSyntax(ret = StringType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    ActiveAlgorithm.rng.getClass.getName.split('.').last.toLogoObject
  }
}