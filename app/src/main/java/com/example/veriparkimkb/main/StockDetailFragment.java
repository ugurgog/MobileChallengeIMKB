package com.example.veriparkimkb.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.veriparkimkb.R;
import com.example.veriparkimkb.basefragments.BaseFragment;
import com.example.veriparkimkb.model.Error;
import com.example.veriparkimkb.model.GraphicData;
import com.example.veriparkimkb.model.HandshakeResponseModel;
import com.example.veriparkimkb.model.StockDetailRequestModel;
import com.example.veriparkimkb.model.StockDetailResponseModel;
import com.example.veriparkimkb.rest.ApiClient;
import com.example.veriparkimkb.rest.RestInterface;
import com.example.veriparkimkb.utils.AESUtil;
import com.example.veriparkimkb.utils.CommonUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class StockDetailFragment extends BaseFragment {

    View mView;

    @BindView(R.id.symbolTv)
    TextView symbolTv;
    @BindView(R.id.dailyReduceTv)
    TextView dailyReduceTv;
    @BindView(R.id.priceTv)
    TextView priceTv;
    @BindView(R.id.dailyIncreaseTv)
    TextView dailyIncreaseTv;
    @BindView(R.id.differenceTv)
    TextView differenceTv;
    @BindView(R.id.perTv)
    TextView perTv;
    @BindView(R.id.volumeTv)
    TextView volumeTv;
    @BindView(R.id.topTv)
    TextView topTv;
    @BindView(R.id.buyingTv)
    TextView buyingTv;
    @BindView(R.id.bottomTv)
    TextView bottomTv;
    @BindView(R.id.salesTv)
    TextView salesTv;
    @BindView(R.id.changeTv)
    TextView changeTv;
    @BindView(R.id.changeImgv)
    ImageView changeImgv;

    private Context mContext;

    private HandshakeResponseModel handshakeResponseModel;
    private StockDetailResponseModel stockDetailResponseModel;
    private String stockId;
    private AESUtil aesUtil = null;

    private RestInterface restInterface;
    private ProgressDialog progressDialog;
    private LineChart lineChart;

    private ArrayList<Entry> x;

    public StockDetailFragment(HandshakeResponseModel handshakeResponseModel, String stockId) {
        this.handshakeResponseModel = handshakeResponseModel;
        this.stockId = stockId;
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
            mView = inflater.inflate(R.layout.fragment_stock_detail, container, false);
            ButterKnife.bind(this, mView);
            init();
        }
        return mView;
    }

    private void init() {
        lineChart = mView.findViewById(R.id.lineChart);
        setLineChartParams();
        aesUtil = new AESUtil(handshakeResponseModel.getAesKey(), handshakeResponseModel.getAesIV());
        restInterface = ApiClient.getClient().create(RestInterface.class);
        getStockDetail();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getStockDetail(){
        progressDialog = ProgressDialog.show(mContext,null,"Please wait...",false,false);
        final Call<StockDetailResponseModel> stockDetail
                = restInterface.getStockDetail(getStockDetailRequestModel(), handshakeResponseModel.getAuthorization());

        stockDetail.enqueue(new Callback<StockDetailResponseModel>() {
            @Override
            public void onResponse(Call<StockDetailResponseModel> call, Response<StockDetailResponseModel> response) {
                progressDialog.dismiss();
                stockDetailResponseModel = response.body();

                if(stockDetailResponseModel != null && stockDetailResponseModel.getStatus() != null){
                    if(stockDetailResponseModel.getStatus().getIsSuccess())
                        setViewParams(stockDetailResponseModel);
                    else {
                        Error error = stockDetailResponseModel.getStatus().getError();
                        if(error != null && error.getMessage() != null){
                            Toast.makeText(mContext, "Error occurred while getting stock detail code:".concat(String.valueOf(error.getCode()))
                                    .concat(" message:")
                                    .concat(error.getMessage()), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StockDetailResponseModel> call, Throwable t) {
                Toast.makeText(mContext, "Error occurred stock list rest service : ".concat(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLineChartParams(){
        x = new ArrayList<Entry>();
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getXAxis().setTextSize(15f);
        lineChart.getAxisLeft().setTextSize(15f);
        XAxis xl = lineChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setInverted(true);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setViewParams(StockDetailResponseModel stockDetailResponseModel) {
        if(stockDetailResponseModel.getSymbol() != null){
            try {
                String decryptedSymbol = aesUtil.aesDecrypt(stockDetailResponseModel.getSymbol());
                stockDetailResponseModel.setSymbol(decryptedSymbol);
            } catch (Exception e) {
                e.printStackTrace();
            }
            symbolTv.setText(mContext.getResources().getString(R.string.symbol).concat(": ").concat(stockDetailResponseModel.getSymbol()));
        }

        dailyReduceTv.setText(mContext.getResources().getString(R.string.daily_reduce).concat(": ")
                .concat(String.valueOf(CommonUtils.round(stockDetailResponseModel.getMinimum(), 2))));

        priceTv.setText(mContext.getResources().getString(R.string.price).concat(": ")
                .concat(String.valueOf(CommonUtils.round(stockDetailResponseModel.getPrice(), 2))));

        dailyIncreaseTv.setText(mContext.getResources().getString(R.string.daily_raise).concat(": ")
                .concat(String.valueOf(CommonUtils.round(stockDetailResponseModel.getMaximum(), 2))));

        differenceTv.setText(mContext.getResources().getString(R.string.difference).concat(": ")
                .concat(String.valueOf(CommonUtils.round(stockDetailResponseModel.getDifference(), 2))));

        perTv.setText(mContext.getResources().getString(R.string.per).concat(": ")
                .concat(String.valueOf(stockDetailResponseModel.getCount())));

        volumeTv.setText(mContext.getResources().getString(R.string.volume).concat(": ")
                .concat(String.valueOf(CommonUtils.round(stockDetailResponseModel.getVolume(), 2))));

        topTv.setText(mContext.getResources().getString(R.string.limit).concat(": ")
                .concat(String.valueOf(CommonUtils.round(stockDetailResponseModel.getHighest(), 2))));

        buyingTv.setText(mContext.getResources().getString(R.string.buying).concat(": ")
                .concat(String.valueOf(CommonUtils.round(stockDetailResponseModel.getBid(), 2))));

        bottomTv.setText(mContext.getResources().getString(R.string.floor).concat(": ")
                .concat(String.valueOf(CommonUtils.round(stockDetailResponseModel.getLowest(), 2))));

        salesTv.setText(mContext.getResources().getString(R.string.sale).concat(": ")
                .concat(String.valueOf(stockDetailResponseModel.getOffer())));

        changeTv.setText(mContext.getResources().getString(R.string.change).concat(": "));

        if(stockDetailResponseModel.getIsUp()){
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    .into(changeImgv);
        }else if(stockDetailResponseModel.getIsDown()){
            Glide.with(mContext)
                    .load(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    .into(changeImgv);
        }
        
        setGraphicData(stockDetailResponseModel.getGraphicData());
    }

    private void setGraphicData(List<GraphicData> graphicDataList) {
        for(GraphicData graphicData1 : graphicDataList){
            x.add(new Entry(graphicData1.getDay().floatValue(), graphicData1.getValue().floatValue()));
        }
        LineDataSet set1 = new LineDataSet(x, "");
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        set1.setLineWidth(1.5f);
        set1.setCircleRadius(4f);
        LineData data = new LineData(set1);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    private StockDetailRequestModel getStockDetailRequestModel(){
        StockDetailRequestModel stockDetailRequestModel = new StockDetailRequestModel();

        String encrptedStockId = null;
        try {
            encrptedStockId = aesUtil.aesEncrypt(stockId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stockDetailRequestModel.setId(encrptedStockId);
        return stockDetailRequestModel;
    }
}