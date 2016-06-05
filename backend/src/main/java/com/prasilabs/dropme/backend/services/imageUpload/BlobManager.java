package com.prasilabs.dropme.backend.services.imageUpload;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.prasilabs.dropme.backend.core.CoreController;
import com.prasilabs.dropme.backend.debug.ConsoleLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by prasi on 4/4/16.
 */
public class BlobManager
{
    private static final String TAG = BlobManager.class.getSimpleName();
    private static BlobManager instance = new BlobManager();

    public static BlobManager getInstance()
    {
        return instance;
    }

    public String getBlobUrl()
    {
        BlobstoreService blobService = BlobstoreServiceFactory.getBlobstoreService();
        String uploadUrl = "";
        if(CoreController.isDebug)
        {
            uploadUrl = blobService.createUploadUrl("/UploadServlet", UploadOptions.Builder.withGoogleStorageBucketName("findspaceimages"));
        }
        else
        {
            uploadUrl = blobService.createUploadUrl("/UploadServlet", UploadOptions.Builder.withGoogleStorageBucketName("findspaceimages_production"));
        }

        return uploadUrl;
    }

    public JSONArray uploadBlob(HttpServletRequest request)
    {
        ConsoleLog.l(TAG, "upload blob called");
        BlobstoreService blobService = BlobstoreServiceFactory.getBlobstoreService();

        List<BlobKey> blobs = blobService.getUploads(request).get("file");
        if (blobs == null || blobs.isEmpty()) throw new IllegalArgumentException("No blobs given");

        JSONArray jsonArray = new JSONArray();
        for(BlobKey blobKey : blobs)
        {
            JSONObject json = new JSONObject();
            try {
                ImagesService imagesService = ImagesServiceFactory.getImagesService();
                ServingUrlOptions servingOptions = ServingUrlOptions.Builder.withBlobKey(blobKey);
                String servingUrl = imagesService.getServingUrl(servingOptions);

                BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
                BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);

                try {
                    json.put("imageUrl", servingUrl);
                    json.put("imageId", blobInfo.getFilename());
                    json.put("blobKey", blobKey.getKeyString());
                } catch (Exception e) {
                    ConsoleLog.e(e);
                }
            } catch (Exception ex) {
                ConsoleLog.e(ex);
            }
            jsonArray.put(json);
        }

        return jsonArray;
    }

    public boolean deleteBlob(String blobKeyStr)
    {
        List<String> blobKeyStrList = new ArrayList<>();
        blobKeyStrList.add(blobKeyStr);

        return deleteBlob(blobKeyStrList);
    }

    public boolean deleteBlob(List<String> blobKeyStrList)
    {
        boolean success = false;
        try
        {
            BlobKey[] blobKeys = new BlobKey[blobKeyStrList.size()];
            for(int i = 0; i < blobKeyStrList.size(); i++)
            {
                blobKeys[i] = new BlobKey(blobKeyStrList.get(i));
            }

            BlobstoreService blobService = BlobstoreServiceFactory.getBlobstoreService();
            blobService.delete(blobKeys);
            success = true;
        }catch (Exception e)
        {
            success = false;
        }

        return success;
    }
}
