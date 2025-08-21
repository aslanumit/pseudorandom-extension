# Proof-of-concept pseudorandom number generator extension for NetLogo

## Building

Make sure you have `sbt` installed on your system, then run `sbt package` to package the sample extension jar file.  If the build succeeds, `samplescala.jar` is created.

Run `sbt test` to run the NetLogo language tests from the `tests.txt` file.

Run `sbt packageZip` to create a zip file with all necessary files for publishing the extension.

## Quick notes

* This is basically a copy of the `sample Scala extension` repo from NetLogo on GitHub.
