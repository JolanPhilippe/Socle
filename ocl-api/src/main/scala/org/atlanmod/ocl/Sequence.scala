package org.atlanmod.ocl

/**
  * Sequence is a collection in which duplicates are allowed. Sequence is ordered.
  * @tparam E
  */
trait Sequence[E <: OclAny] extends Collection[E] {
  def union(c: Collection[E]) : Sequence[E]
  def flatten() : Sequence[E]

  /**
    * The sequence of elements, consisting of all elements of self, followed by object
    * @param o
    * @return
    */
  def append(o: E) : Sequence[E]

  /**
    * The sequence consisting of object, followed by all elements in self.
    * @param c
    * @return
    */
  def prepend(c: E) : Sequence[E]

  /**
    * The sequence consisting of self with object inserted at position index.
    * @param n
    * @param o
    * @return
    */
  def insertAt(n: Integer, o: E) : Sequence[E]

  /**
    * The sub-sequence of self starting at number lower, up to and including element number upper.
    * @param lower
    * @param upper
    * @return
    */
  def subSequence(lower: Integer, upper: Integer) : Sequence[E]

  def at(n: Integer) : E

  def indexOf(o: E) : Integer

  def first(): E

  def last(): E

  /**
    * The sequence containing all elements of self plus object added as the last element.
    * @param o
    * @return
    */
  def including(o: E): Sequence[E]

  /**
    * The sequence containing all elements of self apart from all occurrences of object.
    * The order of the remaining elements is not changed.
    *
    * @param o
    * @return
    */
  def excluding(o: E): Sequence[E]
}
