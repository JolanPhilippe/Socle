package org.atlanmod.ocl.implementation

import org.atlanmod.ocl
import org.atlanmod.ocl._

class BasicString(var value: String) extends OclAny with OclType {

  final val OCL_TYPE_NAME = "BasicString"

  override def equals(that: Any): Boolean =
    that match {
      case that: BasicString => {
        that.canEqual(this) &&
          this.value == that.value
      }
      case _ => false
    }

  // defined to use the diff operation
  def canEqual(a: Any) = a.isInstanceOf[BasicString]

  // defined to use the diff operation
  override def hashCode: Int = value.toInt

  /**
    * +(object2 : OclSelf[?]) : Boolean[1] precedence: EQUALITY
    *
    * True if self is the same object as object2. Infix operator.
    *
    * post: result = self = object2
    */
  override def +(other: OclAny): OclAny = {
    other match {
      case other: BasicString => {
        return new BasicString(value.concat(other.value))
      }
      case _ => {
        return new BasicString(value)
      }
    }
  }

  /**
    * <>(object2 : OclAny) : Boolean[1] precedence: EQUALITY
    *
    * True if self is a different object from object2. Infix operator.
    *
    * post: result = not (self = object2)
    *
    * @return
    */
  override def <>(other: OclAny): Boolean = {
    !(this == other)
  }

  /**
    * =(object2 : OclSelf[?]) : Boolean[1] precedence: EQUALITY
    *
    * True if self is the same object as object2. Infix operator.
    *
    * post: result = self = object2
    */
  override def ==(other: OclAny): Boolean = {
    other match {
      case other: BasicString => {
        return other.value == value
      }
      case _ => {
        return false
      }
    }
  }

  /**
    * oclIsUndefined() : Boolean[1] validating
    *
    * Evaluates to true if the self is equal to invalid or equal to null.
    *
    * @return
    */
  override def oclIsUndefined(): Boolean = ???

  /**
    * oclIsKindOf(type : OclType) : Boolean[1]
    *
    * Evaluates to true if the type of self conforms to type.
    * That is, self is of type type or a subtype of type.
    *
    * @return
    */
  override def oclIsKindOf(t: OclType): Boolean = ???

  /**
    * oclIsTypeOf(type : OclType) : Boolean[1]
    *
    * Evaluates to true if self is of the type type but not a subtype of type.
    *
    * @return
    */
  override def oclIsTypeOf(t: OclType): Boolean = ???

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
  override def oclType(): OclType = ???

  /**
    * asSequence() : Sequence[1]
    *
    * Returns a sequence containing self.
    *
    * @return
    */
  override def asSequence(): Sequence[OclAny] = ???

  /**
    * asSet() : Set[1]
    *
    * Returns a set containing self.
    *
    * @return
    */
  override def asSet(): ocl.Set[OclAny] = ???

  /**
    * asBag() : Bag[1]
    *
    * Returns a bag containing self.
    *
    * @return
    */
  override def asBag(): Bag[OclAny] = ???

  /**
    * Writes the string s to the default output.
    *
    * @param s
    */
  override def output(s: String): Unit = {
    println(s)
  }

  //  /**
  //    * Returns the self value and write the string s to the default output.
  //    *
  //    * @param s
  //    */
  //  override def debug(s: String): Unit = ???

  /**
    * A reflective operation that returns the value of the self feature identified by name.
    *
    * @param s
    */
  override def refGetValue(s: String): OclAny = {
    s match {
      case "value" => new BasicString(value)
      case _ => new BasicString(this.getClass().getDeclaredField(s).get(this).toString())
    }
  }

  /**
    * A reflective operation that enables to set the self feature identified by name to value v.
    * It returns self.
    *
    * @param name
    * @param v
    */
  override def refSetValue(name: String, v: OclAny): Unit = ???

  /**
    * A reflective operation that returns the immediate composite of self.
    *
    */
  override def refImmediateComposite(): Unit = ???

  /**
    * A reflective operation that enables to invoke the self operation named opName with the
    * sequence of parameters contained by args.
    *
    * @param opName
    * @param args
    */
  override def refInvokeOperation(opName: String, args: Sequence[OclAny]): Unit = ???

  override def allInstances(): Collection[OclAny] = {
    return new BasicSet(Vector[OclAny](new BasicString(OCL_TYPE_NAME)))
  }

  override def toString: String = return value

  def >(other: OclAny): Boolean = {
    other match {
      case other: BasicString => {
        return value > other.value
      }
      case _ => {
        return false
      }
    }
  }

  def <(other: OclAny): Boolean = {
    other match {
      case other: BasicString => {
        return value < other.value
      }
      case _ => {
        return false
      }
    }
  }
}
