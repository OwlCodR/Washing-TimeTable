package com.example.cablewaycontrol;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothClassicService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothWriter;

import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Button buttonM1, buttonM2, buttonM3, buttonM4, buttonSearch;
    private Switch switchBluetooth;
    private ImageView imageViewCableway;
    private LinearLayout linearLayoutButtons;
    private TextView textViewSpeed;
    private SeekBar seekBarSpeed;

    private BluetoothService service;
    private ProgressBar progressBarSearch;

    private HashMap<String,String> hashMapMotors = new HashMap<>();
    private String speed = "1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewSpeed = findViewById(R.id.textViewSpeed);
        seekBarSpeed = findViewById(R.id.seekBarSpeed);
        seekBarSpeed.setOnSeekBarChangeListener(this);

        buttonM1 = findViewById(R.id.buttonMotor1);
        buttonM2 = findViewById(R.id.buttonMotor2);
        buttonM3 = findViewById(R.id.buttonMotor3);
        buttonM4 = findViewById(R.id.buttonMotor4);
        buttonSearch = findViewById(R.id.buttonStartSearch);

        buttonM1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                service = BluetoothService.getDefaultInstance();
                BluetoothWriter writer = new BluetoothWriter(service);

                buttonM1.setTextSize(24);
                buttonM1.setBackgroundColor(getResources().getColor(R.color.colorGray));
                buttonM1.setText("M1");
                writer.writeln("-10");
                hashMapMotors.put("M1", "-10");
                return true;
            }
        });
        buttonM2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                service = BluetoothService.getDefaultInstance();
                BluetoothWriter writer = new BluetoothWriter(service);

                buttonM2.setTextSize(24);
                buttonM2.setBackgroundColor(getResources().getColor(R.color.colorGray));
                buttonM2.setText("M2");
                writer.writeln("-30");
                hashMapMotors.put("M2", "-30");
                return true;
            }
        });
        buttonM3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                service = BluetoothService.getDefaultInstance();
                BluetoothWriter writer = new BluetoothWriter(service);

                buttonM3.setTextSize(24);
                buttonM3.setBackgroundColor(getResources().getColor(R.color.colorGray));
                buttonM3.setText("M3");
                writer.writeln("-20");
                hashMapMotors.put("M3", "-20");
                return true;
            }
        });
        buttonM4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                service = BluetoothService.getDefaultInstance();
                BluetoothWriter writer = new BluetoothWriter(service);

                buttonM4.setTextSize(24);
                buttonM4.setBackgroundColor(getResources().getColor(R.color.colorGray));
                buttonM4.setText("M4");
                writer.writeln("-40");
                hashMapMotors.put("M4", "-40");
                return true;
            }
        });

        buttonM1.setOnClickListener(this);
        buttonM2.setOnClickListener(this);
        buttonM3.setOnClickListener(this);
        buttonM4.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);

        imageViewCableway = findViewById(R.id.imageViewCableway);

        linearLayoutButtons = findViewById(R.id.linearLayoutButtons);

        progressBarSearch = findViewById(R.id.progressBarSearch);

        switchBluetooth = findViewById(R.id.switchTurnOnBluetooth);
        switchBluetooth.setOnCheckedChangeListener(this);

        BluetoothConfiguration config = new BluetoothConfiguration();
        config.context = getApplicationContext();
        config.bluetoothServiceClass = BluetoothClassicService.class;
        config.bufferSize = 1024;
        config.characterDelimiter = '\n';
        config.deviceName = "Cableway Control";
        config.callListenersInMainThread = true;

        config.uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // Required

        BluetoothService.init(config);
        service = BluetoothService.getDefaultInstance();

        service.setOnScanCallback(new BluetoothService.OnBluetoothScanCallback() {
            @Override
            public void onDeviceDiscovered(BluetoothDevice device, int rssi) {
                if (device.getName() != null && (device.getName().equals("HC-05") || device.getName().equals("HC-06") || device.getAddress().contains("FC:A8"))) {
                    service.connect(device);
                }
                //Snackbar.make(findViewById(android.R.id.content), "Нашел " + device.getName(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onStartScan() {
                progressBarSearch.setVisibility(View.VISIBLE);
                Snackbar.make(findViewById(android.R.id.content), "Поиск канатной дороги", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onStopScan() {
                progressBarSearch.setVisibility(View.GONE);
                //Snackbar.make(findViewById(android.R.id.content), "Поиск остановлен", Snackbar.LENGTH_SHORT).show();
            }
        });

        service.setOnEventCallback(new BluetoothService.OnBluetoothEventCallback() {
            @Override
            public void onDataRead(byte[] buffer, int length) {
            }

            @Override
            public void onStatusChange(BluetoothStatus status) {
                if (status == BluetoothStatus.CONNECTED) {
                    progressBarSearch.setVisibility(View.GONE);
                    buttonSearch.setVisibility(View.GONE);
                    imageViewCableway.setVisibility(View.VISIBLE);
                    linearLayoutButtons.setVisibility(View.VISIBLE);
                    seekBarSpeed.setVisibility(View.VISIBLE);
                    textViewSpeed.setVisibility(View.VISIBLE);

                    Snackbar.make(findViewById(android.R.id.content), "Подключено!", Snackbar.LENGTH_LONG).show();
                } else if (status == BluetoothStatus.CONNECTING) {
                    Snackbar.make(findViewById(android.R.id.content), "Подключение...", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDeviceName(String deviceName) {
                //Snackbar.make(findViewById(android.R.id.content), deviceName, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onToast(String message) {
            }

            @Override
            public void onDataWrite(byte[] buffer) {
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (isChecked) {
            int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                btAdapter.enable(); // Включаю Bluetooth
                buttonSearch.setVisibility(View.VISIBLE);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            off_motors();
            gone_gui();

            btAdapter.disable();
        }
        //Snackbar.make(findViewById(android.R.id.content), isChecked + "", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        off_motors();
        gone_gui();

        service.stopScan();
    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonM1.setTextSize(24);
        buttonM1.setBackgroundColor(getResources().getColor(R.color.colorGray));
        buttonM1.setText("M1");

        buttonM2.setTextSize(24);
        buttonM2.setBackgroundColor(getResources().getColor(R.color.colorGray));
        buttonM2.setText("M2");

        buttonM3.setTextSize(24);
        buttonM3.setBackgroundColor(getResources().getColor(R.color.colorGray));
        buttonM3.setText("M3");

        buttonM4.setTextSize(24);
        buttonM4.setBackgroundColor(getResources().getColor(R.color.colorGray));
        buttonM4.setText("M4");
    }

    @Override
    public void onClick(View v) {
        service = BluetoothService.getDefaultInstance();
        BluetoothWriter writer = new BluetoothWriter(service);
        String command = "";

        switch (v.getId()) {
            case R.id.buttonMotor1: {
                buttonM1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonM1.setTextSize(48);
                if (hashMapMotors.containsKey("M1")) {
                    char dir = hashMapMotors.get("M1").toCharArray()[0];
                    if (dir == '+') {
                        buttonM1.setText("↓");
                        command = "-1" + speed;
                    } else {
                        buttonM1.setText("↑");
                        command = "+1" + speed;
                    }
                } else {
                    buttonM1.setText("↑");
                    command = "+1" + speed;
                }
                writer.writeln(command);
                hashMapMotors.put("M1", command);
                break;
            }
            case R.id.buttonMotor2: {
                buttonM2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonM2.setTextSize(48);

                if (hashMapMotors.containsKey("M2")) {
                    char dir = hashMapMotors.get("M2").toCharArray()[0];
                    if (dir == '+') {
                        buttonM2.setText("↓");
                        command = "-3" + speed;
                    } else {
                        command = "+3" + speed;
                        buttonM2.setText("↑");
                    }
                } else {
                    buttonM2.setText("↑");
                    command = "+3" + speed;
                }
                writer.writeln(command);
                hashMapMotors.put("M2", command);
                break;
            }
            case R.id.buttonMotor3: {
                buttonM3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonM3.setTextSize(48);
                if (hashMapMotors.containsKey("M3")) {
                    char dir = hashMapMotors.get("M3").toCharArray()[0];
                    if (dir == '+') {
                        buttonM3.setText("↓");
                        command = "-2" + speed;
                    } else {
                        buttonM3.setText("↑");
                        command = "+2" + speed;
                    }
                } else {
                    buttonM3.setText("↑");
                    command = "+2" + speed;
                }
                writer.writeln(command);
                hashMapMotors.put("M3", command);
                break;
            }
            case R.id.buttonMotor4: {
                buttonM4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                buttonM4.setTextSize(48);
                if (hashMapMotors.containsKey("M4")) {
                    char dir = hashMapMotors.get("M4").toCharArray()[0];
                    if (dir == '+') {
                        buttonM4.setText("←");
                        command = "-4" + speed;
                    } else {
                        buttonM4.setText("→");
                        command = "+4" + speed;
                    }
                } else {
                    buttonM4.setText("→");
                    command = "+4" + speed;
                }
                writer.writeln(command);

                hashMapMotors.put("M4", command);
                break;
            }
            case R.id.buttonStartSearch: {
                service.startScan();
                break;
            }
        }
        Toast.makeText(this, command + " " + hashMapMotors.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        speed = String.valueOf((progress + 1)*100);
        textViewSpeed.setText("Скорость: " + speed);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void off_motors() {
        service = BluetoothService.getDefaultInstance();
        BluetoothWriter writer = new BluetoothWriter(service);

        writer.writeln("-10");
        writer.writeln("-20");
        writer.writeln("-30");
        writer.writeln("-40");

        hashMapMotors.put("M1", "-10");
        hashMapMotors.put("M2", "-30");
        hashMapMotors.put("M3", "-20");
        hashMapMotors.put("M4", "-40");
    }

    private void gone_gui() {
        buttonSearch.setVisibility(View.GONE);
        progressBarSearch.setVisibility(View.GONE);
        imageViewCableway.setVisibility(View.GONE);
        linearLayoutButtons.setVisibility(View.GONE);
        seekBarSpeed.setVisibility(View.GONE);
        textViewSpeed.setVisibility(View.GONE);
    }
}
