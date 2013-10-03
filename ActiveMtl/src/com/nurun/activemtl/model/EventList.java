package com.nurun.activemtl.model;

import java.util.List;

public interface EventList extends Iterable<Event> {

    int getIndex(String id);

    int size();

    Event get(int position);

    List<Event> list();

}
