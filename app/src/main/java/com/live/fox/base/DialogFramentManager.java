package com.live.fox.base;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.live.fox.dialog.UpdateFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * <p>类描述：  DialogFrament 管理类
 * <p>创建人：Simon
 * <p>创建时间：2019-02-14
 * <p>修改人：Simon
 * <p>修改时间：2019-02-14
 * <p>修改备注：
 **/
public class DialogFramentManager {
    List<BaseDialogFragment> dialogFragments = new ArrayList<>();
    public static DialogFramentManager mDialogFramentManage;

    public DialogFramentManager() {
        dialogFragments.clear();
    }

    public static DialogFramentManager getInstance() {
        if (mDialogFramentManage == null) {
            mDialogFramentManage = new DialogFramentManager();
        }
        return mDialogFramentManage;
    }


    public void addDialog(BaseDialogFragment dialogFragment) {
        dialogFragments.add(dialogFragment);
    }

    public void removeDialog(BaseDialogFragment dialogFragment) {
        dialogFragments.remove(dialogFragment);
        dialogFragment = null;
    }

    public synchronized void showDialog(FragmentManager supportFragmentManager, BaseDialogFragment dialogFragment) {
        if (!dialogFragments.contains(dialogFragment)) {
            dialogFragments.add(dialogFragment);
            FragmentTransaction ft = supportFragmentManager.beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.add(dialogFragment, dialogFragment.getClass().getSimpleName());
//            ft.commitAllowingStateLoss();
//            dialogFragment.show(ft, dialogFragment.getClass().getSimpleName());
            dialogFragment.show(supportFragmentManager, dialogFragment.getClass().getSimpleName());

        } else {
            Log.i("TT", "已经show了");
        }

    }

    //此方法为了Can not perform this action after onSaveInstanceState这个异常
    //commitAllowingStateLoss是防止错误的关键 仿照dialogFragment.show()的方法 没事就不用这个 可能导致某些东西错乱
    public synchronized void showDialogAllowingStateLoss(FragmentManager supportFragmentManager, BaseDialogFragment dialogFragment) {
        if (!dialogFragments.contains(dialogFragment) ) {
            try {
               Field mDismissed= dialogFragment.getClass().getSuperclass().getSuperclass().getDeclaredField("mDismissed");
                mDismissed.setAccessible(true);
                mDismissed.set(dialogFragment,false);

                Field mShownByMe= dialogFragment.getClass().getSuperclass().getSuperclass().getDeclaredField("mShownByMe");
                mShownByMe.setAccessible(true);
                mShownByMe.set(dialogFragment,true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FragmentTransaction ft = supportFragmentManager.beginTransaction();
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.add(dialogFragment, dialogFragment.getClass().getName());
            ft.commitAllowingStateLoss();
            dialogFragments.add(dialogFragment);
//            dialogFragment.show(ft, dialogFragment.getClass().getSimpleName());
        } else {
            Log.i("TT", "已经show了");
        }

    }

    /**
     * 关闭全部dialog
     */
    public void clearDialog() {
        for (int i = 0; i < dialogFragments.size(); i++) {
            if(dialogFragments.get(i) instanceof UpdateFragment)
            {
                //任何情况下升级弹窗都不取消
            }
            else
            {
                if(dialogFragments.get(i).isConditionOk() && !dialogFragments.get(i).isRemoving())
                {
                    dialogFragments.get(i).dismissAllowingStateLoss();
                }
            }
        }
        dialogFragments.clear();
    }

    /**
     * 重后关闭dialog
     *
     * @param num 关闭几个
     */
    public void clearDialog(int num) {
        for (int i = 0; i < num; i++) {
            dialogFragments.get(dialogFragments.size() - 1).dismiss();
            dialogFragments.remove(dialogFragments.size() - 1);
        }
    }

    /**
     * 关闭除了最后一个以外的dialog
     */
    public void retainLastDialog(FragmentManager supportFragmentManager, BaseDialogFragment dialogFragment) {
//        for (int i = 0; i < dialogFragments.size(); i++) {
//            dialogFragments.get(dialogFragments.size() - 2).dismiss();
//            dialogFragments.remove(dialogFragments.size() - 2);
//        }
        if (supportFragmentManager == null || dialogFragment == null) {
            return;
        }
        if (dialogFragments.size() != 0) {
            if (dialogFragments.get(0).getTag().equals("LoginDialogFragment")) {
                return;
            }
        }
        for (DialogFragment data : dialogFragments) {
            data.dismiss();
        }
        dialogFragments.clear();
        showDialog(supportFragmentManager, dialogFragment);
    }

    /**
     * 是否在show dialog
     */
    public boolean isShowLoading(String name) {
        if (dialogFragments.size() == 0) {
            return false;
        }
        if(dialogFragments.get(dialogFragments.size() - 1)==null || dialogFragments.get(dialogFragments.size() - 1).getTag()==null)
        {
            return false;
        }
        String tag=dialogFragments.get(dialogFragments.size() - 1).getClass().getName();
        boolean isShow = tag.equals(name);
        return isShow;
    }


}
