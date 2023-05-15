package net.wlfeng.test.model;

import lombok.Data;

@Data
public class RCell {

    private int rowIndex;

    private String rowName;

    private int colIndex;

    private String colName;

    private int rowSpan;

    private int colSpan;

    private String value;

    private int width;

}
