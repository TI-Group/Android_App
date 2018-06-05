package io.appetizer.hci_fridge.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
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

    private List<Foodinfo> foodList = new ArrayList<Foodinfo>();
    public Context context;
    public fridgeFragment() {
        // Required empty public constructor
    }


    private void initFood(){
        Foodinfo apple = new Foodinfo("apple",2.0, R.drawable.ic_tab1_s);
        Foodinfo banana = new Foodinfo("banana",2.0,R.drawable.ic_tab2_s);
        Foodinfo orange = new Foodinfo("orange",2.0,R.drawable.ic_tab3_s);
        Foodinfo pear = new Foodinfo("pear",2.0,R.drawable.ic_tab4_s);
        Foodinfo peach = new Foodinfo("peach",2.0,R.drawable.ic_tab_menu_selected);
        foodList.add(apple);
        foodList.add(banana);
        foodList.add(orange);
        foodList.add(peach);
        foodList.add(pear);
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_fridge, container, false);
        initFood();
        FoodAdapter adapter = new FoodAdapter(fridgeFragment.this.getActivity(),R.layout.fridge_item,foodList);
        flist = (ListView) view.findViewById(R.id.foodlist);
        flist.setAdapter(adapter);
        return view;
    }*/
    private SwipeRefreshLayout mRefreshSrl;
    private RecyclerView mContentRv;
    private FoodAdapter adapter;
    private View view;
    private android.support.v7.widget.Toolbar toolbar;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_fridge, container, false);
        initFood();
        context = this.getContext();
        //toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.tb_toolbar);
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mContentRv = (RecyclerView) view.findViewById(R.id.rv_content);
        mContentRv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mContentRv.setHasFixedSize(true);
        adapter = new FoodAdapter(this.getContext(),foodList);
        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + foodList.get(position).getName());
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
                                        // get user input and set it to result
                                        // edit text
                                        foodList.get(position).setNum(Double.parseDouble(userInput.getText().toString()));
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
        mContentRv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.frag_fridge, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_action:
                System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }






}
