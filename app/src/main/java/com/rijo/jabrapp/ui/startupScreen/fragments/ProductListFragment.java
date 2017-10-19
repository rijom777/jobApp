package com.rijo.jabrapp.ui.startupScreen.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rijo.jabrapp.R;
import com.rijo.jabrapp.domains.product.Products;
import com.rijo.jabrapp.ui.startupScreen.StartupActivity;
import com.rijo.jabrapp.ui.startupScreen.adapters.ProductsRecyclerAdapter;

import java.util.Collections;


public class ProductListFragment extends Fragment {

    private Products products;
    private boolean isLoading=false;
    ProgressBar progressBar;

    private OnProductSelectedListener activityCallback;
    private OnScrolledToEndListener loadMoreCallback;
    ProductsRecyclerAdapter productsRecyclerAdapter;

    private static final int loadMoreThreshold=15;

    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance(Products products) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putParcelable(StartupActivity.PRODUCTS_KEY, products);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            products = getArguments().getParcelable(StartupActivity.PRODUCTS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_product_list, container, false);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
        RecyclerView productsRecyclerView=view.findViewById(R.id.productList_recycler_view);
        productsRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        productsRecyclerView.setLayoutManager(mLayoutManager);
        productsRecyclerAdapter=new ProductsRecyclerAdapter(products,getActivity().getApplicationContext(),activityCallback);
        productsRecyclerView.setAdapter(productsRecyclerAdapter);
        productsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = mLayoutManager.getItemCount();
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + loadMoreThreshold)) {
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    loadMoreCallback.LoadMore();
            }
        };
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int i=0;
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position=viewHolder.getAdapterPosition();
                products.getItems().remove(position);
                productsRecyclerAdapter.notifyDataSetChanged();
            }
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                //Collections.swap(/*RecyclerView.Adapter's data collection*/, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                //_adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                Collections.swap(products.getItems(),viewHolder.getAdapterPosition(),target.getAdapterPosition());
                productsRecyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
                int position=viewHolder.getAdapterPosition();
                products.getItems().remove(position);
                productsRecyclerAdapter.notifyDataSetChanged();
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
//                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
//                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

        };

        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        //itemTouchHelper.attachToRecyclerView(productsRecyclerView);

        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(productsRecyclerView);

        return view;
    }

    public void addMoreProducts(Products products){
        productsRecyclerAdapter.setMoreData(products);
        progressBar.setVisibility(View.GONE);
        isLoading=false;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            activityCallback = (OnProductSelectedListener) context;
            loadMoreCallback = (OnScrolledToEndListener) context;
        } catch(ClassCastException e) {
            throw new RuntimeException(context.toString()
                    + " must implement OnProductSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityCallback = null;
        loadMoreCallback = null;
    }


    public interface OnProductSelectedListener {
        void onProductSelected(int position);
    }
    public interface OnScrolledToEndListener {
        void LoadMore();
    }
}
