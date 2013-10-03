package com.nurun.activemtl.model.parse;

import java.util.ArrayList;
import java.util.List;

import com.nurun.activemtl.model.Event;
import com.nurun.activemtl.model.EventList;

public class ParseCourtList extends ArrayList<Event> implements EventList {

    public ParseCourtList(List<ParseEvent> courts) {
        super(courts);
    }

    public ParseCourtList() {
        super();
    }
    
    @Override
    public int getIndex(String id) {
        for (int i = 0; i < size(); i++) {
            Event court = get(i);
            if (court.getEventId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public List<Event> list() {
        return this;
    }

}
