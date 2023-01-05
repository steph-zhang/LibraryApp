package com.example.library_system.Service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.library_system.Dao.*;
import com.example.library_system.Service.BookService;
import com.example.library_system.domain.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private SubjectDao subjectDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private ClassifyDao classifyDao;
    @Autowired
    private MakeTagDao makeTagDao;
    @Autowired
    private BorrowDao borrowDao;
    @Autowired
    private ReturnBookDao returnBookDao;

    @Override
    public boolean addBook(BookInfo bookInfo) {
        return bookDao.insert(bookInfo) > 0;
    }

    @Override
    public List<BookInfo> findBooks(BookInfo bookInfo) {
//        HashMap<String,Object> map = new HashMap<>();
        QueryWrapper wrapper = new QueryWrapper();
        if(bookInfo.getBookId() != null) {
            wrapper.eq("bookId",bookInfo.getBookId());
        }
        if(bookInfo.getTitle() != null){
            wrapper.like("title",bookInfo.getTitle());
        }
        if(bookInfo.getAuthor() != null){
            wrapper.like("author",bookInfo.getAuthor());
        }
        if(bookInfo.getTag() != null){
            wrapper.like("tag",bookInfo.getTag());
        }
        if(bookInfo == null){
            return bookDao.selectList(null);
        }
        return bookDao.selectList(wrapper);
    }

    @Override
    public boolean updateBook(BookInfo bookInfo) {
        return bookDao.updateById(bookInfo) > 0;
    }

    @Override
    public boolean deleteBook(BookInfo bookInfo) {
        QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id",bookInfo.getBookId());
        classifyDao.delete(queryWrapper);
        return bookDao.deleteById(bookInfo) > 0;
    }

    @Override
    public boolean addSubject(Subject subject,String tags) {

        if(subjectDao.insert(subject) < 0)
            return false;
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("subject_name",subject.getSubjectName());
        Subject subject1 = subjectDao.selectOne(wrapper);

        if(tags != null){
            String []arr = tags.split(",");
            for(String tag:arr){
                QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tag_name",tag);
                makeTagDao.insert(new MakeTag(subject1.getSubjectId(),tagDao.selectOne(queryWrapper).getTagId()));
            }
        }
        return true;
    }

    @Override
    public List<SubjectInfo> findSubjects(String subjectName) {
        List<SubjectInfo> subjectInfos = new ArrayList<>();
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        if(subjectName != null) {
            wrapper.like("subject_name",subjectName);
            List<Subject> subjectList =  subjectDao.selectList(wrapper);
            for(Subject subject:subjectList){
                List<String> tagList = subjectDao.querySubjectTags(subject.getSubjectName());
                String tags = new String();
                for(String str:tagList){
                    if(str == tagList.get(0))
                        tags = str;
                    else
                        tags = tags + "," + str;
                }
                SubjectInfo subjectInfo = new SubjectInfo(subject,tags);
                subjectInfos.add(subjectInfo);
            }
        }
        else{
            List<Subject> subjectList =  subjectDao.selectList(null);
            for(Subject subject:subjectList){
                List<String> tagList = subjectDao.querySubjectTags(subject.getSubjectName());
                String tags = null;
                for(String str:tagList){
                    if(str == tagList.get(0))
                        tags = str;
                    else
                        tags = tags + "," + str;
                }
                SubjectInfo subjectInfo = new SubjectInfo(subject,tags);
                subjectInfos.add(subjectInfo);
            }
        }

        return subjectInfos;
    }

    @Override
    public boolean updateSubject(int subjectId,String subjectName,String tags) {
        Subject subject = new Subject();
        subject.setSubjectId(subjectId);
        subject.setSubjectName(subjectName);

        subjectDao.updateById(subject);
        //先删掉再重新加
        QueryWrapper<MakeTag> wrapper = new QueryWrapper<>();
        wrapper.eq("subject_id",subjectId);
        makeTagDao.delete(wrapper);

        String []arr = tags.split(",");
        for(String tag:arr){
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tag_name",tag);



            makeTagDao.insert(new MakeTag(subjectId,tagDao.selectOne(queryWrapper).getTagId()));
        }

        return true;
    }

    @Override
    public boolean deleteSubject(String name) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("subject_name",name);
        return subjectDao.delete(wrapper) > 0;
    }

    @Override
    public boolean addTag(String name,String describe) {
        Tag tag = new Tag();
        tag.setTagName(name);
        tag.setDescription(describe);
        return tagDao.insert(tag) > 0;
    }

    @Override
    public List<Tag> findTags(String tagName) {
        QueryWrapper wrapper = new QueryWrapper();
        if(tagName != null) {
            wrapper.like("tag_name",tagName);
        }
        else{
            return tagDao.selectList(null);
        }
        return tagDao.selectList(wrapper);
    }

    @Override
    public boolean updateTag(String oldName,String newName,String describe) {
        //构建条件构造器
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_name",oldName);

        Tag tag = tagDao.selectOne(wrapper);
        if(tag == null)
            return false;
        Tag tag1 = new Tag();
        tag1.setTagId(tag.getTagId());
        tag1.setTagName(newName);
        tag1.setDescription(describe);
        System.out.println(tag1.toString());
        return tagDao.updateById(tag1) > 0;
    }

    @Override
    public boolean deleteTag(String name) {
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_name",name);
        return tagDao.delete(wrapper) > 0;
    }

    @Override
    public boolean classify(String bookId, int subjectId) {
        Classify classify = new Classify(bookId,subjectId);
        return classifyDao.insert(classify) > 0;
    }

    @Override
    public List<BookInfo> queryBooksOnATopic(String subjectName) {
        List<BookInfo> bookInfos = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("subject_name", subjectName);
        List<Subject> subjects = subjectDao.selectList(queryWrapper);
        for (Subject subject : subjects) {
            queryWrapper.clear();
            queryWrapper.eq("subject_id",subject.getSubjectId());
            List<Classify> classifies = classifyDao.selectList(queryWrapper);
            for(Classify classify:classifies){
                bookInfos.add(bookDao.selectById(classify.getBookId()));
            }
        }
        return bookInfos;

    }


    @Override
    public List<Tag> queryTagsOnATopic(String subjectName) {
        List<Tag> tags = new ArrayList<>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("subject_name", subjectName);
        List<Subject> subjects = subjectDao.selectList(queryWrapper);

        for (Subject subject : subjects) {
            queryWrapper.clear();
            queryWrapper.eq("subject_id",subject.getSubjectId());
            List<MakeTag> makeTags = makeTagDao.selectList(queryWrapper);
            for(MakeTag makeTag:makeTags){
                tags.add(tagDao.selectById(makeTag.getTagId()));
            }
        }
        return tags;
    }

    @Override
    public boolean makeTag(int subjectId, int tagId) {
        MakeTag makeTag = new MakeTag(subjectId,tagId);
        return makeTagDao.insert(makeTag) > 0;
    }

    @Transactional
    @Override
    public boolean borrow(String account, String bookId) {
        Borrow borrow = new Borrow();
        borrow.setBookId(bookId);
        borrow.setAccount(account);

        long time = System.currentTimeMillis();
        Date date = new Date(time);
        borrow.setDate(date);

        BookInfo bookInfo = bookDao.selectById(bookId);
        if(bookInfo.getRest() - 1 < 0){
            return false;
        }
        bookInfo.setRest(bookInfo.getRest() - 1);
        bookDao.updateById(bookInfo);
        return borrowDao.insert(borrow) > 0;
    }

    @Transactional
    @Override
    public boolean return_book(String account, String bookId) {
        ReturnBook returnBook = new ReturnBook();
        returnBook.setBookId(bookId);
        returnBook.setAccount(account);

        long time = System.currentTimeMillis();
        Date date = new Date(time);
        returnBook.setDate(date);

        //只有借过该书的人才能还这本书
        QueryWrapper<Borrow> wrapper = new QueryWrapper<>();
        wrapper
                .eq("account",account)
                .eq("book_id",bookId);
        List<Borrow> borrowList = borrowDao.selectList(wrapper);
        borrowDao.delete(wrapper);
        if(borrowList.size() == 0)
            return false;

        BookInfo bookInfo = bookDao.selectById(bookId);
        bookInfo.setRest(bookInfo.getRest() + 1);
        if(bookDao.updateById(bookInfo) <= 0)
            return false;

        return returnBookDao.insert(returnBook) > 0;
    }



