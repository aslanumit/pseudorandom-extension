# NetLogo sample Scala extension

This is a toy project to see if I can create a NetLogo extension that allows using different pseudo random number generator algorithms.

I plan to simply use the pseudo random number generator algorithms implemented in the Spire library: https://github.com/typelevel/spire  

It is inspired by Numpy's transition from Mersenne Twister, which is the current NetLogo algorithm, to the PCG64 algorithm for being faster and higher quality: https://numpy.org/doc/stable/reference/random/bit_generators/pcg64.html

## Building

Make sure you have `sbt` installed on your system, then run `sbt package` to package the sample extension jar file.  If the build succeeds, `samplescala.jar` is created.

Run `sbt test` to run the NetLogo language tests from the `tests.txt` file.

Run `sbt packageZip` to create a zip file with all necessary files for publishing the extension.

## Quick notes

* This is just a copy of the `sample Scala extension` repo from NetLogo on GitHub.
* 