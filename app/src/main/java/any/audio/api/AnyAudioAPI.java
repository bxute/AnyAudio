package any.audio.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ankit on 5/23/2017.
 */

public interface AnyAudioAPI {

    //Getting Audio from Video ID
    @GET("g")
    Call<VideoIDResponse> getVideoId(@Query("url") String url);

    //Getting Youtube Search Results
    @GET("search")
    Call<YoutubeSearchResponse> search(@Query("q") String search_term);

    //todo: issue with api
    //Getting Trending Songs
    @GET("trending")
    Call<TrendingResponse> getTrendings(@Query("number") String number,@Query("offset") String offset,@Query("type") String type);

    //Getting supported Playlists
    @GET("playlists")
    Call<SupportedPlaylistsResponse> getPlaylists();

}
