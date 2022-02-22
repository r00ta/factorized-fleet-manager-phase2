# factorized-fleet-manager-phase2

The idea is to provide some common abstractions to build your fleet manager. One of the challenges is the management of the status: in this draft I experimented a very first abstraction. 

**NOTE: this is just an experiment. In order to design a good abstraction we have to take into account multiple aspects like the lifecycle of the application: this experiment just aims to give a rough idea of what we might do.**

The abstractions are inside the `core` module. 

The `DinosaurServiceImpl` now extends the `AbstractStatusOrchestrator` class. When a new `Dinosaur` is created, the implementation looks like 

```java
    @Override
    public Dinosaur createDinosaur(String customerId, DinosaurRequest dinosaurRequest) {
        if (managedEntityDAO.findByNameAndCustomerId(dinosaurRequest.getName(), customerId) != null) {
            throw new AlreadyExistingItemException(String.format("Dinosaur with name '%s' already exists for customer with id '%s'", dinosaurRequest.getName(), customerId));
        }

        Dinosaur dinosaur = dinosaurRequest.toEntity();
        dinosaur.setSubmittedAt(ZonedDateTime.now(ZoneOffset.UTC));
        dinosaur.setCustomerId(customerId);
        dinosaur.setShardId(shardService.getAssignedShardId(dinosaur.getId()));

        // Persist and fire events.
        this.accept(dinosaur);

        LOGGER.info("Dinosaur with id '{}' has been created for customer '{}'", dinosaur.getId(), dinosaur.getCustomerId());
        return dinosaur;
    }
```

In particular `this.accept(dinosaur);` will persist and then fire an event so that another thread will process the request asynchronously. The common implementation of `accept` is like

```java
    @Override
    public void accept(T entity) {
        LOGGER.info("Accept entity " + entity.getId());
        if (entity.getStatus() != null) {
            LOGGER.warn("The status of the entity is not null. It will be overwritten.");
        }
        entity.setStatus(ManagedEntityStatus.ACCEPTED);
        entity.setDesiredStatus(ManagedEntityStatus.PREPARING);

        // Persist and fire
        transact(entity);
        abstractPreparingWorker.fireAccepted(entity);
    }

    @Transactional
    private void transact(T entity){
        managedEntityDAO.persist(entity);
    }
```

The event is then consumed by an implementation of `AbstractPreparingWorker`

```java
    // To be annotated by the user, since @ConsumeEvents creates the bean automatically!
    public void consumeAccepted(T entity) {
        LOGGER.info("Consuming event for entity " + entity.getId());

        // we should fire here the PREPARING events to the workers defined by the client of the module.
        // The idea is that the user can deploy dependencies before moving the object to the desired PROVISIONING status
        entity.setStatus(ManagedEntityStatus.PREPARING);
        entity.setDesiredStatus(ManagedEntityStatus.PROVISIONING);
        managedEntityDAO.getEntityManager().merge(entity);
    }
```


## Other common features that can be abstracted

1) the error catalog: we might provide 2 classes to be extended by the custom fleet manager: 
```java 
public class InternalPlatformException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InternalPlatformException(String message) {
        super(message);
    }

    public InternalPlatformException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

and 

```java
/**
 * This class represents all the exceptions that are caused by the user interaction. For example the user asks for a resource
 * that does not exist, the user sends a wrong configuration etc.
 * <p>
 * All the subclasses have to be included in the /resources/exceptionInfo.json because they are directly visible on the catalog.
 */
public class ExternalUserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExternalUserException(String message) {
        super(message);
    }

    public ExternalUserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * The status code to be returned to the client when this Exception is raised. Sub-classes should
     * over-ride this.
     *
     * @return - The HTTP Status code to return to the client.
     */
    public int getStatusCode() {
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
```
The custom fleet shard then creates exceptions extending one of those two classes, and with an annotation like `@ManagedException` we can retrieve them all and build/expose the error catalog automatically.

2) TODO
