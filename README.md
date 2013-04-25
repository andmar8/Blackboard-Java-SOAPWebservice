Blackboard-Java-SOAPWebservice
==============================

Bare with me, it'll take a while to get all the required source and dependancies on github and then a bit of documentation to make this useful...

This project exposes all the back end code in the helper jar via a SOAP interface, there is also a REST interface but does not currently have as many interfaces to exposes the back end code.

Dependencies
------------

* [JAX-WS 2.2](https://jax-ws.java.net/), you might like to try the [metro](https://metro.java.net/) project instead
* Blackboard jar files (remember to NOT compile these into your jar!)
* [Blackboard-Java-WebservicesBBHelper](https://github.com/andmar8/Blackboard-Java-WebservicesBBHelper)
* [Blackboard-Java-WebservicesBBSerializableObjects](https://github.com/andmar8/Blackboard-Java-WebservicesBBSerializableObjects)
* [Blackboard-Java-WebservicesBBSecurity](https://github.com/andmar8/Blackboard-Java-WebservicesBBSecurity)
* [Blackboard-Java-WebservicesOAuthSOAP](https://github.com/andmar8/Blackboard-Java-WebservicesOAuthSOAP)
* [Blackboard-Java-WebservicesOAuth](https://github.com/andmar8/Blackboard-Java-WebservicesOAuth)

How to compile
--------------

Create/find jars for all of the above and add to your project
