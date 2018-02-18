package any.audio.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 5/23/2017.
 */

class SupportedPlaylistsResponse {


    @SerializedName("metadata")
    public Metadata metadata;
    @SerializedName("requestLocation")
    public String requestlocation;
    @SerializedName("results")
    public List<Results> results;
    @SerializedName("status")
    public int status;

    public static class Metadata {
        @SerializedName("count")
        public int count;
    }

    public static class Results {
        @SerializedName("playlist")
        public String playlist;
        @SerializedName("url")
        public String url;
    }
}
