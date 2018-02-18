package any.audio.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 5/23/2017.
 */

class YoutubeSearchResponse {

    @SerializedName("metadata")
    public Metadata metadata;
    @SerializedName("results")
    public List<Results> results;

    public YoutubeSearchResponse(Metadata metadata, List<Results> results) {
        this.metadata = metadata;
        this.results = results;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "YoutubeSearchResponse{" +
                "metadata=" + metadata +
                ", results=" + results +
                '}';
    }

    public static class Metadata {
        @SerializedName("q")
        public String q;
        @SerializedName("count")
        public int count;

        public Metadata(String q, int count) {
            this.q = q;
            this.count = count;
        }

        public String getQ() {
            return q;
        }

        public void setQ(String q) {
            this.q = q;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "Metadata{" +
                    "q='" + q + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
    public static class Results {
        @SerializedName("get_url")
        public String getUrl;
        @SerializedName("id")
        public String id;
        @SerializedName("length")
        public String length;
        @SerializedName("thumb")
        public String thumb;
        @SerializedName("time")
        public String time;
        @SerializedName("title")
        public String title;
        @SerializedName("uploader")
        public String uploader;
        @SerializedName("views")
        public String views;
        @SerializedName("description")
        public String description;
        @SerializedName("suggest_url")
        public String suggestUrl;

        public Results(String getUrl, String id, String length, String thumb, String time, String title, String uploader, String views, String description, String suggestUrl) {
            this.getUrl = getUrl;
            this.id = id;
            this.length = length;
            this.thumb = thumb;
            this.time = time;
            this.title = title;
            this.uploader = uploader;
            this.views = views;
            this.description = description;
            this.suggestUrl = suggestUrl;
        }

        public String getGetUrl() {
            return getUrl;
        }

        public void setGetUrl(String getUrl) {
            this.getUrl = getUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUploader() {
            return uploader;
        }

        public void setUploader(String uploader) {
            this.uploader = uploader;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSuggestUrl() {
            return suggestUrl;
        }

        public void setSuggestUrl(String suggestUrl) {
            this.suggestUrl = suggestUrl;
        }

        @Override
        public String toString() {
            return "Results{" +
                    "getUrl='" + getUrl + '\'' +
                    ", id='" + id + '\'' +
                    ", length='" + length + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", time='" + time + '\'' +
                    ", title='" + title + '\'' +
                    ", uploader='" + uploader + '\'' +
                    ", views='" + views + '\'' +
                    ", description='" + description + '\'' +
                    ", suggestUrl='" + suggestUrl + '\'' +
                    '}';
        }
    }

}
