package com.example.miapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.miapp.adaptadores.MyAppAdapter;
import com.example.miapp.modelos.MyAppModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String datos = "https://nubecolectiva.com/blog/tutos/demos/leer_json_android_java/datos/postres.json";
    private final int codigodatos = 1;
    private RecyclerView recyclerView;
    ArrayList<MyAppModel> myappModelArrayList;
    private MyAppAdapter myappAdapter;
    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Llamo al layout para el RecyclerView
        recyclerView = findViewById(R.id.contenedor);

        // Llamo al método para leer el archivo JSON (Este método lo crearé a continuación)
        leerJSON();
    }

    @SuppressLint("StaticFieldLeak")
    private void leerJSON(){

        // Inicio una tare Asíncrona
        new AsyncTask<Void, Void, String>(){

            // La tarea se llevará acabo de fondo
            protected String doInBackground(Void[] params) {

                String response = "";

                // Declaro un HashMap
                HashMap<String, String> map = new HashMap<>();

                // Hago la petición de los datos
                try {
                    HttpRequest req = new HttpRequest(datos);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }

            // Después de realizar la petición de los datos, llamo al método tareaCompletada()
            // El método tareaCompletada() lo crearé a continuación
            protected void onPostExecute(String resultado) {
                tareaCompletada(resultado, codigodatos);
            }

        }.execute();
    }

    public void tareaCompletada(String response, int serviceCode) {
        switch (serviceCode) {

            // Uso un case y le paso la variable 'codigodatos'
            case codigodatos:

                // Verifico si los datos se recibieron con el método siCorrecto()
                // El método siCorrecto() lo crearé más adelante.
                if (siCorrecto(response)) {

                    // A mi modelo le paso el método obtenerInformacion(), este método lo crearé más adelante.
                    myappModelArrayList = obtenerInformacion(response);

                    // A mi Adaptador le paso mi modelo
                    myappAdapter = new MyAppAdapter(this,myappModelArrayList);

                    // Le paso mi adaptador al RecyclerView
                    recyclerView.setAdapter(myappAdapter);

                    // Cargo el Layout del RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                }else {
                    // Si hubo error, lo muestro en un Toast
                    Toast.makeText(MainActivity.this, obtenerCodigoError(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public ArrayList<MyAppModel> obtenerInformacion(String response) {

        // Creo un array con los datos JSON que he obtenido
        ArrayList<MyAppModel> listaArray = new ArrayList<>();

        // Solicito los datos al archivo JSON
        try {
            JSONObject jsonObject = new JSONObject(response);

            // En los datos que recibo verifico si obtengo el estado o 'status' con el valor 'true'
            // El dato 'status' con el valor 'true' se encuentra dentro del archivo JSON
            if (jsonObject.getString("status").equals("true")) {

                // Accedo a la fila 'postres' del archivo JSON
                JSONArray dataArray = jsonObject.getJSONArray("postres");

                // Recorro los datos que hay en la fila 'postres' del archivo JSON
                for (int i = 0; i < dataArray.length(); i++) {

                    // Creo la variable 'datosModelo' y le paso mi modelo 'MyAppModel'
                    MyAppModel datosModelo = new MyAppModel();

                    // Creo la  variable 'objetos' y recupero los valores
                    JSONObject objetos = dataArray.getJSONObject(i);

                    // Selecciono dato por dato
                    datosModelo.setNombre(objetos.getString("nombre"));
                    datosModelo.setPrecio(objetos.getString("precio"));
                    datosModelo.setStock(objetos.getString("stock"));
                    datosModelo.setImagen(objetos.getString("img"));

                    // Meto los datos en el array que definí más arriba 'listaArray'
                    listaArray.add(datosModelo);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Devuelvo el array con los datos del archivo JSON
        return listaArray;
    }

    public boolean siCorrecto(String response) {

        // Verificamos si la petición de los datos ha sido correcta
        try {

            // Creo la variable 'objetoJson' de tipo JSONObjetc (Objeto JSON) y le
            // paso los datos que he recibido (response)
            JSONObject objetoJson = new JSONObject(response);

            // En los datos que he recibido verifico si obtengo el estado o 'status' con el valor 'true'
            // El dato 'status' con el valor 'true' se encuentra dentro del archivo JSON
            if (objetoJson.optString("status").equals("true")) {

                return true; // Retorno 'true' si es correcto

            } else {

                return false; // Retorno 'false' si es correcto
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Si nada se lleva acabo retorno 'false'
        return false;
    }

    public String obtenerCodigoError(String response) {

        // Solicitamos el código de error que se encuentra en el archivo JSON
        try {
            // El archivo JSON contiene el dato 'message'
            JSONObject jsonObject = new JSONObject(response);

            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Si no hay datos en el archiv JSON, muestro un mensaje
        return "No hay datos";
    }

}
