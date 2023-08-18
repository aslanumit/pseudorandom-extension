package org.nlogo.extensions.pseudorandom

import org.nlogo.{ agent, api, core, nvm }
import core.Syntax._
import api.ScalaConversions._  // implicits
import org.nlogo.core.AgentKind

//import spire.random._
import spire.random.Generator // Gives us the Generator abstract class to swap algorithms
import spire.random.rng // Gives us the algorithms

class PseudoRandomExtension extends api.DefaultClassManager {

  def load(manager: api.PrimitiveManager) {
    // generate numbers
    manager.addPrimitive("random", Random)
    manager.addPrimitive("random-float", RandomDouble)
    manager.addPrimitive("random-normal", RandomGaussian)
    // switch algorithm
    manager.addPrimitive("use-mersenne", UseMersenne)
    manager.addPrimitive("use-pcg", UsePCG)
    manager.addPrimitive("use-well", UseWell)
    manager.addPrimitive("use-xorshift", UseXorshift)
    // utilities
    manager.addPrimitive("active-algorithm", ReportActiveAlgorithm)
  }
}

// Set-up
object PseudoRandom{
  var algorithm = rng.MersenneTwister64().asInstanceOf[Generator]
}

// Generators

object UsePCG extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    PseudoRandom.algorithm = rng.PcgXshRr64_32().asInstanceOf[Generator]
  }
}

object UseMersenne extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    PseudoRandom.algorithm = rng.MersenneTwister64().asInstanceOf[spire.random.Generator]
  }
}

object UseWell extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    PseudoRandom.algorithm = rng.Well1024a().asInstanceOf[Generator]
  }
}

object UseXorshift extends api.Command {
  override def getSyntax =
    commandSyntax()

  def perform(args: Array[api.Argument], context: api.Context) {
    PseudoRandom.algorithm = rng.Marsaglia32a6().asInstanceOf[Generator]
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

    var num = 0
    PseudoRandom.algorithm.nextInt(n).toLogoObject
  }
}

object RandomDouble extends api.Reporter {
  override def getSyntax =
    reporterSyntax(right = List(NumberType), ret = ListType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    val n = try args(0).getDoubleValue
    catch {
      case e: api.LogoException =>
        throw new api.ExtensionException(e.getMessage)
    }
    if (n < 0)
      throw new api.ExtensionException("input must be positive")

    PseudoRandom.algorithm.nextDouble(n).toLogoObject
  }
}

object RandomGaussian extends api.Reporter {
  override def getSyntax =
    reporterSyntax(right = List(NumberType, NumberType), ret = ListType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    val m = try args(0).getDoubleValue
    catch {
      case e: api.LogoException =>
        throw new api.ExtensionException(e.getMessage)
    }

    val sd = try args(1).getDoubleValue
    catch {
      case e: api.LogoException =>
        throw new api.ExtensionException(e.getMessage)
    }

    PseudoRandom.algorithm.nextGaussian(m, sd).toLogoObject
  }
}


// Utilities

object ReportActiveAlgorithm extends api.Reporter {
  override def getSyntax =
    reporterSyntax(ret = StringType)
  def report(args: Array[api.Argument], context: api.Context): AnyRef = {
    PseudoRandom.algorithm.getClass.getName.split('.').last.toLogoObject
  }
}