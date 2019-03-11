package com.example.arshit.ecommerceapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahamed.multiviewadapter.BaseViewHolder;
import com.ahamed.multiviewadapter.ItemBinder;
import com.example.arshit.ecommerceapp.Model.Category;
import com.example.arshit.ecommerceapp.R;

public class CategoryBinder extends ItemBinder<Category, CategoryBinder.CategoryViewHolder> {

    @Override
    public CategoryViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new CategoryViewHolder(inflater.inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void bind(CategoryViewHolder holder, Category item) {

        holder.category_name.setText(item.getCategoryName());
        holder.category_image.setImageResource(item.getCategoryImage());
    }

    @Override public int getSpanSize(int maxSpanCount) {
        return 1;
    }

        @Override
    public boolean canBindData(Object item) {
        return item instanceof Category;
    }

    static class CategoryViewHolder extends BaseViewHolder<Category> {

        private ImageView category_image;
        private TextView category_name;
        public CategoryViewHolder(View itemView) {
            super(itemView);

            category_image = itemView.findViewById(R.id.category_image);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
        }



    }
}
