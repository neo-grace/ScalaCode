package CSYE7200

import org.scalatest.{FlatSpec,Matchers}

/**
  * Created by Neola Pinto on 9/12/17.
  */
class HelloWorldSpec extends FlatSpec with Matchers {

  behavior of "HelloWorld"

  it should "get the correct greeting" in {
    val greeting = HelloWorld.greeting
    greeting shouldBe "Hello World!"
  }
}