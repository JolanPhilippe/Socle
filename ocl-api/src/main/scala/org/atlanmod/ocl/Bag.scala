package org.atlanmod.ocl

/**
  * Bag is a collection in which duplicates are allowed. Bag has no order.
  * @tparam E
  */
trait Bag[E <: OclAny] extends Collection[E] {

  /**
   * The bag containing all elements of self plus object
   *
   * @param o
   * @return a copy of self with the element o added to the collection.
   */
  def including(o: E): Bag[E]

  /**
   * The bag containing all elements of self apart from all occurrences of object
   *
   * @param o
   * @return a copy of self with the element o removed from the collection
   */
  def excluding(o: E): Bag[E]

  /**
   * Redefines the Collection operation. If the element type is not a collection type, this results in the same
   * bag as self. If the element type is a collection type, the result is the bag containing all the elements of
   * all the recursively flattened elements of self.
   *
   * @return
   */
  def flatten(): Bag[E]
}
