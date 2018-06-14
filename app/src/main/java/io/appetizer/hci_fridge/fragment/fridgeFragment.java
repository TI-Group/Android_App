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
    private RecyclerView fruit,vegetable;
    private FoodAdapter adapter;
    private View view;
    private Context context;
    private List<Foodinfo> foodList = new ArrayList<Foodinfo>();

    private void initFood(){
        Foodinfo apple = new Foodinfo("apple",2.0, R.drawable.ic_tab1_s,"01");
        Foodinfo banana = new Foodinfo("banana",2.0,R.drawable.ic_tab2_s,"01");
        Foodinfo orange = new Foodinfo("orange",2.0,R.drawable.ic_tab3_s,"01");
        Foodinfo pear = new Foodinfo("pear",2.0,R.drawable.ic_tab4_s,"01");
        Foodinfo peach = new Foodinfo("peach",2.0,R.drawable.ic_tab_menu_selected,"01");
        foodList.add(apple);
        foodList.add(banana);
        foodList.add(orange);
        foodList.add(peach);
        foodList.add(pear);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_fridge, container, false);
        context = this.getContext();
        initFood();


        fruit = (RecyclerView) view.findViewById(R.id.fruit);
        vegetable = (RecyclerView) view.findViewById(R.id.vegetable);


        adapter = new FoodAdapter(this.getContext(), foodList);

        fruit.setLayoutManager(new GridLayoutManager(getContext(), 4));
        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                View promptsView = inflater.inflate(R.layout.dialog,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextResult);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                adapter.remove(position); //remove the item

            }
        });
        fruit.setAdapter(adapter);
        vegetable.setLayoutManager(new GridLayoutManager(getContext(), 4));
        vegetable.setAdapter(adapter);
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.support.v7.widget.Toolbar toolbar = getActivity().findViewById(R.id.fridgeToolBar);
        toolbar.setTitleTextColor(getActivity().getResources().getColor(R.color.white));
        toolbar.setTitle("食物");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
}
