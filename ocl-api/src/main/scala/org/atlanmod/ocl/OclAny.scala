package org.atlanmod.ocl

trait OclAny {

  /**
   * +(object2 : OclSelf[?]) : Boolean[1] precedence: EQUALITY
   *
   * True if self is the same object as object2. Infix operator.
   *
   * post: result = self = object2
   */
  def +(other: OclAny): OclAny

  /**
    * True if self is the same object as other. Infix operator.
    *
    * @param other
    * @return
    */
  def ==(other: OclAny): scala.Boolean

  /**
    * True if self is a different object from other. Infix operator.
    *
    * @param other
    * @return
    */
  def <>(other: OclAny): scala.Boolean

  /**
   * oclIsUndefined() : Boolean[1] validating
   *
   * Evaluates to true if the self is equal to invalid or equal to null.
   *
   * @return
   */
  def oclIsUndefined() : scala.Boolean

  /**
   * oclIsKindOf(type : OclType) : Boolean[1]
   *
   * Evaluates to true if the type of self conforms to type.
   * That is, self is of type type or a subtype of type.
   *
   * @return
   */
  def oclIsKindOf(t : OclType): scala.Boolean

  /**
   * oclIsTypeOf(type : OclType) : Boolean[1]
   *
   * Evaluates to true if self is of the type type but not a subtype of type.
   *
   * @return
   */
  def oclIsTypeOf(t : OclType): scala.Boolean

  /**
   * toString() : String[1]
   *
   * Returns a string representation of self.
   *
   * @return
   */
  def toString() : String

  /**
   * oclType() : OclType[1]
   *
   * Evaluates to the most derived type of which self is currently an instance.
   * If self is an instance of a multiply classified type,
   * the return is the most derived type of the current classification which is established
   * when the instance is passed to OCL, or re-established by an oclAsType() call.
   *
   * @return
   */
  def oclType(): OclType

  /**
   * asSequence() : Sequence[1]
   *
   * Returns a sequence containing self.
   *
   * @return
   */
  def asSequence() : Sequence[OclAny]

  /**
   * asSet() : Set[1]
   *
   * Returns a set containing self.
   *
   * @return
   */
  def asSet() : Set[OclAny]

  /**
   * asBag() : Bag[1]
   *
   * Returns a bag containing self.
   *
   * @return
   */
  def asBag() : Bag[OclAny]

  /**
   * Writes the string s to the default output.
   *
   * @param s
   */
  def output(s : String): Unit = print(s)

  /**
   * Returns the self value and write the string s to the default output.
   *
   * @param s
   */
  def debug(s : String) : OclAny = {
    print(s)
    this
  }

  /**
   * A reflective operation that returns the value of the self feature identified by name.
   * @param s
   */
  def refGetValue(s : String)  : OclAny

  /**
   * A reflective operation that enables to set the self feature identified by name to value v.
   * It returns self.
   *
   * @param name
   * @param v
   */
  def refSetValue(name : String, v : OclAny)

  /**
   * A reflective operation that returns the immediate composite of self.
   *
   */
  def refImmediateComposite()

  /**
   * A reflective operation that enables to invoke the self operation named opName with the
   * sequence of parameters contained by args.
   *
   * @param opName
   * @param args
   */
  def refInvokeOperation(opName : String, args : Sequence[OclAny])
}
