package com.example.library_system.Service;

import com.example.library_system.domain.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;

public interface BookService {
    boolean addBook(BookInfo bookInfo);
    List<BookInfo> findBooks(BookInfo bookInfo);
    boolean updateBook(BookInfo bookInfo);
    boolean deleteBook(BookInfo bookInfo);

    boolean addSubject(Subject subject,String tags);
    List<SubjectInfo> findSubjects(String subjectName);
    boolean updateSubject(int subjectId,String subjectName,String tags);
    boolean deleteSubject(String name);

    boolean addTag(String name,String describe);
    List<Tag> findTags(String tagName);
    boolean updateTag(String oldName,String newName,String describe);
    boolean deleteTag(String name);

    //为专题添加书本
    boolean classify(String bookId,int subjectId);

    //为专题打标签
    boolean makeTag(int subjectId,int tagId);

    //查某专题下图书
    List<BookInfo> queryBooksOnATopic(String subjectName);
    //查某专题下标签
    List<Tag> queryTagsOnATopic(String subjectName);




    //借书还书
    boolean borrow(String account,String bookId);
    boolean return_book(String account,String bookId);

    //查询借书情况
    List<BorrowBookInfo> queryByAccount(String account, String username) throws JSONException;
    List<UserInfo> queryByBookId(String BookId,String title);
    List<UserInfo> queryByBookId2(String BookId,String title);

}
