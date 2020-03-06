package droidmentor.tabwithviewpager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by home on 27.02.2018.
 */

public class AdapterPlayers extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    FragmentManager fragmentManager;
    private Context context;
    private LayoutInflater inflater;
    List<DataPlayer> data= Collections.emptyList();
    DataPlayer current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterPlayers(Context context, List<DataPlayer> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_players, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataPlayer current=data.get(position);
        myHolder.pName.setText(current.name);
        myHolder.pTel.setText(current.sex);
       // myHolder.tDate.setText(current.bd);
if(current.elo.length()==1  || current.elo.equals("null")) {
    myHolder.pElo.setVisibility(View.INVISIBLE);
}

      //  if(current.elo.length()>3) {

        //    myHolder.tReg.setLayoutParams(new LinearLayout.LayoutParams  (150, 150));
       // }
        myHolder.pElo.setText(current.elo);

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
        myHolder.cview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"a",Toast.LENGTH_LONG).show();

               // openDetailActivity("a",2);

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

    private void openDetailActivity(String name,int image)
    {
        Intent i=new Intent(context, TourneyDetailActivity.class);

        //PACK DATA TO SEND
        i.putExtra("NAME_KEY",name);
        i.putExtra("IMAGE_KEY",image);

        //open activity
        context.startActivity(i);

    }
    class MyHolder extends RecyclerView.ViewHolder{

        TextView pName;
    //    DownloadImageTask ivFish;
        TextView pTel;
        TextView pElo;
        TextView tClub;
        public CardView cview;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            pName= (TextView) itemView.findViewById(R.id.pName);
            pTel= (TextView) itemView.findViewById(R.id.pTel);
            pElo= (TextView) itemView.findViewById(R.id.pElo);
            cview=(CardView) itemView.findViewById(R.id.card_view);
//ivFish=(ImageView) itemView.findViewById(R.id.img);
     //   ivFish=new DownloadImageTask((ImageView) itemView.findViewById(R.id.img));
     //   ivFish.execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");

           // textSize = (TextView) itemView.findViewById(R.id.textSize);
           // textType = (TextView) itemView.findViewById(R.id.textType);
           // textPrice = (TextView) itemView.findViewById(R.id.textPrice);
        }

    }
   }
