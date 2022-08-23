package com.live.fox.utils.location;//package location;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.blankj.utilcode.util.LogUtils;
//import com.blankj.utilcode.util.StringUtils;
//import com.blankj.utilcode.util.Utils;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
//public class LocationManager implements AMapLocationListener {
//
//    private final String TAG = getClass().getSimpleName();
//
//    private LocationManager() {
//    }
//
//    private static class InstanceHolder {
//        private static LocationManager instance = new LocationManager();
//    }
//
//    public static LocationManager ins() {
//        return InstanceHolder.instance;
//    }
//
//    private AMapLocationClient mLocationClient = null;
//    private AMapLocationClientOption mLocationOption = null;
//    private String mCity;
//    private String mProvince;
//    private double latitude = 0.0;
//    private double longitude = 0.0;
//
//    /**
//     * 初始化
//     */
//    public void init() {
//        mLocationClient = new AMapLocationClient(Utils.getApp());
//        mLocationClient.setLocationListener(this);
//        mLocationOption = new AMapLocationClientOption();
//        //設置定位模式爲高精度模式，Battery_Saving爲低功耗模式，Device_Sensors是僅設備模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //設置是否返回地址信息（默認返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //設置是否只定位一次,默認爲false
//        mLocationOption.setOnceLocation(true);
//        //設置是否強制刷新WIFI，默認爲強制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //設置是否允許模擬位置,默認爲false，不允許模擬位置
//        mLocationOption.setMockEnable(false);
//        //設置定位間隔,單位毫秒,默認爲2000ms
//        mLocationOption.setInterval(2000);
//        //給定位客戶端對象設置定位參數
//        mLocationClient.setLocationOption(mLocationOption);
//        //啓動定位
//        mLocationClient.startLocation();
//    }
//
//    /**
//     * 重新定位
//     */
//    public void refreshLocation() {
//        if (null != mLocationClient) {
//            mLocationClient.startLocation();
//        }
//    }
//
//    public String getX() {
//        return latitude == 0 ? "0.0" : String.valueOf(latitude);
//    }
//
//    public String getY() {
//        return longitude == 0 ? "0.0" : String.valueOf(longitude);
//    }
//
//
//    /**
//     * 獲得 定位客戶端
//     *
//     * @param
//     */
//    public AMapLocationClient getLocationClient() {
//        return mLocationClient;
//    }
//
//    /**
//     * 獲得當前所在城市
//     *
//     * @param
//     */
//
//    public String getCity() {
//        refreshLocation();
//        if (StringUtils.isEmpty(mCity)) {
//            return "unknow";
//        }
//        LogUtils.e("getCity : " + mCity);
//        return mCity;
//    }
//
//    /**
//     * 獲取當前省份
//     *
//     * @return 省份名稱
//     */
//    public String getProvince() {
//        if (StringUtils.isEmpty(mProvince)) {
//            return "unknow";
//        }
//        return mProvince;
//    }
//
//    /**
//     * 獲取地理位置的經緯度信息
//     */
//    public Double[] getLatitudeAndLongitude() {
//        return new Double[]{latitude, longitude};
//    }
//
//    public void clear() {
//        this.mCity = null;
//        this.mProvince = null;
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null) {
//            if (aMapLocation.getErrorCode() == 0) {
//                LogUtils.e("定位调用成功");
//                //定位成功回調信息，設置相關消息
//                aMapLocation.getLocationType();//獲取當前定位結果來源，如網絡定位結果，詳見定位類型表
//                //獲取緯度
//                latitude = aMapLocation.getLatitude();
//                //獲取經度
//                longitude = aMapLocation.getLongitude();
//
//                //獲取精度信息
//                aMapLocation.getAccuracy();
//                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                //定位時間
//                df.format(date);
//                //地址，如果option中設置isNeedAddress爲false，則沒有此結果，網絡定位結果中會有地址信息，GPS定位不返回地址信息。
//                aMapLocation.getAddress();
//                //國家信息
//                aMapLocation.getCountry();
//                //省信息
//                mProvince = aMapLocation.getProvince();
//                //城市信息
//                mCity = aMapLocation.getCity();
//                LogUtils.e("mProvince : " + mProvince + ", mCity : " + mCity);
//                //城區信息
//                aMapLocation.getDistrict();
//                //街道信息
//                aMapLocation.getStreet();
//                //街道門牌號信息
//                aMapLocation.getStreetNum();
//                //城市編碼
//                aMapLocation.getCityCode();
//                //地區編碼
//                aMapLocation.getAdCode();
//                // aMapLocation.getAOIName();//獲取當前定位點的AOI信息
//
//            } else {
//                //顯示錯誤信息ErrCode是錯誤碼，errInfo是錯誤信息，詳見錯誤碼表。
//                LogUtils.e("aMapError", "location Error, ErrCode:"
//                        + aMapLocation.getErrorCode() + ", errInfo:"
//                        + aMapLocation.getErrorInfo());
//            }
//        }
//    }
//}
