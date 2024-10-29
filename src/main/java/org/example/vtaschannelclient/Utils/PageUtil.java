package org.example.vtaschannelclient.Utils;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

public class PageUtil<T> {
    // 记录总数。
    private SimpleIntegerProperty totalRecord;
    // 每页记录数。
    private SimpleIntegerProperty pageSize;
    // 总页数。
    private SimpleIntegerProperty totalPage;
    // 数据源。
    private List<T> rowDataList;

    public PageUtil(List<T> rowDataList, int pageSize) {
        this.totalRecord = new SimpleIntegerProperty();
        this.totalPage = new SimpleIntegerProperty();
        this.rowDataList = rowDataList;
        this.pageSize = new SimpleIntegerProperty(pageSize);
        initialize();
    }

    public SimpleIntegerProperty getTotalPage() {
        return totalPage;
    }

    private void initialize() {
        // 设置记录总数。
        totalRecord.set(rowDataList.size());
        // 计算总页数。
        totalPage.set(
                totalRecord.get() % pageSize.get() == 0 ?
                        totalRecord.get() / pageSize.get() :
                        totalRecord.get() / pageSize.get() + 1);
        // 为每页记录数添加监听，变化时重新计算并设置总页数。
        pageSize.addListener((observable, oldVal, newVal) ->
                totalPage.set(
                        totalRecord.get() % pageSize.get() == 0 ?
                                totalRecord.get() / pageSize.get() :
                                totalRecord.get() / pageSize.get() + 1)
        );
    }

    public List<T> getCurrentPageDataList(int currentPage) {
        int fromIndex = pageSize.get() * currentPage;
        int tmp = pageSize.get() * currentPage + pageSize.get() - 1;
        int endIndex = tmp >= totalRecord.get() ? totalRecord.get() - 1 : tmp;
        // 返回数据范围：[fromIndex, toIndex)。
        return rowDataList.subList(fromIndex, endIndex + 1);
    }
}
