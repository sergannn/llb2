package droidmentor.tabwithviewpager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by home on 27.02.2018.
 */

public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    FragmentManager fragmentManager;
    private Context context;
    private LayoutInflater inflater;
    List<DataFish> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;



    // create constructor to innitilize context and data sent from MainActivity
    public AdapterFish(Context context, List<DataFish> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_fish, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        myHolder.itemView.setTag(position);
        DataFish current=data.get(position);
        myHolder.tName.setText(current.name);
        myHolder.tDate.setText(current.date.concat("\n "+current.club));
        myHolder.tReg.setText(current.reg);
        final String thref=current.href;
        Log.d("FUCK",current.registered);
if(current.registered.equals("1"))
{
myHolder.tName.append(" ЗАРЕГИСТРИРОВАН");
myHolder.tName.setTextColor(Color.parseColor("#a00000"));
  //  editor.putString("registered",current.href);
  //  editor.apply();
}
        final String tname=current.name;

//        myHolder.tClub.setText(current.club);
      //  myHolder.textSize.setText("Size: " + current.sizeName);
      //  myHolder.textType.setText("Category: " + current.catName);
      //  myHolder.textPrice.setText("Rs. " + current.price + "\\Kg");
      //  myHolder.textPrice.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        // load image into imageview using glide
       // Glide.with(context).load("http://192.168.1.7/test/images/" + current.fishImage)
        //        .placeholder(R.drawable.ic_img_error)
          //      .error(R.drawable.ic_img_error)
            //    .into(myHolder.ivFish);
        //myHolder.cview.setOnLongClickListener();
      //  myHolder.cview.setTag(0,67);
        myHolder.cview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setTag(thref);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("tmenuhref",thref);
                editor.apply();
             //   Toast.makeText(context,tname,Toast.LENGTH_LONG).show();

                return false;
            }

        });
        myHolder.cview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"a",Toast.LENGTH_LONG).show();

                openDetailActivity(thref,2,tname);
/*

                Fragment newFragment = new ChatFragment();
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction();
                ft.replace(R.id.activity_main, newFragment);
                ft.addToBackStack(null);
                ft.commit();*/
            }
        });
    }

    // return total item from List

    @Override
    public int getItemCount() {
        return data.size();
    }



    private void openDetailActivity(String thref,int image,String tname)
    {
        Intent i=new Intent(context, TourneyDetailActivity.class);

        //PACK DATA TO SEND
        i.putExtra("thref",thref);
        i.putExtra("IMAGE_KEY",image);
        i.putExtra("tname",tname);
       // context.startActivityf
        //open activity
        context.startActivity(i);

    }
    class MyHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {


        TextView tName;
        String href;
        ImageView ivFish;
        TextView tDate;
        TextView tReg;
        TextView tClub;
        public CardView cview;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            tName= (TextView) itemView.findViewById(R.id.tName);
            tDate= (TextView) itemView.findViewById(R.id.tDate);
            tReg= (TextView) itemView.findViewById(R.id.tReg);
            cview=(CardView) itemView.findViewById(R.id.card_view);
            String href="";
            itemView.setOnCreateContextMenuListener(this);


            }

        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {


        }

        }



}