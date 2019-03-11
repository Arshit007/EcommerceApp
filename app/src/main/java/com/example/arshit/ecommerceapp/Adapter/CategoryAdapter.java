package com.example.arshit.ecommerceapp.Adapter;

import com.ahamed.multiviewadapter.DataListManager;
import com.ahamed.multiviewadapter.RecyclerAdapter;
import com.example.arshit.ecommerceapp.Model.Category;

import java.util.List;

public class CategoryAdapter  extends RecyclerAdapter{

        private DataListManager<Category> dataManager;

        public CategoryAdapter() {
            this.dataManager = new DataListManager<>(this);
            addDataManager(dataManager);

            registerBinder(new CategoryBinder());
        }

        public void addData(List<Category> dataList) {
            dataManager.addAll(dataList);
        }



    }