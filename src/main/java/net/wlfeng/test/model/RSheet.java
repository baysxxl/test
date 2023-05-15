package net.wlfeng.test.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RSheet {

    private List<RRow> headList = new ArrayList<>();

    private List<RRow> dataList = new ArrayList<>();

    private int maxRowNum;

    private int maxColNum;

}
