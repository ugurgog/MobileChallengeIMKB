package com.example.veriparkimkb.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.veriparkimkb.R;
import com.example.veriparkimkb.interfaces.ReturnSizeCallback;
import com.example.veriparkimkb.interfaces.ReturnStockListItemCallback;
import com.example.veriparkimkb.model.StockListItemModel;
import com.example.veriparkimkb.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class StockListItemAdapter extends RecyclerView.Adapter<StockListItemAdapter.CustomerHolder> {

    private Context context;
    private List<StockListItemModel> stockList = new ArrayList<>();
    private List<StockListItemModel> orgStockList = new ArrayList<>();

    private ReturnStockListItemCallback stockListItemCallback;

    public StockListItemAdapter(Context context, List<StockListItemModel> stockList,
                                ReturnStockListItemCallback stockListItemCallback) {
        this.context = context;
        this.stockList.addAll(stockList);
        this.orgStockList.addAll(stockList);
        this.stockListItemCallback = stockListItemCallback;
    }

    @Override
    public CustomerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item, parent, false);
        return new CustomerHolder(itemView);
    }

    public class CustomerHolder extends RecyclerView.ViewHolder {

        private TextView symbolTv;
        private TextView priceTv;
        private TextView differenceTv;
        private TextView volumeTv;
        private TextView buyingTv;
        private TextView salesTv;
        private ImageView changeImgv;
        private TextView changeTv;
        private CardView itemCv;

        private StockListItemModel stockModel;
        private int position;

        public CustomerHolder(View view) {
            super(view);

            symbolTv = view.findViewById(R.id.symbolTv);
            priceTv = view.findViewById(R.id.priceTv);
            differenceTv = view.findViewById(R.id.differenceTv);
            volumeTv = view.findViewById(R.id.volumeTv);
            buyingTv = view.findViewById(R.id.buyingTv);
            salesTv = view.findViewById(R.id.salesTv);
            changeImgv = view.findViewById(R.id.changeImgv);
            changeTv = view.findViewById(R.id.changeTv);
            itemCv = view.findViewById(R.id.itemCv);

            itemCv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stockListItemCallback.OnReturn(stockModel);
                }
            });
        }

        public void setData(StockListItemModel stockModel, int position) {
            this.stockModel = stockModel;
            this.position = position;

            if(stockModel != null){
                if(stockModel.getSymbol() != null)
                    symbolTv.setText(stockModel.getSymbol());

                priceTv.setText(String.valueOf(CommonUtils.round(stockModel.getPrice(), 2)));
                differenceTv.setText(String.valueOf(CommonUtils.round(stockModel.getDifference(), 2)));
                volumeTv.setText(String.valueOf(CommonUtils.round(stockModel.getVolume(), 2)));
                buyingTv.setText(String.valueOf(CommonUtils.round(stockModel.getBid(), 2)));
                salesTv.setText(String.valueOf(CommonUtils.round(stockModel.getOffer(), 2)));

                if(stockModel.getIsUp()){
                    Glide.with(context)
                            .load(R.drawable.ic_baseline_keyboard_arrow_up_24)
                            .into(changeImgv);
                }else if(stockModel.getIsDown()){
                    Glide.with(context)
                            .load(R.drawable.ic_baseline_keyboard_arrow_down_24)
                            .into(changeImgv);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(final CustomerHolder holder, final int position) {
        StockListItemModel stockModel = stockList.get(position);
        holder.setData(stockModel, position);
    }

    @Override
    public int getItemCount() {
        if(stockList != null)
            return stockList.size();
        else
            return 0;
    }

    public void updateAdapter(String searchText, ReturnSizeCallback returnSizeCallback) {
        if (searchText.trim().isEmpty()){
            stockList = orgStockList;
        } else {

            List<StockListItemModel> tempList = new ArrayList<>();

            for (StockListItemModel item : orgStockList) {
                if (item.getSymbol().toLowerCase().contains(searchText.toLowerCase()))
                    tempList.add(item);
            }
            stockList = tempList;
        }

        notifyDataSetChanged();

        if (stockList != null)
            returnSizeCallback.OnReturn(stockList.size());
        else
            returnSizeCallback.OnReturn(0);
    }
}