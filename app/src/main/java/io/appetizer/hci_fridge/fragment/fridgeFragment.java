package io.appetizer.hci_fridge.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;
import io.appetizer.hci_fridge.adapter.FoodAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class fridgeFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_fridge, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new FoodAdapter(this.getContext(), createData());

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        recyclerView.setAdapter(adapter);
        return view;
    }
    private List<Integer> createData() {
        List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                data.add(R.mipmap.chat_normal);
            } else {
                data.add(R.mipmap.chat_press);
            }
        }
        return data;
    }

}
