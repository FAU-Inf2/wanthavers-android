package wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media;

import android.content.Context;
import android.support.annotation.NonNull;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.fau.cs.mad.wanthavers.common.Media;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.MediaClient;
import wanthavers.mad.cs.fau.de.wanthavers_android.rest.RestClient;

/**
 * Created by Nico on 22.05.2016.
 */
public class MediaRemoteDataSource implements MediaDataSource {
    private static MediaRemoteDataSource INSTANCE;

    private MediaClient mediaEndpoint;
    private Context context;

    private MediaRemoteDataSource(Context context) {
        mediaEndpoint = MediaClient.getInstance(context);
        this.context = context;
    }

    public static MediaRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MediaRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllMedia(@NonNull GetAllMediaCallback callback) {
        try {
            List<Media> media = mediaEndpoint.getAllMedia();
            callback.onAllMediaLoaded(media);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMedia(@NonNull long mediaId, @NonNull GetMediaCallback callback) {
        try {
            Media media = mediaEndpoint.get(mediaId);
            callback.onMediaLoaded(media);
        } catch (Throwable t) {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void createMedia(@NonNull InputStream inputStream, @NonNull FormDataContentDisposition contentDispositionHeader, @NonNull CreateMediaCallback callback) {
        try {
            Media media = mediaEndpoint.createMedia(inputStream, contentDispositionHeader);
            callback.onMediaCreated(media);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }

    @Override
    public void createMedia(@NonNull File image, @NonNull CreateMediaCallback callback) {

/*        File tmp = null;

        try {
            //create a file to write bitmap data
            tmp = File.createTempFile("tmp", null, context.getCacheDir())

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 *//*ignored for PNG*//*, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(tmp);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException io) {
            callback.onCreateFailed();
        }*/

        final FormDataMultiPart multiPart = new FormDataMultiPart();
        if (image != null) {
            multiPart.bodyPart(new FileDataBodyPart("file", image,
                    MediaType.APPLICATION_OCTET_STREAM_TYPE));
        }

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(RestClient.API_URL).path("v1/media");

        Invocation.Builder invocationBuilder = target.request(MediaType.MULTIPART_FORM_DATA);

        try {
            Response response = invocationBuilder.post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));
            Media ret = response.readEntity(Media.class);
            callback.onMediaCreated(ret);
        } catch (Throwable t) {
            callback.onCreateFailed();
        }
    }
}