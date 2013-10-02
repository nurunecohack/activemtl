package com.nurun.activemtl.model;

import java.util.List;

public interface CourtList extends Iterable<Court> {

    int getIndex(String id);

    int size();

    Court get(int position);

    List<Court> list();

}
