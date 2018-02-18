package any.audio.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 5/23/2017.
 */

class VideoIDResponse {

    @SerializedName("url")
    public String url;

    public VideoIDResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VideoIDResponse{" +
                "url='" + url + '\'' +
                '}';
    }
}
