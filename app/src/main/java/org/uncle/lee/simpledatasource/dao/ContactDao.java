package org.uncle.lee.simpledatasource.dao;

import java.util.List;
import org.uncle.lee.simpledatasource.entity.Contact;
import org.uncle.lee.simpledatasource.listener.DaoListener;

/**
 * Created by Austin on 2016/7/6.
 */
public class ContactDao implements Dao<Contact, String>{
  @Override public void init() {

  }

  @Override public void insert(List<Contact> contacts) {

  }

  @Override public void queryAll() {

  }

  @Override public boolean deleteByKeyword(String s) {
    return false;
  }

  @Override public boolean update(Contact origin, Contact target) {
    return false;
  }

  @Override public void clean() {

  }

  @Override public void setListener(DaoListener<Contact> daoListener) {

  }
}
