package com.spring.pagehelper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class PageInfo<T> implements Serializable {

    /**
     * 当前页数
     */
    private int pageNum;

    /**
     * 每页记录条数
     */
    private int pageSize;

    /**
     * 当前页的数量
     */
    private int size;

    /**
     * 总记录条数
     */
    private int total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 数据集
     */
    private List<T> list;
    /**
     * 上一页
     */
    private int prePage;

    /**
     * 下一页
     */
    private int nextPage;

    /**
     * 是否首页
     */
    private boolean isFirstPage=false;

    /**
     * 是否末页
     */
    private boolean isLastPage=false;

    /**
     * 有没有前一页
     */
    private boolean hasPreviousPage=false;

    /**
     * 有没有后一页
     */
    private boolean hasNextPage=false;

    /**
     * 导航页的数量
     */
    private int navigatePages;

    /**
     * 导航页的页数
     */
    private int[] navigatepageNums;

    /**
     * 起始导航页
     */
    private int navigateFirstPage;

    /**
     * 末尾导航页
     */
    private int navigateLastPage;

    public PageInfo(List<T> list){
        this(list,5);
    }

    public PageInfo(List<T> list,int pageSize){
        this(list,pageSize,4);
    }

    public PageInfo(List<T> list, int pageSize,int navigatePages) {
        if (list instanceof Collection) {
            this.pageSize=pageSize;
            this.pageNum = 1;
            this.total = list.size();
            this.pages = (int)Math.ceil(this.total * 1.0 / this.pageSize);
            this.list=list;
//            this.size = list.size();
            this.navigatePages = navigatePages;
            this.calcNavigatepageNums();
//            this.calcPage();
//            this.judgePageBoudary();
        }
    }

    private void calcNavigatepageNums() {
        if(pages>navigatePages){
            navigatepageNums=new int[navigatePages];
        }else {
            navigatepageNums=new int[pages];
        }
        for (int i=0;i<navigatepageNums.length;i++){
            navigatepageNums[i]=i+1;
        }
    }

    private void calcPage() {
        if(navigatepageNums!=null && navigatepageNums.length>0){
            navigateFirstPage=navigatepageNums[0];
            navigateLastPage=navigatepageNums[navigatepageNums.length-1];
            if(pageNum>1){
                prePage=pageNum-1;
            }
            if(pageNum<pages){
                nextPage=pageNum+1;
            }
        }
    }

    private void judgePageBoudary() {
        isFirstPage=pageNum==1;
        isLastPage=pageNum==pages;
        hasPreviousPage=pageNum>1;
        hasNextPage=pageNum<pages;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        calcPage();
        judgePageBoudary();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        int endIndex = pageNum * pageSize;
        List<T> l=list.subList((pageNum-1)*pageSize, (endIndex>list.size())?list.size():endIndex);
        return l;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }
}
