package com.luck.keyboard.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.luck.keyboard.R;
import com.luck.keyboard.emoji.EmojiIndicatorView;
import com.luck.keyboard.emoji.EmojiUtil;
import com.luck.keyboard.emoji.RecentEmojiManager;
import com.luck.keyboard.entity.Emoji;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author：luck
 * project：ParadiseGolf
 * package：com.tongyu.luck.paradisegolf.activity
 * email：893855882@qq.com
 * data：16/6/26
 */
public class EmojiTabBFragment extends Fragment {
    private View rootView;
    private ViewPager vPager;
    private EmojiIndicatorView face_indicator;
    private int columns = 7;
    private int rows = 4;
    private ArrayList<View> ViewPagerItems = new ArrayList<>();
    private ArrayList<Emoji> emojiList;
    private ArrayList<Emoji> recentlyEmojiList;
    private OnEmojiClickListener listener;
    private RecentEmojiManager recentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        emojiList = EmojiUtil.getEmojiList();
        try {
            if (recentManager.getCollection(RecentEmojiManager.PREFERENCE_NAME) != null) {
                recentlyEmojiList = (ArrayList<Emoji>) recentManager.getCollection(RecentEmojiManager.PREFERENCE_NAME);
            } else {
                recentlyEmojiList = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof OnEmojiClickListener) {
            this.listener = (OnEmojiClickListener) activity;
        }
        recentManager = RecentEmojiManager.make(activity);
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.emoji_main, container, false);
        findViews();
        initViews();
        return rootView;
    }

    public void initViews() {
        initViewPager(emojiList);
    }

    private void initViewPager(ArrayList<Emoji> list) {
        intiIndicator(list);
        ViewPagerItems.clear();
        for (int i = 0; i < getPagerCount(list); i++) {
            ViewPagerItems.add(getViewPagerItem(i, list));
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(ViewPagerItems);
        vPager.setAdapter(mVpAdapter);
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                face_indicator.playBy(oldPosition, position);
                oldPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void findViews() {
        vPager = (ViewPager) rootView.findViewById(R.id.vPager);
        face_indicator = (EmojiIndicatorView) rootView.findViewById(R.id.face_indicator);
    }

    private void intiIndicator(ArrayList<Emoji> list) {
        face_indicator.init(getPagerCount(list));
    }

    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     *
     * @return
     */
    private int getPagerCount(ArrayList<Emoji> list) {
        int count = list.size();
        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
                : count / (columns * rows - 1) + 1;
    }

    private View getViewPagerItem(int position, ArrayList<Emoji> list) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_face_grid, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
         * */
        final List<Emoji> subList = new ArrayList<>();
        subList.addAll(list.subList(position * (columns * rows - 1),
                (columns * rows - 1) * (position + 1) > list
                        .size() ? list.size() : (columns
                        * rows - 1)
                        * (position + 1)));
        /**
         * 末尾添加删除图标
         * */
        if (subList.size() < (columns * rows - 1)) {
            for (int i = subList.size(); i < (columns * rows - 1); i++) {
                subList.add(null);
            }
        }
        Emoji deleteEmoji = new Emoji();
        deleteEmoji.setImageUri(R.mipmap.face_delete);
        subList.add(deleteEmoji);
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, getActivity());
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == columns * rows - 1) {
                    if (listener != null) {
                        listener.onEmojiDelete();
                    }
                    return;
                }
                if (listener != null) {
                    listener.onEmojiClick(subList.get(position));
                }
                insertToRecentList(subList.get(position));
            }
        });

        return gridview;
    }

    private void insertToRecentList(Emoji emoji) {
        if (emoji != null) {
            if (recentlyEmojiList.contains(emoji)) {
                //如果已经有该表情，就把该表情放到第一个位置
                int index = recentlyEmojiList.indexOf(emoji);
                Emoji emoji0 = recentlyEmojiList.get(0);
                recentlyEmojiList.set(index, emoji0);
                recentlyEmojiList.set(0, emoji);
                return;
            }
            if (recentlyEmojiList.size() == (rows * columns - 1)) {
                //去掉最后一个
                recentlyEmojiList.remove(rows * columns - 2);
            }
            // 二次去重复
            for (int i = 0; i < recentlyEmojiList.size(); i++) {
                Emoji emoji1 = recentlyEmojiList.get(i);
                if (emoji1.getContent().equals(emoji.getContent())) {
                    //如果已经有该表情，就把该表情放到第一个位置
                    int index = recentlyEmojiList.indexOf(emoji1);
                    Emoji emoji0 = recentlyEmojiList.get(0);
                    recentlyEmojiList.set(index, emoji0);
                    recentlyEmojiList.set(0, emoji1);
                    return;
                }
            }
            recentlyEmojiList.add(0, emoji);
            try {
                // 保存选过的
                recentManager.putCollection(RecentEmojiManager.PREFERENCE_NAME, recentlyEmojiList);
                EmojiTabAFragment.getOnRefresh().onRefreshEmj();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface onRefresh {
        void onRefreshEmj();
    }

    private class FaceVPAdapter extends PagerAdapter {
        // 界面列表
        private List<View> views;

        public FaceVPAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) (arg2));
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }

        // 判断是否由对象生成界
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }
    }

    public interface OnEmojiClickListener {
        void onEmojiDelete();

        void onEmojiClick(Emoji emoji);
    }


    class FaceGVAdapter extends BaseAdapter {
        private List<Emoji> list;
        private Context mContext;

        public FaceGVAdapter(List<Emoji> list, Context mContext) {
            super();
            this.list = list;
            this.mContext = mContext;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_face, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.face_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list.get(position) != null) {
                holder.iv.setImageBitmap(EmojiUtil.decodeSampledBitmapFromResource(getActivity().getResources(), list.get(position).getImageUri(),
                        EmojiUtil.dip2px(getActivity(), 32), EmojiUtil.dip2px(getActivity(), 32)));
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
        }
    }
}