//    @Override
//    public List<JSONObject> queryByAccount(String account, String username) throws JSONException {
//        List<JSONObject> bookInfos = new ArrayList<>();
//        if(username != null && account == null) {
//            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper
//                    .eq("username", username);
//            List<UserInfo> borrowerList = userDao.selectList(queryWrapper);
//
//            QueryWrapper<Borrow> queryWrapper1 = new QueryWrapper<>();
//            for(UserInfo borrower:borrowerList){
//                queryWrapper1.clear();
//                queryWrapper1
//                        .eq("account",borrower.getAccount());
//                List<Borrow> borrowList = borrowDao.selectList(queryWrapper1);
//                for(Borrow borrow:borrowList){
//                    JSONObject jsonObject = new JSONObject();
////                    jsonObject.put("bookId",bookDao.selectById(borrow.getBookId()).getBookId());
////                    jsonObject.put("title",bookDao.selectById(borrow.getBookId()).getTitle());
////                    jsonObject.put("rest",bookDao.selectById(borrow.getBookId()).getRest());
////                    jsonObject.put("author",bookDao.selectById(borrow.getBookId()).getAuthor());
////                    jsonObject.put("total",bookDao.selectById(borrow.getBookId()).getTotal());
////                    jsonObject.put("tag",bookDao.selectById(borrow.getBookId()).getTag());
//                    jsonObject.put("bookInfo",bookDao.selectById(borrow.getBookId()));
//                    jsonObject.put("date",borrow.getDate());
//                    bookInfos.add(jsonObject);
//                }
//            }
//        }
//        else{
//            QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
//            queryWrapper
//                    .eq("account",account);
//            List<Borrow> borrowList = borrowDao.selectList(queryWrapper);
//            for(Borrow borrow:borrowList){
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("bookId",bookDao.selectById(account).getBookId());
//                jsonObject.put("title",bookDao.selectById(account).getTitle());
//                jsonObject.put("rest",bookDao.selectById(account).getRest());
//                jsonObject.put("author",bookDao.selectById(account).getAuthor());
//                jsonObject.put("total",bookDao.selectById(account).getTotal());
//                jsonObject.put("tag",bookDao.selectById(account).getTag());
//                jsonObject.put("date",borrow.getDate());
//                bookInfos.add(jsonObject);
//            }
//        }
//        return bookInfos;
//    }
    //某人借的书
    @Override
    public List<BorrowBookInfo> queryByAccount(String account,String username){
        List<BorrowBookInfo> borrowBookInfos = new ArrayList<>();
        if(username != null && account == null) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("username", username);
            List<UserInfo> borrowerList = userDao.selectList(queryWrapper);

            QueryWrapper<Borrow> queryWrapper1 = new QueryWrapper<>();
            for(UserInfo borrower:borrowerList){
                queryWrapper1.clear();
                queryWrapper1
                        .eq("account",borrower.getAccount());
                List<Borrow> borrowList = borrowDao.selectList(queryWrapper1);
                for(Borrow borrow:borrowList){
                    BorrowBookInfo borrowBookInfo = new BorrowBookInfo(bookDao.selectById(borrow.getBookId()),borrow.getDate());
                    borrowBookInfos.add(borrowBookInfo);
                }
            }
        }
        else{
            System.out.println(account);
            QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("account",account);
            List<Borrow> borrowList = borrowDao.selectList(queryWrapper);
            for(Borrow borrow:borrowList){
                BorrowBookInfo borrowBookInfo = new BorrowBookInfo(bookDao.selectById(borrow.getBookId()),borrow.getDate());
                borrowBookInfos.add(borrowBookInfo);
            }
        }
        return borrowBookInfos;
    }
    //某书被谁借
    @Override
    public List<UserInfo> queryByBookId(String BookId,String title) {

        List<UserInfo> userInfos = new ArrayList<>();
        if(BookId == null && title != null){
            QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .like("title", title);
            List<BookInfo> borroweList = bookDao.selectList(queryWrapper);

            QueryWrapper<Borrow> queryWrapper1 = new QueryWrapper<>();

            for(BookInfo bookInfo:borroweList){
                queryWrapper1.clear();
                queryWrapper1
                        .eq("Book_id",bookInfo.getBookId());
                List<Borrow> borrowList = borrowDao.selectList(queryWrapper1);
                for(Borrow borrow:borrowList){
                    userInfos.add(userDao.selectById(borrow.getAccount()));
                }
            }
        }
        else {
            QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("book_id", BookId);
            List<Borrow> borrowList = borrowDao.selectList(queryWrapper);
            for (Borrow borrow : borrowList) {
                userInfos.add(userDao.selectById(borrow.getAccount()));
            }
        }
        return userInfos;
    }
    @Override
    public List<UserInfo> queryByBookId2(String BookId,String title) {
        List<UserInfo> userInfos = new ArrayList<>();
        userInfos = bookDao.queryUsersByTitle(title);
        return userInfos;
    }
}
