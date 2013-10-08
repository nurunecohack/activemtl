package com.nurun.activemtl.model;

import java.util.List;

import com.nurun.activemtl.model.parse.Event;

public interface EventList extends Iterable<Event> {

    int getIndex(String id);

    int size();

    Event get(int position);

    List<Event> list();

}
