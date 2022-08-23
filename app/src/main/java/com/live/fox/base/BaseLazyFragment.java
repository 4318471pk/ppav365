package com.live.fox.base;

import android.view.View;
import android.view.ViewGroup;



/**
 * Author:  cheng
 * Date:    2018/3/9.
 * Description: Fragment懒加载(也就是只加载一次) 后面显示此Fragment时，显示原先加载好的数据
 * 注：子类的Fragment的onCreateView最终返回需要改成 return super.onCreateView(view)
 */
public abstract class BaseLazyFragment extends BaseFragment {


	//Fragment 的onCreateView最终返回需要改成 return super.onCreateView(view)
	public View onCreateView(View view){
		if (null == rootView) {
			this.rootView = view;
		}
		return this.rootView;
	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		Log.e(TAG, "BaseLazyFragment onCreateView: " );
//		if (null == rootView) {
//			rootView = inflater.inflate(layoutResID, null, false);
//			ButterKnife.bind(this, rootView);
//			initData(getArguments());
//			setView(rootView);
//		}
//		return rootView;
//	}


	//ViewPager+Fragment中Fragment不销毁/生命周期
	@Override
	public void onDestroyView() {
		super .onDestroyView();
		if (null != rootView) {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		}
	}



}