package com.kerr.nearme.objectify;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by allankerr on 2017-01-08.
 */

public class Dao<T> {

    private final Class<?> entityClass;

    public Dao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Key<T> save(T object) {
        return ObjectifyService.ofy().save().entity(object).now();
    }

    public void deleteEntities(T... objects) {
        ObjectifyService.ofy().delete().entities(objects).now();
    }
    public void deleteEntity(T object) {
        ObjectifyService.ofy().delete().entity(object).now();
    }

    public T load(String id){
        return (T) ObjectifyService.ofy().load().type(entityClass).id(id).now();
    }

    public T load(Key<T> key) {
        return ObjectifyService.ofy().load().key(key).now();
    }

    public Iterable<T> loadAll(){
        return (Iterable<T>) ObjectifyService.ofy().load().type(entityClass).iterable();
    }
}