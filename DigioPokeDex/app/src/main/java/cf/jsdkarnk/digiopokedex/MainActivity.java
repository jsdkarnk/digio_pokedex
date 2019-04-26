package cf.jsdkarnk.digiopokedex;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestQueue mQueue;
    private ArrayList<String> nameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.listview);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        jsonParse();
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_box,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.pokemonclassic);
            holder.poke_name.setTypeface(typeface);
            holder.poke_name.setText(nameList.get(position));
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return nameList.size();
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView poke_name;

        ViewHolder(View itemView) {
            super(itemView);
            poke_name = (TextView) itemView.findViewById(R.id.poke_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int) v.getTag();
                    Log.d("Click1234","ID: " + index);
                    Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                    intent.putExtra("poke_index",index);
                    startActivity(intent);
                }
            });
        }
    }

    private void jsonParse() {
        String url = "http://172.16.1.182:8001/pokemon/15";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Click12345","Success");
                        nameList.clear();
                        try {
                            JSONArray result_Array = response.getJSONArray("results");
                            Log.d("Click12345","ID: " + result_Array);
                            for(int i = 0;i < result_Array.length();i++){
                                JSONObject poke_obj = result_Array.getJSONObject(i);
                                String p_name = poke_obj.getString("name");
                                Log.d("coffee111","" + p_name);
                                nameList.add(p_name);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView.setAdapter(new RecyclerViewAdapter());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("Click1234",String.valueOf(error) );
            }
        });

        mQueue.add(request);
    }

}
