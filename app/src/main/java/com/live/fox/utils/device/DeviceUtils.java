package com.live.fox.utils.device;

import static android.Manifest.permission.READ_PHONE_STATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ShellUtils;
import com.live.fox.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * getDeviceId      : 获取设备号
 * getIMEI          : 得到IMEI号
 * getUniqueIMEI    : 手动产生IMEI号,不需要任何的权限，非常方便
 * getManufacturer  : 获取设备厂商
 * getModel         : 获取设备型号
 * getCpuNum        : 获取CPU号
 * isDeviceRooted   : 设备是否 rooted
 * getSDKVersionName: 获取设备系统版本号
 * getSDKVersionCode: 获取设备系统版本码
 * getTotalMemory   : 获得系统总内存，GB
 * getAvailableMemory : 获取当前手机可用内存大小 MB
 * isHighEndPhone   : 手机是否高配
 * isExistSDCard    : 是否存在SD卡
 * isMeiTu          : 是否是美图手机
 * isMiUi           : 是否是小米系统
 * <p>
 * getScreenWidth   : 获取屏幕宽度
 * getScreenHeight  : 获取屏幕高度
 * <p>
 * dp2px            : dp转px
 * sp2px            : dp转px
 * px2dp            : px转dp
 * px2sp            : px转sp
 * <p>
 * getWidth         : 获取屏幕分辨宽 px
 * getHeight        : 获取屏幕分辨高 px
 * getRealHeight    : 获取手机屏幕真实高度
 * getScreenInch    : 获取屏幕物理尺寸，保留小数后一位，单位英寸
 * <p>
 * getStatusBarHeight     : 获取状态栏高度
 * getNavigationBarHeight : 获取状态栏高度
 * getNavigationBarHeight : 获取底部导航栏高度
 * checkDeviceHasNavigationBar   : 检查设备是否有NavigationBar
 * setMiuiStatusBarDarkMode      : 设置小米系统顶部状态栏模式
 * isNavigationBarShow           : 判断navigationbar是否显示
 * <p>
 * shutdown         : 关机
 * reboot           : 重启
 * reboot2Recovery  : 重启到 recovery
 * reboot2Bootloader: 重启到 bootloader
 * </pre>
 */
public final class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**My Add-----------------------------------------------------------------------------------*/

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static int px2dp(Context context, float pxValueue) {
        return (int) (pxValueue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转sp
     */
    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * 获取屏幕分辨宽，以px为单位
     */
    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕分辨高，以px为单位
     */
    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取底部导航栏高度
     * Utils.getApp()
     *
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        if (checkNavigationBarShow(context)) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            Log.v("dbw", "Navi height:" + height);
            return height;
        } else {
            return 0;
        }
    }

    /**
     * 判断虚拟导航栏是否显示
     *
     * @param context 上下文对象
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    public static boolean checkNavigationBarShow(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            //判断是否隐藏了底部虚拟导航
            int navigationBarIsMin = 0;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                navigationBarIsMin = Settings.System.getInt(context.getContentResolver(),
                        "navigationbar_is_min", 0);
            } else {
                navigationBarIsMin = Settings.Global.getInt(context.getContentResolver(),
                        "navigationbar_is_min", 0);
            }
            if ("1".equals(navBarOverride) || 1 == navigationBarIsMin) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }


    /**
     * 是否是美图手机
     */
    public static boolean isMeiTu() {
        String device = Build.MANUFACTURER;
        return ("Meitu").equals(device);
    }

    /**
     * 是否是小米系统
     *
     * @return
     */
    public static boolean isMiUi() {
        String device = Build.MANUFACTURER;
        return device.equals("Xiaomi");
    }

