package com.example.assignment3_pizza;

import static java.lang.Integer.parseInt;
import android.content.Context;
import android.util.Patterns;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.util.List;

public class CardOrdersAdapter extends RecyclerView.Adapter<CardOrdersAdapter.ViewHolder> {

    public List<CardText> cards;
    public Context context;
    ImageButton btnEditOrders, btnDeleteOrders;
    TextView txtOrderNumber;

    public CardOrdersAdapter(List<CardText> list, Context con) {
        cards = list;
        context = con;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderNumber, txtDateTime, txtPrice;
        View viewCard;

        public ViewHolder (View itemView) {
            super(itemView);
            txtOrderNumber = itemView.findViewById(R.id.txtOrderNumber);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnEditOrders = itemView.findViewById(R.id.btnEditOrders);
            btnDeleteOrders = itemView.findViewById(R.id.btnDeleteOrders);
            viewCard = itemView.findViewById(R.id.viewCardOrder);

            // Listeners
            viewCard.setOnClickListener(view -> showOrderDetails(txtOrderNumber.getText().toString().replace("#", "")));
        }
    }

    public void showOrderDetails(String orderNumberStr) {
        AllOrdersActivity.popUpOrderDetails.setVisibility(View.VISIBLE);

        int orderNumber = parseInt(orderNumberStr);
        List<Order> orders =  MainActivity.allOrders.getAllOrders();
        for (Order order : orders) {
            if(order.getOrderNumber() == orderNumber) {
                String details = order.displayOrder(context);
                AllOrdersActivity.txtDisplayDetails.setText(details);
            }
        }
    }

    public CardOrdersAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_orders,parent, false);
        return new CardOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardOrdersAdapter.ViewHolder holder, int position) {
        List<Order> allOrders = MainActivity.allOrders.getAllOrders();

        String orderNumber = String.format("#%04d", allOrders.get(position).getOrderNumber());
        String date = allOrders.get(position).getOrderTime();
        String price = String.format("%s:\n$%.2f", context.getString(R.string.total), allOrders.get(position).getOrderPrice());

        holder.txtOrderNumber.setText(orderNumber);
        holder.txtDateTime.setText(date);
        holder.txtPrice.setText(price);

        btnEditOrders.setOnClickListener(view -> {
            AllOrdersActivity.popUpAOEdit.setVisibility(View.VISIBLE);

            int orderNum = parseInt(orderNumber.replace("#", ""));
            List<Order> orders =  MainActivity.allOrders.getAllOrders();
            for (Order order : orders) {
                if(order.getOrderNumber() == orderNum) {
                    AllOrdersActivity.etxtName.setText(order.getCustomerName());
                    AllOrdersActivity.etxtPhone.setText(order.getCustomerPhone());
                    AllOrdersActivity.etxtEmail.setText(order.getCustomerEmail());
                    AllOrdersActivity.etxtAddress.setText(order.getCustomerAddress());
                    AllOrdersActivity.etxtNotes.setText(order.getOrderNotes());
                }
            }

                AllOrdersActivity.btnConfirmEdit.setOnClickListener(v-> {
                    String error = "";
                    error += checkInput("name", AllOrdersActivity.etxtName, context);
                    error += checkInput("phone", AllOrdersActivity.etxtPhone, context);
                    error += checkInput("email", AllOrdersActivity.etxtEmail, context);
                    error += checkInput("address", AllOrdersActivity.etxtAddress, context);

                    if (error.isBlank()) {
                        MainActivity.allOrders.getAllOrders().get(allOrders.get(position).getOrderNumber()-1).setCustomerName(AllOrdersActivity.etxtName.getText().toString());
                        MainActivity.allOrders.getAllOrders().get(allOrders.get(position).getOrderNumber()-1).setCustomerPhone(AllOrdersActivity.etxtPhone.getText().toString());
                        MainActivity.allOrders.getAllOrders().get(allOrders.get(position).getOrderNumber()-1).setCustomerEmail(AllOrdersActivity.etxtEmail.getText().toString());
                        MainActivity.allOrders.getAllOrders().get(allOrders.get(position).getOrderNumber()-1).setCustomerAddress(AllOrdersActivity.etxtAddress.getText().toString());
                        MainActivity.allOrders.getAllOrders().get(allOrders.get(position).getOrderNumber()-1).setOrderNotes(AllOrdersActivity.etxtNotes.getText().toString());

                        // UPDATE THE EDITED ORDER
                        Database database = new Database(context);
                        try {
                            database.updateOrder(Integer.toString(allOrders.get(position).getOrderNumber()), MainActivity.allOrders.getAllOrders().get(allOrders.get(position).getOrderNumber()-1).customerInformation(context));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            MainActivity.allOrders = new AllOrders(context);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        AllOrdersActivity.popUpAOEdit.setVisibility(View.GONE);
                        AllOrdersActivity.setOrders("", context.getString(R.string.all_orders));
                    }
                });

                AllOrdersActivity.btnEditBack.setOnClickListener(v-> AllOrdersActivity.popUpAOEdit.setVisibility(View.GONE));
        });

        btnDeleteOrders.setOnClickListener(view -> {
            AllOrdersActivity.popUpAODelete.setVisibility(View.VISIBLE);

            String confirmation = context.getString(R.string.confirm_delete) + String.format("#%04d", allOrders.get(position).getOrderNumber());
            AllOrdersActivity.txtConfirmation.setText(confirmation);

            AllOrdersActivity.btnDeleteOK.setOnClickListener(v -> {

                // UPDATE THE EDITED ORDER
                Database database = new Database(context);
                try {
                    database.deleteOrder(Integer.toString(allOrders.get(position).getOrderNumber()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                try {
                    MainActivity.allOrders = new AllOrders(context);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                AllOrdersActivity.setOrders("", context.getString(R.string.all_orders));
                AllOrdersActivity.popUpAODelete.setVisibility(View.GONE);
            });

            AllOrdersActivity.btnDeleteBack.setOnClickListener(v -> AllOrdersActivity.popUpAODelete.setVisibility(View.GONE));
        });
    }

    @Override
    public int getItemCount() { return cards.size(); }

    private String checkInput(String type, EditText inputField, Context context) {
        String retString = "";
        String input = inputField.getText().toString();

        switch (type) {
            case "name":
                if (input.isBlank()) { retString = context.getString(R.string.error_name); }
                break ;

            case "phone":
                if (!input.matches("\\d{9,10}")) { retString = context.getString(R.string.error_phone); }
                break;

            case "email":
                if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) { retString = context.getString(R.string.error_email); }
                break;

            case "address":
                if (input.isBlank()) { retString = context.getString(R.string.error_address); }
                break ;

            default:
                // names, addresses, and notes can accept any type of character
                retString = "";
        }

        return retString + "\n";
    }
}
