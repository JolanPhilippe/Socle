package org.atlanmod.ocl

/**
  * Set is a collection without duplicates. Set has no order.
  * @tparam E
  */
trait Set[E <: OclAny] extends Collection[E] {

  /**
    * The set consisting of all elements in self and all elements in c
    *
    * @param c
    * @return
    */
  def union(c: Collection[E]): Set[E]

  /**
    * The intersection of self and c (i.e., the set of all elements that are in both self and c).
    *
    * @param c
    * @return
    */
  def intersection(c: Collection[E]): Set[E]

  /**
    * The elements of self, which are not in s.
    *
    * @param s
    * @return
    */
  def -(s: Set[E]): Set[E]

  /**
    * The set containing all elements of self plus object.
    *
    * @param o
    * @return
    */
  def including(o: E): Set[E]

  /**
    * The set containing all elements of self without object.
    *
    * @param o
    * @return
    */
  def excluding(o: E): Set[E]

  /**
    * The set containing all the elements that are in self or s, but not in both.
    *
    * @param s
    * @return
    */
  def symmetricDifference(s: Set[E]): Set[E]
}
