// IMyService.aidl
package com.chen.interview.aidl;
import com.chen.interview.aidl.Book;

// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
