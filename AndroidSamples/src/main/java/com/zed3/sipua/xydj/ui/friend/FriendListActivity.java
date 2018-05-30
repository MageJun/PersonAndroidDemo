package com.zed3.sipua.xydj.ui.friend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.lw.demo.android.samples.R;
import com.zed3.sipua.xydj.ui.adapter.FriendsListAdapter;
import com.zed3.sipua.xydj.ui.bean.FrindInfo;
import com.zed3.sipua.xydj.ui.friend.helper.GroupItemDecoration;
import com.zed3.sipua.xydj.ui.friend.helper.LetterSideBar;
import com.zed3.sipua.xydj.ui.helper.SpellHelperUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private RecyclerView mListView;
    private FriendsListAdapter mAdapter;
    private LetterSideBar mSideBar;
    private List<FrindInfo> mData ;
    private LetterSideBar.OnIndexSelectChanged mChangedListener = new LetterSideBar.OnIndexSelectChanged() {
        @Override
        public void onSelectChanged(int lastIndex, int curIndex,String letter) {
            if(mData!=null&&mData.size()>curIndex){
                int pos = findCurPos(letter);
                if(pos!=-1){
//                    smoothMoveToPosition(mListView,pos);
                    mListView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mListView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        }
    };

    private int findCurPos(String letter) {
        for (int i = 0;i<mData.size();i++){
            if(mData.get(i).getSpellName().toUpperCase().startsWith(letter)){
                return i;
            }
        }
        return -1;
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(final RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (mShouldScroll&&RecyclerView.SCROLL_STATE_IDLE==newState) {
                        mShouldScroll = false;
                        mRecyclerView.removeOnScrollListener(this);
                        smoothMoveToPosition(mRecyclerView, mToPosition);
                    }
                }
            };
            mRecyclerView.addOnScrollListener(mScrollListener);
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        initView();
        initData();
    }

    private void initView() {
        mListView = findViewById(R.id.listView);
        mSideBar = findViewById(R.id.sidebar);
    }

    private void initData(){
        mAdapter = new FriendsListAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        GroupItemDecoration decoration = new GroupItemDecoration(this,LinearLayout.VERTICAL);
        decoration.setOffset(4);
        decoration.setColor(getResources().getColor(R.color.xydj_bg));
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(lm);
        mListView.addItemDecoration(decoration);
        mListView.addOnScrollListener(mScrollListener);
        mData = createDecData(20);
        mSideBar.setSelectChangedListener(mChangedListener);

        //默认升序
        Collections.sort(mData, new Comparator<FrindInfo>() {
            @Override
            public int compare(FrindInfo o1, FrindInfo o2) {
                //#放到最开始的位置
                //其它非字母字符，放到后面
                // 获取ascii值,65～90为26个大写英文字母，97～122号为26个小写英文字母，其余为一些标点符号、运算符号
                int lhs_ascii = o1.getSpellName().toUpperCase().charAt(0);
                int rhs_ascii = o2.getSpellName().toUpperCase().charAt(0);
                if("#".equals(o1.getSpellName().substring(0,1))){
                    return -1;
                }else if (lhs_ascii < 65 || lhs_ascii > 90)
                    return 1;
                else if (rhs_ascii < 65 || rhs_ascii > 90)
                    return -1;
                else
                    return o1.getSpellName().compareTo(o2.getSpellName());
            }
        });
        decoration.setData(mData);
        mAdapter.addData(mData);

    }

    public final String[] NAMES = new String[] { "A","aa","a","aaa","aaaaaa","aba","宋江", "卢俊义", "吴用",
            "公孙胜", "关胜", "林冲", "秦明", "呼延灼", "花荣", "柴进", "李应", "朱仝", "鲁智深",
            "武松", "董平", "张清", "杨志", "徐宁", "索超", "戴宗", "刘唐", "李逵", "史进", "穆弘",
            "雷横", "李俊", "阮小二", "张横", "阮小五", " 张顺", "阮小七", "杨雄", "石秀", "解珍",
            " 解宝", "燕青", "朱武", "黄信", "孙立", "宣赞", "郝思文", "韩滔", "彭玘", "单廷珪",
            "魏定国", "萧让", "裴宣", "欧鹏", "邓飞", " 燕顺", "杨林", "凌振", "蒋敬", "吕方",
            "郭 盛", "安道全", "皇甫端", "王英", "扈三娘", "鲍旭", "樊瑞", "孔明", "孔亮", "项充",
            "李衮", "金大坚", "马麟", "童威", "童猛", "孟康", "侯健", "陈达", "杨春", "郑天寿",
            "陶宗旺", "宋清", "乐和", "龚旺", "丁得孙", "穆春", "曹正", "宋万", "杜迁", "薛永", "施恩",
            "周通", "李忠", "杜兴", "汤隆", "邹渊", "邹润", "朱富", "朱贵", "蔡福", "蔡庆", "李立",
            "李云", "焦挺", "石勇", "孙新", "顾大嫂", "张青", "孙二娘", " 王定六", "郁保四", "白胜",
            "时迁", "段景柱","#123","?1233d"};
    private List<FrindInfo> createDecData(int count){
        List<FrindInfo> infos = new ArrayList<>();
        for (int i = 0;i<NAMES.length;i++){
            FrindInfo info = new FrindInfo();
            String name = NAMES[i].trim();
            String spellName = SpellHelperUtils.converterToSpell(name);
            info.setSpellName(spellName);
            info.setName(name);
            info.setTag(spellName.toUpperCase().substring(0,1));
            infos.add(info);
        }
        return infos;
    }

    private List<GroupItemDecoration.ItemDecorationData> createItemDats(List<FrindInfo> infos){
        List<GroupItemDecoration.ItemDecorationData> datas = new ArrayList<>();
        datas.addAll(infos);
        return datas;
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(RecyclerView.SCROLL_STATE_IDLE == newState){
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    FrindInfo info = mData.get(firstItemPosition);
                    if(mSideBar!=null){
                        mSideBar.setCurrentLetter(info.getTag());
                    }
                }
            }
        }
    };
}