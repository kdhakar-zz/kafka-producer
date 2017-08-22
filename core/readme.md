# http-kafka-producer
Highly scaled micro Http service/layer between events and kafka cluster. Service can be started with config driven port And user has flexibility to plugin transformation
 logic (from http request body to required object) in middle. Every API can write to same of different kafka cluster.

# vert.x architecture
Vert.x has an event-driven architecture and implements the reactor pattern to handle concurrent requests. So the goal is to receive concurrent requests and to put them on a queue.
An event-loop will dequeue each of these events and dispatch them sequentially to an handler. The process of these events by the event loop is synchronous – one by one.  The handlers
are asynchronous. Something very important to understand is that the event loop and the handlers are executed in the same thread. So the golden rule is that a handler must never block.
If it blocks for some reasons (access to a database, file or external service) it blocks the process of all other requests, events. A vert.x application involves small number of thread
so it consumes less memory and lose less CPU time during context switching. Its event driven architecture will help you to create more scalable application than if you did it with
another framework based on the traditional “thread per request  core.model”. Unlike of node.js, Vert.x will create as many event-loops as the number of available CPU cores.
 
 ![event loop](event-loop.png?raw=true "event loop")
 
# Getting started
 - Required implementation : User has to provide implementation for following interfaces.
   ````
   1. Request Transformer : To convert request body (of http request into kafka producer object).
      public interface RequestTransformer<V> {
          Optional<KafkaRecord<V>> getKafkaRecord(RoutingContext routingContext);
      }
    
   2. Vertx Config provider : vertx and kafka-producer config provider. 
      public interface VertxConfig {
          VertxOptions getVertxOptions() throws ServiceException;
      
          int getServicePort() throws ServiceException;
          
          List<KafkaProperty> getKafkaProperties() throws ServiceException;
      } 
   3. Service Config provider : service runtime config provider.  
       public interface ServiceConfig {
           Set<String> getURLToIgnore();
       
           Set<String> getTopicSetToDropEvent();
       } 
 - Main class will look like this.
   ````
   public class client.Application extends KafkaVerticle {
   
       public static void main(String[] args) throws ServiceException {
           MetricManager.register();
           new client.Application().provideConfigAndBuildVertx();
       }
   
       private void provideConfigAndBuildVertx() throws ServiceException {
           super.buildVertx(new VertxConfigProvider(), new ServiceConfigProvider());
       }
   }
