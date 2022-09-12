package org.atlanmod.ttc.builder

import org.atlanmod.zoo.socialnetwork.{Comment, Post, SocialNetworkPackage, User}
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.{Resource, ResourceSet}
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl

object ModelBuilder {

    private var resSet : ResourceSet = null

    private def instanciateRS(): Unit = {
        val reg = Resource.Factory.Registry.INSTANCE
        val m = reg.getExtensionToFactoryMap
        m.put("xmi", new XMIResourceFactoryImpl)
        // Register the SocialNetworkPackage with the package's URI, ie:
        // eNS_URI = "https://www.transformation-tool-contest.eu/2018/social_media"
        resSet = new ResourceSetImpl
        val packageRegistry = resSet.getPackageRegistry()
        packageRegistry.put(SocialNetworkPackage.eNS_URI, SocialNetworkPackage.eINSTANCE)
    }

    def get_meta(model: EObject) : (Int, Int, Int, Int) = {
        var nbposts = 0
        var nbcomments = 0
        var nbusers = 0
        var nblikes = 0

        for (obj <- model.eContents().toArray()) {
            obj match {
                case post : Post =>
                    nbposts += 1
                case user : User =>
                    nbusers += 1
                    val submissions = user.getSubmissions
                    for (submission <- submissions.toArray){
                        if (submission.isInstanceOf[Comment]) nbcomments += 1
                    }
                    nblikes += user.getLikes.size
            }
        }


        // (posts, comments, users, likes)
        (nbposts, nbcomments, nbusers, nblikes)
    }

    def build_from_uri(uri: String): EObject = {
        if (resSet == null)
            instanciateRS()
        // Load the resource
        val resource = resSet.getResource(URI.createURI(uri), true)
        resource.getContents.get(0)
    }

}
