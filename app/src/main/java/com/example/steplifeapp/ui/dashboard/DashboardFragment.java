package com.example.steplifeapp.ui.dashboard;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.steplifeapp.Bluetooth.Bt_module;
import com.example.steplifeapp.GlideImageGetter;
import com.example.steplifeapp.R;
import com.example.steplifeapp.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    Button buttonConnect;
    BluetoothDevice device;
    TextView DeviceText;
    BluetoothAdapter mBluetoothAdapter;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DeviceText  = view.findViewById(R.id.DeviceText);

        //Кнопка перехода к подключению модуля
        buttonConnect = view.findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Bt_module.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Получение значений через ключ
        Bundle arguments = getActivity().getIntent().getExtras();
        if (arguments!=null) {
            if (!arguments.isEmpty()) {
                device = (BluetoothDevice) arguments.get("Device");
                if (device != null) {
                    DeviceText.setText(device.getAddress());
                }
            }
        }

    }
}