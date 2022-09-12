package org.atlanmod.ocl

trait OclType {

  /* TODO Hint : replace OclType -> OclModel. Easy to handle. Then has 2 methods :
      allInstances
      allInstances(E <: OclAny)
      In the implementation, BulkGraph <: OclModel
   */

  def allInstances() : Collection[OclAny]

}
