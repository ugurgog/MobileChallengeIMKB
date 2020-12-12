package com.example.veriparkimkb.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.veriparkimkb.R;
import com.example.veriparkimkb.basefragments.BaseFragment;
import com.example.veriparkimkb.model.Error;
import com.example.veriparkimkb.model.HandshakeRequestModel;
import com.example.veriparkimkb.model.HandshakeResponseModel;
import com.example.veriparkimkb.rest.ApiClient;
import com.example.veriparkimkb.rest.RestInterface;
import com.google.gson.Gson;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class MainFragment extends BaseFragment{

    View mView;

    @BindView(R.id.showEndexesBtn)
    Button showEndexesBtn;

    private Context mContext;
    private RestInterface restInterface;
    private HandshakeResponseModel handshakeResponseModel = null;

    public MainFragment() {

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
            mView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.bind(this, mView);
            init();
            setListeners();
        }
        return mView;
    }

    private void init() {
        restInterface = ApiClient.getClient().create(RestInterface.class);
        final Call<HandshakeResponseModel> handshake = restInterface.getHandshake(getRequestModel());

        handshake.enqueue(new Callback<HandshakeResponseModel>() {
            @Override
            public void onResponse(Call<HandshakeResponseModel> call, Response<HandshakeResponseModel> response) {
                handshakeResponseModel = response.body();
                Log.i("Info", "call handshakeResponseModel:" + new Gson().toJson(handshakeResponseModel));

                if(handshakeResponseModel != null && handshakeResponseModel.getStatus() != null &&
                        handshakeResponseModel.getStatus().getIsSuccess()){

                    if(handshakeResponseModel.getAesKey() == null || handshakeResponseModel.getAesKey().isEmpty()){
                        Toast.makeText(mContext, "AES key NULL or EMPTY", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(handshakeResponseModel.getAesIV() == null || handshakeResponseModel.getAesIV().isEmpty()){
                        Toast.makeText(mContext, "AES IV NULL or EMPTY", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    showEndexesBtn.setEnabled(true);
                }else{
                    Error error = handshakeResponseModel.getStatus().getError();
                    Toast.makeText(mContext, "Error occurred handshake code:".concat(error.getCode().toString())
                            .concat(" message:")
                            .concat(error.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HandshakeResponseModel> call, Throwable t) {
                Toast.makeText(mContext, "Error occurred handshake rest service : ".concat(t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private HandshakeRequestModel getRequestModel(){

        HandshakeRequestModel handshakeRequestModel = new HandshakeRequestModel();
        handshakeRequestModel.setDeviceId(UUID.randomUUID().toString());
        handshakeRequestModel.setSystemVersion(Build.VERSION.RELEASE);
        handshakeRequestModel.setPlatformName("Android");
        handshakeRequestModel.setDeviceModel(Build.MODEL);
        handshakeRequestModel.setManifacturer(Build.MANUFACTURER);

        return handshakeRequestModel;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setListeners() {
        showEndexesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handshakeResponseModel != null)
                    mFragmentNavigation.pushFragment(new StockListFragment(handshakeResponseModel));
                else
                    Toast.makeText(mContext, "Error occurred handshake rest service !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}