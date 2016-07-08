package org.uncle.lee.simpledatasource.data.center;

import android.content.Context;
import java.util.Collections;
import java.util.List;
import org.uncle.lee.simpledatasource.Entity.in.App;
import org.uncle.lee.simpledatasource.Entity.in.Contact;
import org.uncle.lee.simpledatasource.Utils.LogUtils;
import org.uncle.lee.simpledatasource.controller.CacheDataController;
import org.uncle.lee.simpledatasource.controller.UniDataController;
import org.uncle.lee.simpledatasource.listener.DataControllerListener;
import org.uncle.lee.simpledatasource.listener.UniDataCenterListener;

/**
 * Created by Austin on 2016/7/7.
 */
public class UniDataCenter implements DataCenter {
  public static final String TAG = UniDataCenter.class.getSimpleName();
  private static UniDataCenter uniDataCenter;
  private UniDataCenterListener listener;
  private UniDataController uniDataController;
  private Context mContext;
  private List<Contact> contactTemp;
  private List<App> appTemp;

  private UniDataCenter(Context mContext){
    this.mContext = mContext;
    uniDataController = new UniDataController(mContext);
  }

  public static UniDataCenter getInstance(Context mContext){
    if(uniDataCenter == null){
      synchronized (UniDataCenter.class){
        if(uniDataCenter == null){
          uniDataCenter = new UniDataCenter(mContext);
        }
      }
    }
    return uniDataCenter;
  }

  @Override public void queryContactList() {
    if(!isHasContactCacheData()){
      queryContactInFirstTime();
    }else {
      this.listener.onAction(UniDataCenterListener.ActionType.QUERY_ALL_DONE, true,
          uniDataController.cacheDataController().cacheContactList());
    }
  }

  private boolean isHasContactCacheData() {
    return uniDataController.cacheDataController().cacheContactList() != null;
  }

  private void queryContactInFirstTime() {
    uniDataController.contactDataController().setListener(new DataControllerListener<Contact>() {
      @Override public void onAction(ActionType Type, boolean isSuccess, List<Contact> contacts) {
        if(Type.equals(ActionType.QUERY_ALL_DONE) && isSuccess){
          UniDataCenter.this.listener.onAction(UniDataCenterListener.ActionType.QUERY_ALL_DONE, true, contacts);
          saveCacheContactList(contacts);
        }
      }
    });
    uniDataController.contactDataController().queryAll();
  }

  @Override public void insertContactList(List<Contact> contactList) {
    this.contactTemp = contactList;
    //this.contactTemp = Transformer.addPyForContact(mContext, contactList);
    uniDataController.contactDataController().setListener(new DataControllerListener<Contact>() {
      @Override public void onAction(ActionType Type, boolean isSuccess, List<Contact> contacts) {
        if(Type.equals(ActionType.INSERT_DONE) && isSuccess){
          UniDataCenter.this.listener.onAction(UniDataCenterListener.ActionType.INSERT_DONE, true, null);
          saveCacheContactList(contactTemp);
        }
      }
    });
    uniDataController.contactDataController().insert(contactList);
  }

  private void saveCacheContactList(List<Contact> contacts) {
    uniDataController.cacheDataController().saveCacheContactList(contacts);
  }

  @Override public void cleanContactList() {
    uniDataController.contactDataController().setListener(new DataControllerListener<Contact>() {
      @Override public void onAction(ActionType Type, boolean isSuccess, List<Contact> contacts) {
        if(Type.equals(ActionType.CLEAN_DONE) && isSuccess){
          UniDataCenter.this.listener.onAction(UniDataCenterListener.ActionType.CLEAN_DONE, true, null);
          saveCacheContactList(Collections.<Contact>emptyList());
        }
      }
    });
    uniDataController.contactDataController().clean();
  }

  @Override public void queryAppList() {
    if(!isHasAppCacheData()){
      queryAppInFirstTime();
    }else {
      this.listener.onAction(UniDataCenterListener.ActionType.QUERY_ALL_DONE, true,
          uniDataController.cacheDataController().cacheAppList());
    }
  }

  private boolean isHasAppCacheData(){
    return uniDataController.cacheDataController().cacheAppList() != null;
  }

  private void queryAppInFirstTime() {
    uniDataController.appDataController().setListener(new DataControllerListener<App>() {
      @Override public void onAction(ActionType Type, boolean isSuccess, List<App> apps) {
        if(Type.equals(ActionType.QUERY_ALL_DONE) && isSuccess){
          UniDataCenter.this.listener.onAction(UniDataCenterListener.ActionType.QUERY_ALL_DONE, true, apps);
          saveCacheAppList(apps, CacheDataController.SaveType.TYPE_ADD);
        }
      }
    });
    uniDataController.appDataController().queryAll();
  }

  private void saveCacheAppList(List<App> apps, CacheDataController.SaveType saveType) {
    LogUtils.d(TAG, "start cache app list ...");
    uniDataController.cacheDataController().saveCacheAppList(apps, saveType);
    LogUtils.d(TAG, "cache app list finish ...");
  }

  @Override public void insertAppList(List<App> appList) {
    // app don't need to get py params(this part is too wasting time)
    this.appTemp = appList;
    uniDataController.appDataController().setListener(new DataControllerListener<App>() {
      @Override public void onAction(ActionType Type, boolean isSuccess, List<App> apps) {
        if(Type.equals(ActionType.INSERT_DONE) && isSuccess){
          UniDataCenter.this.listener.onAction(UniDataCenterListener.ActionType.INSERT_DONE, true, null);
          saveCacheAppList(appTemp, CacheDataController.SaveType.TYPE_ADD);
        }
      }
    });
    uniDataController.appDataController().insert(appList);
  }

  @Override public void cleanAppList() {
    uniDataController.appDataController().setListener(new DataControllerListener<App>() {
      @Override public void onAction(ActionType Type, boolean isSuccess, List<App> apps) {
        UniDataCenter.this.listener.onAction(UniDataCenterListener.ActionType.CLEAN_DONE, true, null);
        saveCacheAppList(Collections.<App>emptyList(), CacheDataController.SaveType.TYPE_CLEAN);
      }
    });
    uniDataController.appDataController().clean();
  }

  @Override public void setListener(UniDataCenterListener listener) {
    this.listener = listener;
  }
}
