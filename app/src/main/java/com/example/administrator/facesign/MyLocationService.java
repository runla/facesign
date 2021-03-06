package com.example.administrator.facesign;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.administrator.facesign.entity.MyLocation;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MyLocationService {
    public static MyLocationService myLocationService;

    private LocationClient mLocationClient = null;
    private Context mContext;
    private LocationCallbackListener listener;

    private MyLocationService(Context mContext) {
        this.mContext = mContext;
        mLocationClient = new LocationClient(mContext);
       // initLocation();
    }

    public synchronized static MyLocationService getInstance(Context context){
        if (myLocationService == null) {
            myLocationService = new MyLocationService(context);
        }
        return myLocationService;
    }

    //注册位置地理信息监听器,并且启动
    public void start(BDLocationListener listener){
        mLocationClient.registerLocationListener(listener);
        initLocation();
        mLocationClient.start();
    }

    //注销地理信息监听器，并且关闭
    public void stop(BDLocationListener listener){
        if (mLocationClient != null && mLocationClient.isStarted()){
            mLocationClient.unRegisterLocationListener(listener);
            mLocationClient.stop();
        }

    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

/*    private MyLocation getMyLocation(LocationCallbackListener listener){

    }*/
    //-----------------------------对外公布的方法-------------------------------
    public void getMyLocation(LocationCallbackListener listener){
        this.listener = listener;
        this.start(mLocationListener);
        return ;
    }

    //----------------------------内部接口------------------------
    public interface LocationCallbackListener{
        void succssReceive(MyLocation myLocation);
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mLocationListener = new BDLocationListener() {
        MyLocation myLocation=new MyLocation();
        @Override
        public void onReceiveLocation(BDLocation location) {

            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());

                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\nProvince : ");//省
                sb.append(location.getProvince());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\nbuildingId : ");
                sb.append(location.getBuildingID());
                sb.append("\nbuildingName : ");
                sb.append(location.getBuildingName());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息

                //

                myLocation.setTime(location.getTime());
                myLocation.setErrorCode(location.getLocType());
                myLocation.setLatitude(location.getLatitude());
                myLocation.setLongitude(location.getLongitude());
                myLocation.setCity(location.getCity());
                myLocation.setDistrict(location.getDistrict());
                myLocation.setStreet(location.getStreet());
                myLocation.setLocationdescribe(location.getLocationDescribe());
                myLocation.setProvince(location.getProvince());
                myLocation.setBuildingID(location.getBuildingID());
                myLocation.setBuildingName(location.getBuildingName());
                myLocation.setFloor(location.getFloor());
                myLocation.setAddr(location.getAddrStr());

                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                    myLocation.setDescribe("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                    myLocation.setDescribe("网络定位成功");

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                    myLocation.setDescribe("离线定位成功，离线定位结果也是有效的");

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    myLocation.setDescribe("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                    myLocation.setDescribe("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    myLocation.setDescribe("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.d("BaiduLocationApiDem",sb.toString());

                //回调位置
                listener.succssReceive(myLocation);
                //         show_location.setText(myLocation.getLocationdescribe());
                MyLocationService.getInstance(mContext).stop(mLocationListener);
                //   dataCallBack.onDataChange(myLocation);
            }
        }

    };
}
