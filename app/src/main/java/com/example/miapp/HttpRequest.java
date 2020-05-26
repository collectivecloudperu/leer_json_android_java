package com.example.miapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    // Métodos de solicitud Http admitidos
    public static enum Method{
        POST,PUT,DELETE,GET;
    }
    private URL url;
    private HttpURLConnection con;
    private OutputStream os;

    // Después de la creación de instancias, al abrir la conexión: puede ocurrir una excepción
    public HttpRequest(URL url)throws IOException {
        this.url = url;
        con = (HttpURLConnection)this.url.openConnection();
    }

    // Se puede crear una instancia con la representación de cadena de la url, obligar a la persona que llama a verificar la IOException que se puede lanzar
    public HttpRequest(String url)throws IOException{ this(new URL(url)); Log.d("parameters", url); }

    private void prepareAll(Method method)throws IOException{
        con.setDoInput(true);
        con.setRequestMethod(method.name());
        if(method== Method.POST||method== Method.PUT){
            con.setDoOutput(true);
            os = con.getOutputStream();
        }
    }
    // Preparo la solicitud con el método GET
    public HttpRequest prepare() throws IOException{
        prepareAll(Method.GET);
        return this;
    }

    public HttpRequest prepare(Method method)throws IOException{
        prepareAll(method);
        return this;
    }

    public HttpRequest withHeaders(String... headers){
        for(int i=0,last=headers.length;i<last;i++) {
            String[]h=headers[i].split("[:]");
            con.setRequestProperty(h[0],h[1]);
        }
        return this;
    }

    public HttpRequest withData(String query) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.close();
        return this;
    }

    public HttpRequest withData(HashMap<String,String> params) throws IOException{
        StringBuilder result=new StringBuilder();
        for(Map.Entry<String,String>entry : params.entrySet()){
            result.append((result.length()>0?"&":"")+entry.getKey()+"="+entry.getValue());//appends: key=value (for first param) OR &key=value(second and more)
            Log.d("parameters",entry.getKey()+"  ===>  "+ entry.getValue());
        }
        withData(result.toString());
        return this;
    }

    // Devuelvo el código de estado HTTP para indicar si se hizo la solicitud correctamente
    public int send() throws IOException{
        return con.getResponseCode();
    }

    public String sendAndReadString() throws IOException{
        BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response=new StringBuilder();
        for(String line;(line=br.readLine())!=null;)response.append(line+"\n");
        Log.d("ressss",response.toString());
        return response.toString();
    }

    public byte[] sendAndReadBytes() throws IOException{
        byte[] buffer = new byte[8192];
        InputStream is = con.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (int bytesRead;(bytesRead=is.read(buffer))>=0;)output.write(buffer, 0, bytesRead);
        return output.toByteArray();
    }
    // Represento el JSONObject (Objeto JSON) con la respuesta que obtengo del array de datos
    public JSONObject sendAndReadJSON() throws JSONException, IOException{
        return new JSONObject(sendAndReadString());
    }

}
