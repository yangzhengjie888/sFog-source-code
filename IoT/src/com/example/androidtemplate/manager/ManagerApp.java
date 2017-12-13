package com.example.androidtemplate.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.androidtemplate.mo.User;
import com.example.androidtemplate.mo.WifiInfoc;
import com.example.androidtemplate.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 软件管理与初始化相关
 */
public class ManagerApp extends Application {

	private static Context appContext;

	private static List<Activity> activityList = new LinkedList<Activity>();

	@Override
	public void onCreate() {
		appContext = this;
		initImageLoader();
		initImageLoaderOptions();
		ManagerConf.initManagerConf(appContext);
		ManagerComm.loginUser = GsonUtil.getInstance().fromJson(ManagerConf.readFromLocal("login_user_kaoshi",""),User.class);

		ManagerComm.wifiInfoList = GsonUtil.getInstance().fromJson(ManagerConf.readFromLocal("wifi"),new TypeToken<List<WifiInfoc>>(){}.getType());
		if(ManagerComm.wifiInfoList ==null){
			ManagerComm.wifiInfoList = new ArrayList<WifiInfoc>();
		}

		initData();

		super.onCreate();
	}

	/**
	 * 清除内存数据
	 */
	public static void clearRam() {
		ManagerComm.loginUser = null;
		ManagerComm.displayImageOptions = null;
	}

	/**
	 * 退出软件 杀死播放进程、停止下载、退出界面
	 */
	public static void exitApp() {
		exitAllActivitives();
		clearRam();
	}

	public static void logout(){
		exitActivitivesLogout();
		clearRam();
	}
	
	public static Context getAppContext() {
		return appContext;
	}

	/**
	 * 添加Activity到容器中
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 关闭指定的Activity
	 * @param closeActivity
	 */
	public static void closeActivity(Activity closeActivity) {
		if(closeActivity == null || closeActivity.isFinishing()) {
			return;
		}

		for (Activity activity : activityList) {
			if(closeActivity.equals(activity)) {
				activity.finish();
			}
		}
	}

	/**
	 * 遍历所有Activity并finish
	 */
	public static void exitAllActivitives() {		
		for (Activity activity : activityList) {
			if(activity != null && !activity.isFinishing()) {
				activity.finish();
			}
		}
		System.exit(0);
	}

	/**
	 * 遍历所有Activity并finish 除了登录界面
	 */
	public static void exitActivitivesLogout() {
		for (Activity activity : activityList) {
			if(activity != null && !activity.isFinishing()) {
				activity.finish();
			}
		}
	}

	public static boolean hasActivity(Class<? extends Activity> sameClass) {
		for (Activity activity : activityList) {
			if(sameClass == activity.getClass()) {
				if(activity != null && !activity.isFinishing()) {
					return true;
				} else {
					return false;
				}
			}
		}

		return false;
	}

	public static boolean hasOtherActivity(Class<? extends Activity> sameClass) {
		for (Activity activity : activityList) {
			if(sameClass != activity.getClass()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 初始化图片加载器
	 */
	private void initImageLoader(){
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(appContext)
		.memoryCacheExtraOptions(640, 640) // max width, max height，即保存的每个缓存文件的最大长宽  
		//.discCacheExtraOptions(640, 640, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个  
		.threadPoolSize(3)//线程池内加载的数量  
		.threadPriority(Thread.NORM_PRIORITY - 4)
		.denyCacheImageMultipleSizesInMemory()  
		.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
		.memoryCacheSize(2 * 1024 * 1024)   
		.discCacheSize(50 * 1024 * 1024)    
		.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.discCacheFileCount(100) //缓存的文件数量  
		.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		.imageDownloader(new BaseImageDownloader(appContext, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
		.writeDebugLogs() // Remove for release app  
		.build();//开始构建  
		// Initialize ImageLoader with configuration. 
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 图片异步加载配置
	 */
	@SuppressWarnings("deprecation")
	private void initImageLoaderOptions(){
		ManagerComm.displayImageOptions = new DisplayImageOptions.Builder()
		//.showImageOnLoading(R.drawable.defaultpicture) //设置图片在下载期间显示的图片  
		//.showImageForEmptyUri(R.drawable.defaultpicture)//设置图片Uri为空或是错误的时候显示的图片  
		//.showImageOnFail(R.drawable.defaultpicture)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  		
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		//.displayer(new FadeInBitmapDisplayer(500))//渐变显示
		.build();//构建完成  
	}


	public static void initData(){

//
//		HttpUtil.post("StationServlet", null, new TextHttpResponseHandler() {
//			@Override
//			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//			}
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, String responseString) {
//				ManagerComm.stations = GsonUtil.getInstance().fromJson(new JsonData(responseString).getData(),new TypeToken<List<Station>>(){}.getType());
//				if(!ListUtil.listIsNotNull(ManagerComm.stations)){
//					ManagerComm.stations = new ArrayList<Station>();
//				}
//			}
//		});
//
//		HttpUtil.post("TicketServlet", null, new TextHttpResponseHandler() {
//			@Override
//			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//			}
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, String responseString) {
//				ManagerComm.tickets = GsonUtil.getInstance().fromJson(new JsonData(responseString).getData(),new TypeToken<List<Ticket>>(){}.getType());
//				if(!ListUtil.listIsNotNull(ManagerComm.tickets)){
//					ManagerComm.tickets = new ArrayList<Ticket>();
//				}
//			}
//		});
//
//		HttpUtil.post("OrderServlet", null, new TextHttpResponseHandler() {
//			@Override
//			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//			}
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, String responseString) {
//				ManagerComm.orders = GsonUtil.getInstance().fromJson(new JsonData(responseString).getData(),new TypeToken<List<Order>>(){}.getType());
//				if(!ListUtil.listIsNotNull(ManagerComm.orders)){
//					ManagerComm.orders = new ArrayList<Order>();
//				}
//			}
//		});

	}
}
