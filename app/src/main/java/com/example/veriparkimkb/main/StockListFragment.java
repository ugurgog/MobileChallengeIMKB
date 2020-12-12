package com.example.veriparkimkb.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.veriparkimkb.R;
import com.example.veriparkimkb.basefragments.BaseFragment;
import com.example.veriparkimkb.enums.PeriodTypeEnum;
import com.example.veriparkimkb.interfaces.ReturnSizeCallback;
import com.example.veriparkimkb.interfaces.ReturnStockListItemCallback;
import com.example.veriparkimkb.main.adapters.StockListItemAdapter;
import com.example.veriparkimkb.model.Error;
import com.example.veriparkimkb.model.HandshakeResponseModel;
import com.example.veriparkimkb.model.StockListItemModel;
import com.example.veriparkimkb.model.StockListRequestModel;
import com.example.veriparkimkb.model.StockListResponseModel;
import com.example.veriparkimkb.rest.ApiClient;
import com.example.veriparkimkb.rest.RestInterface;
import com.example.veriparkimkb.utils.AESUtil;
import com.example.veriparkimkb.utils.CommonUtils;
import com.google.android.material.navigation.NavigationView;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class StockListFragment extends BaseFragment {

    View mView;

    @BindView(R.id.searchEdittext)
    EditText searchEdittext;
    @BindView(R.id.searchCancelImgv)
    ImageView searchCancelImgv;
    @BindView(R.id.searchResultTv)
    TextView searchResultTv;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.menuImgv)
    ImageView menuImgv;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navViewLayout)
    NavigationView navViewLayout;

    private Context mContext;
    private StockListItemAdapter stockListItemAdapter;
    private StockListResponseModel stockListResponseModel;

    private HandshakeResponseModel handshakeResponseModel;
    private Map<String, String> headerMap;
    private AESUtil aesUtil = null;

    private RestInterface restInterface;
    private boolean mDrawerState;
    private ProgressDialog progressDialog;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public StockListFragment(HandshakeResponseModel handshakeResponseModel) {
        this.handshakeResponseModel = handshakeResponseModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_stock_list, container, false);
            ButterKnife.bind(this, mView);
            init();
            setListeners();
        }
        return mView;
    }

    private void init() {
        setHeaderMap();
        aesUtil = new AESUtil(handshakeResponseModel.getAesKey(), handshakeResponseModel.getAesIV());
        restInterface = ApiClient.getClient().create(RestInterface.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerview.setLayoutManager(linearLayoutManager);
        getCurrentStockList(PeriodTypeEnum.PERIOD_ALL);
    }

    private void setHeaderMap() {
        headerMap = new HashMap<>();
        headerMap.put("X-VP-Authorization" , handshakeResponseModel.getAuthorization());
        headerMap.put("Content-Type","application/json");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setListeners() {
        menuImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuImgv.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.image_click));
                if (mDrawerState) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().trim().isEmpty()) {
                    updateAdapter(s.toString());
                    searchCancelImgv.setVisibility(View.VISIBLE);
                } else {
                    updateAdapter("");
                    searchCancelImgv.setVisibility(View.GONE);
                }
            }
        });

        searchCancelImgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdittext.setText("");
                searchCancelImgv.setVisibility(View.GONE);
                CommonUtils.showKeyboard(mContext,false, searchEdittext);
            }
        });
        setDrawerListeners();
    }

    public void setDrawerListeners() {
        drawerLayout.addDrawerListener(new ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                null,
                0,
                0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerState = false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerState = true;
            }
        });

        navViewLayout.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.share_and_endexes:
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    getCurrentStockList(PeriodTypeEnum.PERIOD_ALL);

                    break;

                case R.id.increases:
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    getCurrentStockList(PeriodTypeEnum.PERIOD_INCREASING);
                    break;

                case R.id.reduces:
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    getCurrentStockList(PeriodTypeEnum.PERIOD_DECREASING);
                    break;

                case R.id.volume_thirty:
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    getCurrentStockList(PeriodTypeEnum.PERIOD_VOLUME30);
                    break;

                case R.id.volume_fifty:
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    getCurrentStockList(PeriodTypeEnum.PERIOD_VOLUME50);
                    break;

                case R.id.volume_hundred:
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    getCurrentStockList(PeriodTypeEnum.PERIOD_VOLUME100);
                    break;

                default:
                    break;
            }

            return false;
        });
    }

    public void updateAdapter(String searchText) {
        if (searchText != null && stockListItemAdapter != null) {
            stockListItemAdapter.updateAdapter(searchText, new ReturnSizeCallback() {
                @Override
                public void OnReturn(int size) {
                    if (size == 0 && (stockListResponseModel != null && stockListResponseModel.getStocks().size() > 0))
                        searchResultTv.setVisibility(View.VISIBLE);
                    else
                        searchResultTv.setVisibility(View.GONE);
                }
            });
        }
    }

    public void getCurrentStockList(PeriodTypeEnum periodType){
        progressDialog = ProgressDialog.show(mContext,null,"Please wait...",false,false);
        StockListRequestModel stockListRequestModel = getStockListRequestModel(periodType);
        final Call<StockListResponseModel> stockList = restInterface.getStockList(headerMap, stockListRequestModel);

        stockList.enqueue(new Callback<StockListResponseModel>() {
            @Override
            public void onResponse(Call<StockListResponseModel> call, Response<StockListResponseModel> response) {
                progressDialog.dismiss();
                stockListResponseModel = response.body();

                if(stockListResponseModel != null && stockListResponseModel.getStatus() != null){
                    if(stockListResponseModel.getStatus().getIsSuccess())
                        updateAdapterWithCurrentList(stockListResponseModel);
                    else {
                        Error error = stockListResponseModel.getStatus().getError();
                        if(error != null){
                            Toast.makeText(mContext, "Error occurred while getting stock list code:".concat(error.getCode().toString())
                                    .concat(" message:")
                                    .concat(error.getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StockListResponseModel> call, Throwable t) {
                Toast.makeText(mContext, "Error occurred stock list rest service : ".concat(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAdapterWithCurrentList(StockListResponseModel stockListResponseModel) {
        decryptSymbols(stockListResponseModel);
        stockListItemAdapter = new StockListItemAdapter(mContext, stockListResponseModel.getStocks(), new ReturnStockListItemCallback() {
            @Override
            public void OnReturn(StockListItemModel stockListItemModel) {
                if(stockListItemModel != null && stockListItemModel.getId() != 0){
                    mFragmentNavigation.pushFragment(new StockDetailFragment(handshakeResponseModel, stockListItemModel.getId().toString()));
                }
            }
        });
        recyclerview.setAdapter(stockListItemAdapter);
    }

    private void decryptSymbols(StockListResponseModel stockListResponseModel){
        for(StockListItemModel stockListItemModel : stockListResponseModel.getStocks()){
            try {
                String decryptedSymbol = aesUtil.aesDecrypt(stockListItemModel.getSymbol());
                stockListItemModel.setSymbol(decryptedSymbol);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private StockListRequestModel getStockListRequestModel(PeriodTypeEnum periodType){
        StockListRequestModel stockListRequestModel = new StockListRequestModel();

        String aesEncrypted = null;
        try {
            aesEncrypted = aesUtil.aesEncrypt(periodType.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        stockListRequestModel.setPeriod(aesEncrypted);
        return stockListRequestModel;
    }
}