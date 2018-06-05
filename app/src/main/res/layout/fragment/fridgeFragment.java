package sjtu_hci.io.hci_irefrigerator.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sjtu_hci.io.hci_irefrigerator.R;
import sjtu_hci.io.hci_irefrigerator.adapter.FoodAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class fridgeFragment extends Fragment {

    private List<sjtu_hci.io.hci_irefrigerator.Model.Foodinfo> foodList = new ArrayList<sjtu_hci.io.hci_irefrigerator.Model.Foodinfo>();
    private View view;
    private ListView flist;

    public fridgeFragment() {
        // Required empty public constructor
    }


    private void initFood(){
        sjtu_hci.io.hci_irefrigerator.Model.Foodinfo apple = new sjtu_hci.io.hci_irefrigerator.Model.Foodinfo("apple",2.0,R.drawable.ic_tab1_s);
        sjtu_hci.io.hci_irefrigerator.Model.Foodinfo banana = new sjtu_hci.io.hci_irefrigerator.Model.Foodinfo("banana",2.0,R.drawable.ic_tab2_s);
        sjtu_hci.io.hci_irefrigerator.Model.Foodinfo orange = new sjtu_hci.io.hci_irefrigerator.Model.Foodinfo("orange",2.0,R.drawable.ic_tab3_s);
        sjtu_hci.io.hci_irefrigerator.Model.Foodinfo pear = new sjtu_hci.io.hci_irefrigerator.Model.Foodinfo("pear",2.0,R.drawable.ic_tab4_s);
        sjtu_hci.io.hci_irefrigerator.Model.Foodinfo peach = new sjtu_hci.io.hci_irefrigerator.Model.Foodinfo("peach",2.0,R.drawable.ic_tab_menu_selected);
        foodList.add(apple);
        foodList.add(banana);
        foodList.add(orange);
        foodList.add(peach);
        foodList.add(pear);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.frag_fridge, container, false);
        initFood();
        FoodAdapter adapter = new FoodAdapter(sjtu_hci.io.hci_irefrigerator.fragment.fridgeFragment.this.getActivity(),R.layout.fridge_item,foodList);
        flist = (ListView) view.findViewById(R.id.foodlist);
        flist.setAdapter(adapter);
        return view;
    }


}
