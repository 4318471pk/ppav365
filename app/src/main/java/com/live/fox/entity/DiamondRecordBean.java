package com.live.fox.entity;

import java.util.List;

public class DiamondRecordBean {

    int total;
    int size;
    int pages;
    int current;
    private List<DiamondIncomeAndExpenseBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<DiamondIncomeAndExpenseBean> getRecords() {
        return records;
    }

    public void setRecords(List<DiamondIncomeAndExpenseBean> records) {
        this.records = records;
    }

}
