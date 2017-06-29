package com.zedeff.marvellous;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zedeff.marvellous.api.ApiClient;
import com.zedeff.marvellous.api.MarvelApi;
import com.zedeff.marvellous.api.models.Comic;
import com.zedeff.marvellous.api.models.ComicDataWrapper;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ApiClientTest {

    private MarvelApi api;

    @Before
    public void SetUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        ApiClient client = new ApiClient(context);
        api = client.createMarvelApi();
    }

    @Test
    public void canGet100Comics() {
        ComicDataWrapper cdw = api.getComics().blockingGet();
        assertNotNull(cdw);
        assertNotNull(cdw.data);
        assertNotNull(cdw.data.results);
        assertThat(cdw.data.results.size(), is(100));
    }

    @Test
    public void FirstComicIsSilverSurfer() {
        ComicDataWrapper cdw = api.getComics().blockingGet();
        Comic firstComic = cdw.data.results.get(0);
        assertThat(firstComic, notNullValue());
        assertThat(firstComic.id, greaterThan(0));
        assertThat(firstComic.title, is("Silver Surfer (1987)"));
        assertThat(firstComic.pageCount, is(0));
        assertThat(firstComic.description, Matchers.nullValue());
    }
}
