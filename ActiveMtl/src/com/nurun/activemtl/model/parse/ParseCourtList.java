package com.nurun.activemtl.model.parse;

import java.util.ArrayList;
import java.util.List;

import com.nurun.activemtl.model.Court;
import com.nurun.activemtl.model.CourtList;

public class ParseCourtList extends ArrayList<Court> implements CourtList {

    public ParseCourtList(List<ParseCourt> courts) {
        super(courts);
    }

    public ParseCourtList() {
        super();
    }
    
    @Override
    public int getIndex(String id) {
        for (int i = 0; i < size(); i++) {
            Court court = get(i);
            if (court.getCourtId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public List<Court> list() {
        return this;
    }

}
