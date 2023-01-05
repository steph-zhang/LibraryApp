package com.example.library_system.controller;

import com.alibaba.excel.EasyExcel;
import com.example.library_system.Listener.ExcelListener;
import com.example.library_system.Service.BookService;
import com.example.library_system.domain.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/books")
@CrossOrigin
@Api(tags = "Book类")
public class BookController {

    @Autowired
    private BookService bookService;

    //对图书的操作
    @PostMapping(value = "/add_book")
    public boolean addBook(BookInfo bookInfo){
        //添加图书
        //如果没有该图书则添加图书
        //如果有该图书则total+1和amount+1
        List<BookInfo> list = bookService.findBooks(bookInfo);
        if(list.size() == 1){
            BookInfo bookInfo1 = list.get(0);
            bookInfo1.setRest(bookInfo1.getRest() + 1);
            bookInfo1.setTotal(bookInfo1.getTotal() + 1);
            return bookService.updateBook(bookInfo1);
        }
        else {
            return bookService.addBook(bookInfo);
        }
    }
    @PostMapping(value = "/find_book")
    public List<BookInfo> findBook(BookInfo bookInfo) {
        return bookService.findBooks(bookInfo);
    }
    @PostMapping(value = "/update_book")
    public boolean updateBook(BookInfo bookInfo) {
        return bookService.updateBook(bookInfo);
    }
    @PostMapping(value = "/delete_book")
    public boolean deleteBook(BookInfo bookInfo) {
        return bookService.deleteBook(bookInfo);
    }

    //批量添加图书
    @PostMapping(value = "/add_books")
    @ResponseBody
    public String importexcel(@RequestParam(value = "excelFile") MultipartFile file) throws IOException {
        System.out.println("开始解析文件~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        EasyExcel.read(file.getInputStream(), BookInfo.class, new ExcelListener(bookService)).sheet().doRead();
        return "success";
    }


    //对专题的操作
    @PostMapping(value = "/add_subject")
    public boolean addSubject(Subject subject,String tags) {
        return bookService.addSubject(subject,tags);
    }
    @PostMapping(value = "/find_subject")
    public List<SubjectInfo> findSubject(String subjectName) {
        return bookService.findSubjects(subjectName);
    }
    @PostMapping(value = "/update_subject")
    public boolean updateSubject(int subjectId,String subjectName,String tags) {
        return bookService.updateSubject(subjectId,subjectName,tags);
    }
    @PostMapping(value = "/delete_subject")
    public boolean deleteSubject(String subjectName) {
        return bookService.deleteSubject(subjectName);
    }

    //对标签的操作
    @PostMapping(value = "/add_tag")
    public boolean addSTag(String tagName,String description) {
        return bookService.addTag(tagName,description);
    }
    @PostMapping(value = "/find_tag")
    public List<Tag> findTag(String tagName) {
        return bookService.findTags(tagName);
    }
    @PostMapping(value = "/update_tag")
    public boolean updateTag(String oldName,String newName,String description) {
        return bookService.updateTag(oldName,newName,description);
    }
    @PostMapping(value = "/delete_tag")
    public boolean deleteTag(String tagName) {
        return bookService.deleteTag(tagName);
    }

    //借书
    @PostMapping(value = "/borrow")
    public boolean borrow(String account,String label) {
        return bookService.borrow(account,label);
    }
    //还书
    @PostMapping(value = "/return")
    public boolean return_book(String account,String label) {
        return bookService.return_book(account,label);
    }

    //分类
    @PostMapping(value = "/classify")
    public boolean classify(String bookId,int subjectId) {
        return bookService.classify(bookId,subjectId);
    }

    //打标签
    @PostMapping(value = "/make_tag")
    public boolean make_tag(int subjectId,int tagId) {
        return bookService.makeTag(subjectId,tagId);
    }

    //查询某人借书情况
    @PostMapping(value = "/borrow_book_info")
    public List<BorrowBookInfo> queryBorrowBookInfo(String account, String username) throws JSONException {
        return bookService.queryByAccount(account,username);
    }
    //查询某书借出情况
    @PostMapping(value = "/borrower_info")
    public List<UserInfo> queryBorrowerInfo(String bookId,String title) {
        return bookService.queryByBookId(bookId,title);
    }
    //查询某书借出情况1
    @PostMapping(value = "/borrower_info2")
    public List<UserInfo> queryBorrowerInfo2(String bookId,String title) {
        return bookService.queryByBookId2(bookId,title);
    }
    //查询专题下图书
    @PostMapping(value = "/query_books_on_a_topic")
    public List<BookInfo> queryBooksOnATopic(String subjectName){
        return bookService.queryBooksOnATopic(subjectName);
    }
    //查询专题下标签
    @PostMapping(value = "/query_tags_on_a_topic")
    public List<Tag> queryTagsOnATopic(String subjectName){
        return bookService.queryTagsOnATopic(subjectName);
    }
}
