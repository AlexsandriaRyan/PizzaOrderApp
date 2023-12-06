package com.example.assignment3_pizza;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CardCartAdapter extends RecyclerView.Adapter<CardCartAdapter.ViewHolder> {

    public List<CardText> cards;
    public Context context;
    ImageButton btnEditCart, btnDeleteCart;

    public CardCartAdapter(List<CardText> list, Context con) {
        cards = list;
        context = con;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCartDescription;
        ImageView imgImage;
        View viewCard;

        public ViewHolder (View itemView) {
            super(itemView);
            imgImage = itemView.findViewById(R.id.imgCart);
            viewCard = itemView.findViewById(R.id.viewCardCart);
            btnDeleteCart = itemView.findViewById(R.id.btnDeleteCart);
            txtCartDescription = itemView.findViewById(R.id.txtCartDescription);
        }
    }

    public CardCartAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart,parent, false);
        return new CardCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardCartAdapter.ViewHolder holder, int position) {
        holder.txtCartDescription.setText(cards.get(position).getName());
        holder.imgImage.setImageDrawable(cards.get(position).getImage());

        // FIND A WAY TO EDIT / DELETE THE ASSOCIATED PIZZA FROM EACH CARD'S BUTTONS
        // btnEditCart.setOnClickListener(view -> FragCart.editPizza());
        // btnDeleteCart.setOnClickListener(view -> FragCart.deletePizza());
    }

    @Override
    public int getItemCount() { return cards.size(); }

}