package com.example.assignment3_pizza;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    public List<CardText> cards;
    public Context context;
    private View lastSelected = null;

    public CardAdapter(List<CardText> list, Context context) {
        this.cards = list;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtText, txtQuantity;
        ImageButton btnAdd, btnSub;
        ImageView imgImage;
        View viewAddSub, viewCard;

        public ViewHolder (View itemView) {
            super(itemView);
            txtText = itemView.findViewById(R.id.txtName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            imgImage = itemView.findViewById(R.id.imgItem);
            viewAddSub = itemView.findViewById(R.id.lineAddSub);
            viewCard = itemView.findViewById(R.id.viewCardCart);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnSub = itemView.findViewById(R.id.btnSub);

            FragOrder.btnAddToCart.setClickable(false);

            // if the card is for a pizza size, remove the add/sub buttons
            if (cards.get(0).getName().contains("$")) {
                viewAddSub.setVisibility(View.GONE);

                // ensure only one size can be selected at a time
                viewCard.setOnClickListener(view -> {
                    View checked = view.findViewById(R.id.viewCardCart);
                    checked.setSelected(true);
                    NewOrderActivity.pizza.sizeSelect = true;

                    if (lastSelected != null) {
                        lastSelected.setSelected(false);
                        changeCardColor(lastSelected);
                    }

                    if (lastSelected == checked) {
                        lastSelected = null;
                        NewOrderActivity.pizza.sizeSelect = false;

                    } else {
                        lastSelected = checked;
                    }

                    // update the selected / unselected colour
                    changeCardColor(checked);

                    // update the pizza's size
                    TextView checkedText = view.findViewById(R.id.txtName);
                    String size = checkedText.getText().toString().replaceAll("\\s.*", "");
                    NewOrderActivity.pizza.setSize(size);
                });

            } else {
                viewAddSub.setVisibility(View.VISIBLE);
            }

            // Listeners
            btnAdd.setOnClickListener(view -> add(txtQuantity, viewCard));
            btnSub.setOnClickListener(view -> subtract(txtQuantity, viewCard));
        }
    }

    private void add(TextView txtQuantity, View viewCard) {
        // add topping as long as there is less than three toppings selected
        if (NewOrderActivity.pizza.totalQuantity >= 0 && NewOrderActivity.pizza.totalQuantity < NewOrderActivity.pizza.max) {
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            quantity++;
            txtQuantity.setText(String.valueOf(quantity));
            NewOrderActivity.pizza.totalQuantity++;

            if (quantity == 0) {
                viewCard.setSelected(false);

            // add toppings to pizza order
            } else if (quantity > 0) {
                viewCard.setSelected(true);
                TextView checkedText = viewCard.findViewById(R.id.txtName);
                String topping = checkedText.getText().toString();
                NewOrderActivity.pizza.addToppings(topping);
            }

        } else if (NewOrderActivity.pizza.totalQuantity >= NewOrderActivity.pizza.max) {
            // toast: max toppings reached.
        }

        changeCardColor(viewCard);
    }

    private void subtract(TextView txtQuantity, View viewCard) {
        int quantity = Integer.parseInt(txtQuantity.getText().toString());

        // subtract topping only if there is a topping to subtract & remove from pizza order
        if (NewOrderActivity.pizza.totalQuantity > 0 && quantity > 0) {
            quantity--;
            txtQuantity.setText(String.valueOf(quantity));
            NewOrderActivity.pizza.totalQuantity--;
            TextView checkedText = viewCard.findViewById(R.id.txtName);
            String topping = checkedText.getText().toString();
            NewOrderActivity.pizza.removeToppings(topping);

            if (quantity == 0) {
                viewCard.setSelected(false);

            } if (quantity > 0) {
                viewCard.setSelected(true);
            }
        }

        // update the selected / unselected colour
        changeCardColor(viewCard);
    }

    private void changeCardColor(View viewCard) {
        // if the isSelected() bool returns true, change the card colour to green
        // otherwise change it back to grey
        if (viewCard.isSelected()) {
            viewCard.setBackgroundResource(R.drawable.card_green);

        } else {
            viewCard.setBackgroundResource(R.drawable.card);
        }

        // if one size and at least one topping are selected, make "Add to Cart" clickable
        if (NewOrderActivity.pizza.sizeSelect && NewOrderActivity.pizza.totalQuantity > 0) {
            FragOrder.btnAddToCart.setBackgroundColor(context.getResources().getColor(R.color.green));
            FragOrder.btnAddToCart.setClickable(true);

        } else {
            FragOrder.btnAddToCart.setBackgroundColor(context.getResources().getColor(R.color.red));
            FragOrder.btnAddToCart.setClickable(false);
        }
    }

    public CardAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        holder.txtText.setText(cards.get(position).getName());
        holder.imgImage.setImageDrawable(cards.get(position).getImage());
    }

    @Override
    public int getItemCount() { return cards.size(); }
}