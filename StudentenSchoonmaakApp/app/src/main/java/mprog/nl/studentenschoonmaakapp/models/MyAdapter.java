/**
 * Created by Jim Boelrijk
 * Student of UvA
 * Student number: 1045216
 */
package mprog.nl.studentenschoonmaakapp.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;

/**
 * Adapter for MakeGroupActivity. Receives an ArrayList containing User objects and sets
 * email and name of User object in ListView.
 */

public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> members;

    public MyAdapter(Context context, ArrayList<User> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TwoLineListItem twoLineListItem;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();

        text1.setText(String.format("Naam: %s", members.get(position).getName()));
        text2.setText(String.format("E-mailadres: %s", members.get(position).getEmail()));

        return twoLineListItem;
    }
}
