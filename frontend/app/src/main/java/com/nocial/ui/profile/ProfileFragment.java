package com.nocial.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nocial.R;
import com.nocial.databinding.FragmentProfileBinding;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

//    private FragmentProfileBinding binding;
    private TextView mTextView;
    private ProfileViewModel profileViewModel;
    private String user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mTextView = root.findViewById(R.id.text_profile);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        user = "dtsela"; // example userName --> get this user's data

        profileViewModel.getTextLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mTextView.setText(s);
            }
        });

        makeGetRequest();

        return root;
    }

    private void makeGetRequest() {
        OkHttpClient client = new OkHttpClient();

        String userUrl = ("http://10.0.2.2:5000/").concat(user);
        Request request = new Request.Builder().url(userUrl).build();

        // make async HTTP request to server
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected code:" + response);
                }

                String responseText = response.body().string();
                profileViewModel.setText(responseText);
            }
        });
    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}