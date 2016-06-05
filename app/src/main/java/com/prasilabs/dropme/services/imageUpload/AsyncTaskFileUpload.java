package com.prasilabs.dropme.services.imageUpload;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by prasi on 1/2/16.
 */
public class AsyncTaskFileUpload extends AsyncTask<String, String, String>
{
    private File file;
    private String fileName;
    private Context context;
    private AsyncImageUploadCallBack listener;

    public static void uploadImage(Context context, File file, String fileName, AsyncImageUploadCallBack asyncImageUploadCallBack)
    {
        AsyncTaskFileUpload asyncTaskFileUpload = new AsyncTaskFileUpload(context, asyncImageUploadCallBack);
        asyncTaskFileUpload.file = file;
        asyncTaskFileUpload.fileName = fileName;

        asyncTaskFileUpload.execute();
    }

    public AsyncTaskFileUpload(Context context, AsyncImageUploadCallBack listener){
        this.listener=listener;
        this.context = context;
    }


    @Override
    protected String doInBackground(String... content)
    {
        uploadFile("");

        return " ";
    }

    public long uploadFile(String stringUrl)
    {
        long actuaStartlTime = 0 ;
        double debutPaquet ;
        double[] tabTotalDebit= new double [10];
        long TotalSize=0;
        HttpURLConnection comm = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead,bytesReadBis,bytesAvailable,bufferSize;
        byte []buffer;
        int maxBufferSize =   10240 ;

        if(!file.isFile())
        {
            return 0;
        }
        else
        {
            try
            {
                FileInputStream fis = new FileInputStream(file);
                URL url = new URL(stringUrl);

                comm = (HttpURLConnection)url.openConnection();
                comm.setDoInput(true);
                comm.setDoOutput(true);
                comm.setUseCaches(false);
                comm.setRequestMethod("POST");
                comm.setRequestProperty("Connection", "Keep-Alive");
                comm.setRequestProperty("ENCTYPE", "multipart/form-data");
                comm.setRequestProperty("Content-Type", "multipart/form-data;boundary=" +boundary);
                comm.setRequestProperty("uploaded_file", fileName);
                dos = new DataOutputStream(comm.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"upload\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                bytesAvailable = fis.available();
                TotalSize = bytesAvailable ;
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fis.read(buffer,0,bufferSize);
                long global = System.currentTimeMillis();
                debutPaquet = System.currentTimeMillis() ;
                double totalTime =0;

                while(bytesRead > 0)

                { dos.write(buffer,0,bufferSize);
                    bytesAvailable = fis.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fis.read(buffer,0,bufferSize);
                    publishProgress (bytesRead+"");

                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                int serverResonseCode = comm.getResponseCode();
                String serverResponseMessage = comm.getResponseMessage();
                if(serverResonseCode == 200)
                { String msg = "File Upload Completed. \n\n See uploaded file here : /var/www/uploads";
                }

                fis.close();
                dos.flush();
                dos.close();
            }
            catch(MalformedURLException e)
            {

                e.printStackTrace();

                Log.e("Upload file to server", "error: " + e.getMessage(), e);
            }
            catch(Exception e)
            {

                e.printStackTrace();

                Log.e("Upload file to server", "error: " + e.getMessage(), e);
            }

            return TotalSize;
        }
    }
    protected void onProgressUpdate(String... progress) {
    }
    protected void onPostExecute(String reslut)
    {
        Log.i("msg","Upload comlete. \n\n See Upload file here : : /var/www/uploads ");
        listener.uploaded(true, reslut);
    }

    public interface AsyncImageUploadCallBack
    {
        void uploaded(boolean status, String url);
    }

}
