package gmp.thiago.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiagom on 11/13/17.
 */

public class TrailerJson implements Parcelable {

    private int id;
    private List<Trailer> results;

    public TrailerJson (Parcel parcel) {
        id = parcel.readInt();
        results = new ArrayList<>();
        parcel.readList(results, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeArray(results.toArray());
    }

    public static final Parcelable.Creator<TrailerJson> CREATOR = new Creator<TrailerJson>() {
        @Override
        public TrailerJson createFromParcel(Parcel in) {
            return new TrailerJson(in);
        }

        @Override
        public TrailerJson[] newArray(int size) {
            return new TrailerJson[size];
        }
    };

    public static class Trailer implements Parcelable {

        private String id;
        private String iso_639_1;
        private String iso_3166_1;
        private String key;
        private String name;
        private String site;
        private int size;
        private String type;

        public Trailer(Parcel parcel) {
            id = parcel.readString();
            iso_639_1 = parcel.readString();
            iso_3166_1 = parcel.readString();
            key = parcel.readString();
            name = parcel.readString();
            site = parcel.readString();
            size = parcel.readInt();
            type = parcel.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(iso_639_1);
            parcel.writeString(iso_3166_1);
            parcel.writeString(key);
            parcel.writeString(name);
            parcel.writeString(site);
            parcel.writeInt(size);
            parcel.writeString(type);
        }

        public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
            @Override
            public Trailer createFromParcel(Parcel in) {
                return new Trailer(in);
            }

            @Override
            public Trailer[] newArray(int size) {
                return new Trailer[size];
            }
        };
    }
}
