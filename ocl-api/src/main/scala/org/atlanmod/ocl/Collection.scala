package org.atlanmod.ocl

/**
  * A collection can be seen as a template data type.
  * This means that the declaration of a collection data type has to include the type of the elements
  * that will be contained by the type instances.
  * @tparam E
  */
trait Collection[E <: OclAny] extends OclAny with Seq[E] {
  def size(): Int

  /**
    * True if object is an element of self, false otherwise
    * @param o
    * @return
    */
  def includes(o: E) : Boolean

  /**
    * True if object is not an element of self, false otherwise
    * @param o
    * @return
    */
  def excludes(o: E) : Boolean

  /**
    * The number of times that object occurs in the collection self.
    * @param o
    * @return
    */
  def count(o: E) : Int

  /**
    * Does self contain all the elements of c ?
    * @param c
    * @return
    */
  def includesAll(c: Collection[E]) : Boolean

  /**
    * Does self contain none of the elements of c ?
    *
    * @param c
    * @return
    */
  def excludesAll(c: Collection[E]) : Boolean

  /**
    * Is self the empty collection?
    * @return
    */
  def isEmpty() : Boolean

  /**
    * Is self not the empty collection?
    * @return
    */
  def notEmpty() : Boolean

  /**
    * The number of elements in the collection self.
    *
    * @return a value that corresponds to the addition of all elements in self
    */
  def sum() : E


  /**
    * The sub-collection of the source collection for which body is true
    *
    * @param expression
    * @return
    */
  def select(expression: E => Boolean) : Collection[E]

  /**
    * The sub-collection of the source collection for which body is false.
    *
    * @param expression
    * @return
    */
  def reject(expression: E => Boolean) : Collection[E]

  /**
    * The Collection of elements that results from applying body to every member of the source set. The result
    * is flattened. Notice that this is based on collectNested, which can be of different type depending on the
    * type of source. collectNested is defined individually for each subclass of CollectionType.
    *
    * @param expression
    * @return
    */
  def collect(expression: E => E) : Collection[E]

  /**
    * Results in true if the body expression evaluates to true for each element in the source collection;
    * otherwise, result is false
    *
    * @param expression
    * @return
    */
  def forAll(expression: E => Boolean): Boolean

  /**
    * The Bag that contains all the elements from self.
    * @return
    */
  override def asBag() : Bag[OclAny]

  /**
    * A Sequence that contains all the elements from self, in an order dependent on the particular concrete
    * collection type.
    *
    * @return
    */
  override def asSequence() : Sequence[OclAny]

  /**
    * The Set containing all the elements from self, with duplicates removed.
    * @return
    */
  override def asSet() : Set[OclAny]
}
