package com.example.library_system.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.library_system.Service.BookService;
import com.example.library_system.domain.BookInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener extends AnalysisEventListener<BookInfo> {

    private BookService bookService;

    private List<BookInfo> list = new ArrayList<>();

    private static final int BATCH_COUNT = 1;

    public ExcelListener(BookService bookService1){
        bookService = bookService1;
    }
    @Override
    public void invoke(BookInfo bookInfo, AnalysisContext analysisContext) {
        System.out.println("解析到一条数据:========================"+bookInfo.toString());
        list.add(bookInfo);
        if(list.size() >= BATCH_COUNT){
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }
    private void saveData() {
        System.out.println("=============================="+list.size()+"条数据，开始存储到数据库");
        for(BookInfo bookInfo:list) {
            bookService.addBook(bookInfo);
        }
    }
}
