package com.hbw.bookstore.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hbw.bookstore.entities.Book;
import com.hbw.bookstore.mapper.BookDAO;
import com.hbw.bookstore.service.IBookService;

@Service
public class BookService implements IBookService{

	@Resource
	BookDAO bookdao;
	
	@Resource
	RedisCacheStorageImpl cacheStore;
	
	public List<Book> getAllBooks() {
		List<Book> list = new ArrayList<Book>();
		Map<String, Book> cacheMap = new HashMap<String, Book>();
		cacheMap = cacheStore.hget("book");
		if(!cacheMap.isEmpty()){
			 Iterator<Map.Entry<String, Book>> it = cacheMap.entrySet().iterator();
			   while(it.hasNext()){
			       Map.Entry<String, Book> entry = it.next();
			       Book book = entry.getValue();
			       list.add(book);
			   }
			   return list;
		}else{
			list = bookdao.getAllBooks();
			if(!list.isEmpty()){
				//加入缓存
				for(Book book : list){
					cacheStore.hset("book", book.getTitle(), book);
				}
			}
			return list;
		}
	}
	
	public Book getBookById(int id){
		Book book = bookdao.getBookById(id);
		if(null != book){
			//加入缓存
		}
		return book;
	}
	
	public int add(Book entity) throws Exception {
		if(entity.getTitle()==null||entity.getTitle().equals("")){
			throw new Exception("书名必须不为空");
		}else{
			//加入缓存
			cacheStore.hset("book", entity.getTitle(), entity);
		}
		
		return bookdao.add(entity);
	}
	
	@Transactional
	public int add(Book entity1,Book entityBak){
		int rows=0;
		rows=bookdao.add(entity1);
		rows=bookdao.add(entityBak);
		return rows;
	}

	public int delete(int id) {
		int num = bookdao.delete(id);
		Book book = bookdao.getBookById(id);
		if(null != book){
			//删除缓存
		}
		return num;
	}
	
	/**
	 * 多删除
	 */
	public int delete(String[] ids){
		int rows=0;
		for (String idStr : ids) {
			int id=Integer.parseInt(idStr);
			rows+=delete(id);
			Book book = bookdao.getBookById(Integer.parseInt(idStr));
			if(null != book){
				//删除缓存
			}
		}
		return rows;
	}

	public int update(Book entity) {
		int num = bookdao.update(entity);
		//更新缓存
		
		return num;
	}

}
