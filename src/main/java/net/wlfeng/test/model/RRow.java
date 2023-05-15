package net.wlfeng.test.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RRow {

    private List<RCell> cellList = new ArrayList<>();

}
