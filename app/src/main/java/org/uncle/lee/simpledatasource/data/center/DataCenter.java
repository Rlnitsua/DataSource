package org.uncle.lee.simpledatasource.data.center;

import java.util.List;
import org.uncle.lee.simpledatasource.Entity.App;
import org.uncle.lee.simpledatasource.Entity.Contact;
import org.uncle.lee.simpledatasource.listener.UniDataCenterListener;

/**
 * Created by Austin on 2016/7/7.
 */
public interface DataCenter {
  public abstract void queryContactList();
  public abstract void insertContactList(List<Contact> contactList);
  public abstract void cleanContactList();
  public abstract void queryAppList();
  public abstract void insertAppList(List<App> appList);
  public abstract void cleanAppList();

  public abstract void setListener(UniDataCenterListener listener);
}
