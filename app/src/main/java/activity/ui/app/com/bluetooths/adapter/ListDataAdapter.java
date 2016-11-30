package activity.ui.app.com.bluetooths.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import activity.ui.app.com.bluetooths.BluetoothClientActivity;
import activity.ui.app.com.bluetooths.R;


public class ListDataAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> phones = new ArrayList<BluetoothDevice>();
    private Context context;
    private BluetoothClientActivity activity;

    public ListDataAdapter(Context context) {
        this.context = context;
    }

    public ListDataAdapter(BluetoothClientActivity activity) {
        this.activity = activity;
    }

    public void setData(ArrayList<BluetoothDevice> phones) {
        this.phones = phones;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return phones.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewgroup) {
        Hodler hodler;
        if (view == null) {
            hodler = new Hodler();
            view = LayoutInflater.from(activity).inflate(R.layout.item, null);
            hodler.name = (TextView) view.findViewById(R.id.name_tv);
            hodler.adderss = (TextView) view.findViewById(R.id.address_tv);
            hodler.write = (TextView) view.findViewById(R.id.write_tv);
            hodler.dle = (TextView) view.findViewById(R.id.dle_tv);
            hodler.connect=(TextView) view.findViewById(R.id.connect_tv);
            view.setTag(hodler);
        } else {
            hodler = (Hodler) view.getTag();
        }
        BluetoothDevice phone = phones.get(i);
        hodler.name.setText(phone.getName());
        hodler.adderss.setText(phone.getAddress());
        hodler.write.setOnClickListener(new OnClick(i));
        hodler.dle.setOnClickListener(new OnClick(i));
        hodler.connect.setOnClickListener(new OnClick(i));
        return view;
    }

    class Hodler {
        public TextView name;
        public TextView adderss;
        public TextView write, dle,connect;

    }

    class OnClick implements View.OnClickListener {
        private int index;

        public OnClick(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {

            BluetoothDevice phone = phones.get(index);
            switch (v.getId()){
                case R.id.write_tv:
                    activity.write(phone);
                    break;
                case R.id.dle_tv:
                    activity.unpair(phone);
                    break;
                case R.id.connect_tv:
                    activity.connect(phone);
                    break;
            }

        }
    }
}
