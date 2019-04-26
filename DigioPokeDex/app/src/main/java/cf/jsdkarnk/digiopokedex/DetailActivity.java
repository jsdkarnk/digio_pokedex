package cf.jsdkarnk.digiopokedex;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    private TextView tv_type, tv_atk,tv_def,tv_speed, tv_sp_atk,tv_sp_def,tv_name;
    private ImageView iv_top_l,iv_top_r,iv_bot_l,iv_bot_r;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tv_name = (TextView) findViewById(R.id.detail_poke_nam);
        tv_type = (TextView) findViewById(R.id.detail_type);
        tv_atk = (TextView) findViewById(R.id.detail_atk);
        tv_def = (TextView) findViewById(R.id.detail_def);
        tv_speed = (TextView) findViewById(R.id.detail_speed);
        tv_sp_atk = (TextView) findViewById(R.id.detail_sp_atk);
        tv_sp_def = (TextView) findViewById(R.id.detail_sp_def);
        iv_top_l = (ImageView) findViewById(R.id.img_front_shi);
        iv_top_r = (ImageView) findViewById(R.id.img_back_shi);
        iv_bot_l = (ImageView) findViewById(R.id.img_front_def);
        iv_bot_r = (ImageView) findViewById(R.id.img_back_def);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        int index = bundle.getInt("poke_index");
        int poke_id = index+1;
        jsonParse(poke_id);
    }

    private void jsonParse(int id) {
        String url = "https://pokeapi.co/api/v2/pokemon/"+id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String poke_name = response.getString("name");
                            JSONArray types_Array = response.getJSONArray("types");
                            tv_type.setText("Type: ");
                            for(int i=0;i<types_Array.length();i++){
                                JSONObject obj_arr_type = types_Array.getJSONObject(i);
                                JSONObject obj_type = obj_arr_type.getJSONObject("type");
                                String type_obj = obj_type.getString("name");
                                if(i==(types_Array.length()-1)){
                                    tv_type.append(type_obj);
                                }else {
                                    tv_type.append(type_obj+", ");
                                }
                            }
                            JSONArray stats_Array = response.getJSONArray("stats");
                            JSONObject obj_0 = stats_Array.getJSONObject(0);
                            String speed_obj = obj_0.getString("base_stat");
                            JSONObject obj_1 = stats_Array.getJSONObject(1);
                            String sp_def_obj = obj_1.getString("base_stat");
                            JSONObject obj_2 = stats_Array.getJSONObject(2);
                            String sp_atk_obj = obj_2.getString("base_stat");
                            JSONObject obj_3 = stats_Array.getJSONObject(3);
                            String def_obj = obj_3.getString("base_stat");
                            JSONObject obj_4 = stats_Array.getJSONObject(4);
                            String atk_obj = obj_4.getString("base_stat");

                            JSONObject spr_obj = response.getJSONObject("sprites");
                            String url_bac_def = spr_obj.getString("back_default");
                            String url_bac_shi = spr_obj.getString("back_shiny");
                            String url_front_def = spr_obj.getString("front_default");
                            String url_front_shi = spr_obj.getString("front_shiny");

                            setImg_to_iv(url_front_shi,iv_top_l);
                            setImg_to_iv(url_bac_shi,iv_top_r);
                            setImg_to_iv(url_front_def,iv_bot_l);
                            setImg_to_iv(url_bac_def,iv_bot_r);
                            tv_name.setText(poke_name);
                            tv_atk.setText("Atk: "+atk_obj);
                            tv_def.setText("Def: "+def_obj);
                            tv_sp_atk.setText("Sp-Atk: "+sp_atk_obj);
                            tv_sp_def.setText("Sp-Def: "+sp_def_obj);
                            tv_speed.setText("Speed: "+speed_obj);
                            Log.d("Click123456",poke_name);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void setImg_to_iv(String url,ImageView iv){
        Picasso.with(getApplicationContext()).load(url).into(iv);
    }
}
