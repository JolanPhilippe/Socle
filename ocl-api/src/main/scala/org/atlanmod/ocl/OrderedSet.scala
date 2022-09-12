package org.atlanmod.ocl

/**
  * OrderedSet is a collection without duplicates. OrderedSet is ordered.
  * @tparam E
  */
trait OrderedSet[E <: OclAny] extends Collection[E] {
  /**
    *
    * @param o
    * @return a copy of self with the element o added at the end of the ordered set if it does not already appear in self
    */
  def append(o: E): OrderedSet[E]

  /**
    * a copy of self with the element o added at the beginning of the ordered set if it does not already appear in self
    * @param o
    * @return
    */
  def prepend(o: E): OrderedSet[E]

  /**
    *
    * @param n
    * @param o
    * @return a copy of self with the element o added at rank n of the ordered set if it does not already appear in self
    */
  def insertAt(n: Integer, o: E): OrderedSet[E]

  /**
    *
    * @param lower
    * @param upper
    * @return a subsequence of self starting from rank lower to rank upper (both bounds being included)
    */
  def subOrderedSet(lower: Integer, upper: Integer): OrderedSet[E]

  /**
    *
    * @param n
    * @return the element located at rank n in self
    */
  def at(n: Integer): E

  /**
    *
    * @param o
    * @return the rank of first occurrence of o in self
    */
  def indexOf(o: E): Integer

  /**
    *
    * @return the first element of self (oclUndefined if self is empty)
    */
  def first(): E

  /**
    *
    * @return the last element of self (oclUndefined if self is empty)
    */
  def last(): E

  /**
    *
    * @param c
    * @return an ordered set composed of the elements of self followed by the elements of c with duplicates removed
    *         (they may appear within c, and between c and self elements)
    */
  def union(c: Collection[E]): OrderedSet[E]

  /**
    *
    * @return an ordered set directly containing the children of the nested subordinate collections contained by self
    */
  def flatten(): OrderedSet[E]

  /**
    *
    * @param o
    * @returna copy of self with the element o added at the end of the ordered set if it does not already appear in self
    */
  def including(o: E): OrderedSet[E]

  /**
    *
    * @param o
    * @return returns a copy of self with the o removed
    */
  def excluding(o: E): OrderedSet[E]
}
