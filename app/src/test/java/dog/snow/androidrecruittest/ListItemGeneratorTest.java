package dog.snow.androidrecruittest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import dog.snow.androidrecruittest.repository.CollectionToTreeMap;
import dog.snow.androidrecruittest.repository.model.RawAlbum;
import dog.snow.androidrecruittest.repository.model.RawPhoto;
import dog.snow.androidrecruittest.ui.model.ListItem;
import dog.snow.androidrecruittest.ui.model.ListItemGenerator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListItemGeneratorTest {

    public static ArrayList<RawPhoto> photoList;
    public static ArrayList<RawAlbum> albumArrayList;
    public static TreeMap<Integer, RawAlbum> albumTreeMap;

    @Before
    public void beforeTest() {
        photoList = new ArrayList<RawPhoto>();
        photoList.addAll(Arrays.asList(photosSource));
        albumArrayList = new ArrayList<RawAlbum>();
        albumArrayList.addAll(Arrays.asList(albumsSource));
    }

    @Test
    public void a_albumCollectionToTreeMapTest() {
        albumTreeMap = CollectionToTreeMap.INSTANCE.getAlbumTree(albumArrayList);

        assertEquals(albumsSource.length, albumTreeMap.size());
        for(RawAlbum a: albumsSource) {
            assertTrue(albumTreeMap.containsKey(a.getId()));
            assertNotNull(albumTreeMap.get(a.getId()));
            assertTrue(albumTreeMap.get(a.getId()).getTitle().equalsIgnoreCase(a.getTitle()));
            assertEquals(a.getUserId(), albumTreeMap.get(a.getId()).getUserId());
        }
    }

    @Test
    public void b_listItemGeneratorTest() {
        List<ListItem> items = ListItemGenerator.INSTANCE.generateFromPhotosAndAlbums(photoList, albumTreeMap);

        assertEquals(photosSource.length, items.size());
        for(ListItem i: items) {
            assertNotNull(i);
            RawPhoto tmp = FindPhotoById(i.getId());

            assertNotNull(tmp); // Assert that this photo was indeed in the array

            RawAlbum tmpAlbum = albumTreeMap.containsKey(tmp.getAlbumId())
                    ? albumTreeMap.get(tmp.getAlbumId()) : null;
            if(tmpAlbum == null)
                assertEquals("", i.getAlbumTitle());
            else
                assertEquals(tmpAlbum.getTitle(), i.getAlbumTitle());

            assertEquals(tmp.getTitle(), i.getTitle());
            assertEquals(tmp.getThumbnailUrl(), i.getThumbnailUrl());
        }
    }

    private static RawPhoto FindPhotoById(int id) {
        for(RawPhoto p: photosSource) {
            if(p.getId() == id)
                return p;
        }
        return null;
    }

    public static final String URL_PREFIX = "https://via.placeholder.com/images/";
    public static final RawPhoto[] photosSource = {
            new RawPhoto(1, 1, "ABC", URL_PREFIX + "image1.jpg", URL_PREFIX+"thumbnail1.jpg"),
            new RawPhoto(2, 1, "DEF", URL_PREFIX + "image55.jpg", URL_PREFIX+"thumbnail51.jpg"),
            new RawPhoto(3, 1, "123456", URL_PREFIX + "image009.jpg", URL_PREFIX+"thumbnail_009.jpg"),
            new RawPhoto(7, 2, "photo 1", URL_PREFIX + "img1.jpg", URL_PREFIX+"thumb1.jpg")
    };
    public static final RawAlbum[] albumsSource = {
            new RawAlbum(1, 1, "Album 1"),
            new RawAlbum(2, 1, "Second album"),
            new RawAlbum(4, 2, "my first album")
    };

}
