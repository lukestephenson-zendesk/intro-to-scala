package introcourse.level05

/**
  * These exercises are intended to show the difficulty of working with Exceptions.
  *
  * We will work through a better alternative to Exceptions after this.
  */
@SuppressWarnings(Array("org.wartremover.warts.Throw", "org.wartremover.warts.Any"))
object ExceptionExercises {

  //Exceptions that will be thrown
  class EmptyNameException(message: String) extends Exception(message)

  class InvalidAgeValueException(message: String) extends Exception(message)

  class InvalidAgeRangeException(message: String) extends Exception(message)

  //test data of names and age pairs
  val personStringPairs =
    List(("Tokyo", "30"),
      ("Moscow", "5o"),
      ("The Professor", "200"),
      ("Berlin", "43"),
      ("Arturo Roman", "0"),
      ("", "30"))

  /**
    * Handling validation using Exceptions will come naturally if you are coming
    * to Scala from languages like Java or Ruby. In Scala there is a better way
    * to handle these scenarios, which we will get into later. For now let's
    * use Exceptions to handle the following scenarios and see where the
    * pain points lie.
    */

  /**
    * Implement the function getName, so that it either accepts the supplied name
    * and returns it unchanged or throws a EmptyNameException if the supplied name
    * is empty.
    *
    * scala> getName("Fred")
    * > "Fred"
    *
    * scala> getName("")
    * > EmptyNameException: provided name is empty
    *
    * Hint: use the isEmpty method on String
    */
  def getName(providedName: String): String = {
    if (providedName.isEmpty) {
      throw new EmptyNameException("provided name is empty")
    } else {
      providedName
    }
  }

  /**
    * Implement the function getAge, so that it either accepts the supplied age
    * and returns it as an Int.
    * If the age can't be converted to an Int, throw an InvalidAgeValueException
    * If the provided age is not between 1 and 120 throw an InvalidAgeRangeException.
    *
    * scala> getAge("Fred")
    * > InvalidAgeValueException: provided age is invalid: Fred
    *
    * scala> getAge("20")
    * > 20
    *
    * scala> getAge("0")
    * > InvalidAgeRangeException: provided age should be between 1-120: 0
    *
    * Hint: use the toInt method to convert a String to an Int.
    */
  def getAge(providedAge: String): Int =
    try {
      val validNumber = providedAge.toInt
      if (validNumber > 0 && validNumber <= 120) {
        validNumber
      } else {
        throw new InvalidAgeRangeException(s"provided age should be between 1-120: $validNumber")
      }
    } catch {
      case _: NumberFormatException => throw new InvalidAgeValueException(s"provided age is invalid: $providedAge")
    }


  /**
    * Implement the function createPerson, so that it either accepts a name and age
    * and returns a Person instance or throws an EmptyNameException, InvalidAgeValueException or
    * InvalidAgeRangeException when given invalid values.
    *
    * Notice that createPerson is not declared to throw any Exceptions, although it does.
    * What does this imply?
    *
    * scala> createPerson("Fred", "32")
    * > "Person(Fred, 32)"
    *
    * scala> createPerson("", "32")
    * > EmptyNameException: provided name is empty
    *
    * scala> createPerson("Fred", "ThirtyTwo")
    * > InvalidAgeValueException: provided age is invalid: ThirtyTwo
    *
    * scala> createPerson("Fred", "150")
    * > InvalidAgeRangeException: provided age should be between 1-120: 150
    *
    * Hint: Use `getName` and `getAge` from above.
    */
  def createPerson(name: String, age: String): Person = {
    val validName = getName(name)
    val validAge = getAge(age)
    Person(validName, validAge)
  }

  /**
    * Implement the function createValidPeople to create a List of Person instances
    * from personStringPairs. It should not throw any Exceptions.
    * It should only catch Exceptions thrown by createPerson.
    *
    * scala> createValidPeople
    * > List(Person("Tokyo", 30), Person("Berlin", 43))
    *
    * Hint: Use `map` and `collect`
    *
    * What issues do you run into (if any)?
    */
  def createValidPeople: List[Person] = {
    val listOfPeopleOrErrors: List[Any] = personStringPairs.map {
      case (name, age) =>
        try {
          createPerson(name, age)
        } catch {
          case _: EmptyNameException => ""
          case _: InvalidAgeRangeException => ""
          case _: InvalidAgeValueException => ""
          //handle in any other exception here
        }
    }
    listOfPeopleOrErrors.collect {
      case p: Person => p
    }
  }

  List(1,2,3,4).collect {
    case x if x > 2 => x * 10
  }

  List(Person("Jack", 31), Person("Bob", 32)).collect {
    case p @ Person("Jack", _) => p.copy(name = "Jack Low")
  }

  /**
    * Implement the function collectErrors that collects all the Exceptions
    * that occur while processing personStringPairs. It should not throw any Exceptions.
    * It should only catch Exceptions thrown by createPerson.
    *
    * scala> collectErrors
    * > List(InvalidAgeValueException: provided age is invalid: 5o,
    * InvalidAgeRangeException: provided age should be between 1-120: 200,
    * InvalidAgeRangeException: provided age should be between 1-120: 0,
    * EmptyNameException: provided name is empty)
    *
    * Hint: Use `map` and `collect`
    *
    * What issues do you run into (if any)?
    */
  def collectErrors: List[Exception] = {
    val listOfPeopleOrErrors: List[Any] = personStringPairs.map {
      case (name, age) =>
        try {
          createPerson(name, age)
        } catch {
          case e: EmptyNameException => e
          case e: InvalidAgeRangeException => e
          case e: InvalidAgeValueException => e
          //handle in any other exception here
        }
    }
    listOfPeopleOrErrors.collect {
      case e: EmptyNameException => e
      case e: InvalidAgeRangeException => e
      case e: InvalidAgeValueException => e
    }
  }
}
