package dog.snow.androidrecruittest;


import org.junit.Test;

import java.io.IOException;
import java.util.List;

import dog.snow.androidrecruittest.repository.model.RawPhoto;
import dog.snow.androidrecruittest.repository.service.PhotoService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static org.junit.Assert.*;


public class RESTFetchTest {

    @Test
    public void testRestFetch() throws IOException {
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PhotoService service = rf.create(PhotoService.class);
        List<RawPhoto> photos = service.getPhotos().execute().body();
        System.out.println(photos);
        assertEquals(5000, photos.size());
    }

}
