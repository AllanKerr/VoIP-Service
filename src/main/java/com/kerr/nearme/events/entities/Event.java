package com.kerr.nearme.events.entities;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfTrue;

/**
 * Created by allankerr on 2016-12-23.
 */
public class Event {

    @Id
    protected Long id;

    @Index(IfTrue.class)
    protected boolean enabled = true;

    @Index
    protected Ref<City> city;

    @Parent
    // TODO Change to the user class
    Key<City> owner;

    protected EventDescriptor descriptor;
}
