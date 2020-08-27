package dog.snow.androidrecruittest;


import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Orderer;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dog.snow.androidrecruittest.repository.model.RawAlbum;
import dog.snow.androidrecruittest.repository.model.RawPhoto;
import dog.snow.androidrecruittest.repository.model.RawUser;
import dog.snow.androidrecruittest.repository.service.AlbumService;
import dog.snow.androidrecruittest.repository.service.PhotoService;
import dog.snow.androidrecruittest.repository.service.UserService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RESTFetchTest {

    Retrofit rf;
    static Set<Integer> albumIds;

    @Before
    public void createRetrofit() {
        rf = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Test
    public void a_testPhotosFetch() throws IOException {
        PhotoService service = rf.create(PhotoService.class);

        List<RawPhoto> photos = service.getAllPhotos().execute().body();
        assertEquals(5000, photos.size());

        List<RawPhoto> photos100 = service.getPhotos(100).execute().body();
        System.out.println(photos100);
        assertEquals(100, photos100.size());

        albumIds = photos100.stream().mapToInt(RawPhoto::getAlbumId).boxed().collect(Collectors.toSet());
    }

    @Test
    public void b_testAlbumsFetch() throws IOException {
        AlbumService service = rf.create(AlbumService.class);

        for(Integer id: albumIds) {
            RawAlbum album = service.getAlbum(id).execute().body();

            assertNotNull(album);
            assertFalse(album.getTitle().isEmpty());
        }
    }

    @Test
    public void c_testUserFetch() throws IOException {
        UserService service = rf.create(UserService.class);

        RawUser user = service.getUser(1).execute().body();

        assertNotNull(user);
        assertFalse(user.getName().isEmpty());
    }

}