//    public static boolean isMiuiV5() {
//        InputStream inputStream = null;
//        try {
//            Properties properties = new Properties();
//            inputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
//            properties.load(inputStream);
//            String version = properties.getProperty("ro.miui.ui.version.name", null);
//            if (version != null) {
//                if (version.equals("V5")) {
//                    return true;
//                }
//            }
//            return false;
//        } catch (final IOException e) {
//            return false;
//        } finally {
//            try {
//                inputStream.close();
//            } catch (final IOException e) {
//            }
//        }
//    }

    public static boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取屏幕物理尺寸，保留小数后一位，单位英寸
     */
    public static double getScreenInch(Context context) {
        double mInch = 0.0d;

        try {
            int realWidth, realHeight;
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            if (Build.VERSION.SDK_INT >= 17) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (Build.VERSION.SDK_INT < 17 && Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }

            BigDecimal bd = new BigDecimal(Math.sqrt((realWidth / metrics.xdpi) * (realWidth / metrics.xdpi) + (realHeight / metrics.ydpi) * (realHeight / metrics.ydpi)));
            mInch = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInch;
    }


    public static int getCpuNum() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获得系统总内存，单位是GB
     */
    public static float getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String memoryOfString = "";
        long initialMemory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            if (str2 != null && !"".equals(str2)) {
                for (int i = 0; i < str2.length(); i++) {
                    char c = str2.charAt(i);
                    if (c >= 48 && c <= 57)
                        memoryOfString += c;
                }
            }
            initialMemory = Integer.valueOf(memoryOfString);
            localBufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return initialMemory / (1024 * 1024.0f);
    }

    /**
     * 获取当前手机可用内存大小，单位MB
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / (1024 * 1024);
    }

    public static boolean isHighEndPhone(Context context) {
        int cpu = getCpuNum();
        float memory = getTotalMemory();
        long availableMemory = getAvailableMemory(context);
        float dpi = context.getResources().getDisplayMetrics().density;
        return cpu >= 4 && memory >= 1.8 && availableMemory >= 200 && dpi >= 3.0;
    }

//    public static boolean isMIUI() {
//        String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
//        String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
//        String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
//        try {
//            final BuildProperties prop = BuildProperties.newInstance();
//            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
//                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
//        } catch (final IOException e) {
//            return false;
//        }
//    }

    public static int checkDeviceHasNavigationBar(Context context) {

        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            //获取NavigationBar的高度
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } else {
            return 0;
        }
    }

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    //判断navigationbar是否显示
    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }


    //获取手机屏幕真实高度
    public static int getRealHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        } else {
            display.getSize(size);
        }
        return size.y;
    }

    /**
     * 获取IMEI(需获取权限后才能调用这个方法)移动设备识别码
     */
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMEI() {
        if (ActivityCompat.checkSelfPermission(Utils.getApp(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            //有权限获取
            //获取IMEI码（必须获取权限后才能调用）
            TelephonyManager telephonyManager = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
            String imeiStr = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//如果在设备上执行了出厂重置或APK签名密钥更改，则该值可能会更改
                imeiStr = Settings.Secure.getString(Utils.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);//https://developer.android.com/reference/android/provider/Settings.Secure#ANDROID_ID
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imeiStr = telephonyManager.getImei();
            } else {
                imeiStr = telephonyManager.getDeviceId();
            }

            if (TextUtils.isEmpty(imeiStr)) {
                return getUniqueIMEI();
            } else {
                SPUtils.getInstance("DeviceInfo").put("imei", imeiStr);
                return imeiStr;
            }
        } else {
            //没权限获取 则返回本地的
            return SPUtils.getInstance("DeviceInfo").getString("imei", getUniqueIMEI());
        }
    }

    /**
     * 手动产生手机IMEI手机识别码
     * 通过读取设备的ROM版本号、厂商名、CPU型号和其他硬件信息来组合出一串15位的号码
     * “66”加上后面的13位一共15位，我们可以得到355715565309247这样一串号码，不需要任何的权限，非常方便。
     */
    public static String getUniqueIMEI() {
        String serial;
        String fakeIMEI = "24" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位
        try {
            serial = Build.FINGERPRINT;
            return new UUID(fakeIMEI.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // 随便一个初始化
        }
        return new UUID(fakeIMEI.hashCode(), serial.hashCode()).toString();
    }

    /**My Add-----------------------------------------------------------------------------------*/

    /**
     * Return whether device is rooted.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the version name of device's system.
     *
     * @return the version name of device's system
     */
    public static String getSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Return version code of device's system.
     *
     * @return version code of device's system
     */
    public static int getSDKVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Return the android id of device.
     *
     * @return the android id of device
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        try {
            String androidId = Settings.System.getString(Utils.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
            return androidId;
        } catch (Exception e) {
            return "000000000000000";
        }
    }

//    /**
//     * Return the MAC address.
//     * <p>Must hold
//     * {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
//     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
//     *
//     * @return the MAC address
//     */
//    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET})
//    public static String getMacAddress() {
//        String macAddress = getMacAddressByWifiInfo();
//        if (!"02:00:00:00:00:00".equals(macAddress)) {
//            return macAddress;
//        }
//        macAddress = getMacAddressByNetworkInterface();
//        if (!"02:00:00:00:00:00".equals(macAddress)) {
//            return macAddress;
//        }
//        macAddress = getMacAddressByInetAddress();
//        if (!"02:00:00:00:00:00".equals(macAddress)) {
//            return macAddress;
//        }
//        macAddress = getMacAddressByFile();
//        if (!"02:00:00:00:00:00".equals(macAddress)) {
//            return macAddress;
//        }
//        return "please open wifi";
//    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    private static String getMacAddressByWifiInfo() {
        try {
            Context context = Utils.getApp().getApplicationContext();
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMacAddressByFile() {
        ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = ShellUtils.execCmd("cat/sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    String address = result.successMsg;
                    if (address != null && address.length() > 0) {
                        return address;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    /**
     * Return the manufacturer of the product/hardware.
     * <p>e.g. Xiaomi</p>
     *
     * @return the manufacturer of the product/hardware
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * Return the model of device.
     * <p>e.g. MI2SC</p>
     *
     * @return the model of device
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * Shutdown the device
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.SHUTDOWN/>}
     * in manifest.</p>
     */
    public static void shutdown() {
        ShellUtils.execCmd("reboot -p", true);
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * Reboot the device.
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"} in manifest.</p>
     */
    public static void reboot() {
        ShellUtils.execCmd("reboot", true);
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        Utils.getApp().sendBroadcast(intent);
    }

    /**
     * Reboot the device.
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.REBOOT" />}</p>
     *
     * @param reason code to pass to the kernel (e.g., "recovery") to
     *               request special boot modes, or null.
     */
    public static void reboot(final String reason) {
        PowerManager mPowerManager =
                (PowerManager) Utils.getApp().getSystemService(Context.POWER_SERVICE);
        try {
            if (mPowerManager == null) return;
            mPowerManager.reboot(reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reboot the device to recovery.
     * <p>Requires root permission.</p>
     */
    public static void reboot2Recovery() {
        ShellUtils.execCmd("reboot recovery", true);
    }

    /**
     * Reboot the device to bootloader.
     * <p>Requires root permission.</p>
     */
    public static void reboot2Bootloader() {
        ShellUtils.execCmd("reboot bootloader", true);
    }
}
