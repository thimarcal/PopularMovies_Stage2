package gmp.thiago.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiagom on 11/13/17.
 */

public class ReviewJson implements Parcelable {

    /**
     * id : 284053
     * page : 1
     * results : [{"id":"59f09b979251416ac10169f7","author":"Gimly","content":"_Ragnarok_ is a tough one to review completely without spoiler, so I'll keep this brief:\r\n\r\n* I was bothered by a small number of things that happened in this movie.\r\n* I was in love with a large number of things that happened in this movie.\r\n* _Thor: Ragnarok_ is not the MCU's best movie.\r\n* _Thor: Ragnarok_ is the MCU's most fun movie.\r\n* Can we **please** have a worldwide ban on putting anything that happens in the third act of your movie, in said movie's trailers?\r\nThank you for your time. 4 stars.\r\n\r\n\r\n_Final rating:★★★★ - Very strong appeal. A personal favourite._","url":"https://www.themoviedb.org/review/59f09b979251416ac10169f7"},{"id":"5a08bdccc3a368672a006056","author":"Movie Queen41","content":"The Best CBM movie of 2017\r\n\r\nThe film is one of the most entertaining of the MCU films so far and a HUGE improvement over Thor: The Dark World. Cate Blanchett as the villainess Hela, the Goddess of Death, is a far better villain that TDW's lame Malekith. Kat Dennings' annoying Darcy and Natalie Portman's bland Jane are not missed at all. Instead, we get Tessa Thompson's badass Valkyrie, who can hold her own in a fight and isn't just there just as a love interest for Thor (Chris Hemsworth). Jeff Goldblum is hilarious as Grandmaster, who runs the gladiator fights on the planet Sakaar. But the real pleasure of the movie is watching the love-hate brother relationship between Thor and his adoptive brother, Loki (Tom Hiddleston). The relationship between the two men was the only redeeming feature of TDW, and it's great to see it expanded in this movie. Also great is seeing Thor buddy up with Bruce Banner/ Hulk (Mark Ruffalo). The only thing I didn't like about the movie was the fate of The Warriors Three. They deserved better than that. But otherwise, it's a great comic book movie.","url":"https://www.themoviedb.org/review/5a08bdccc3a368672a006056"}]
     * total_pages : 1
     * total_results : 2
     */

    private int id;
    private int page;
    private int total_pages;
    private int total_results;
    private List<Review> results;

    public ReviewJson(Parcel parcel) {
        id = parcel.readInt();
        page = parcel.readInt();
        total_pages = parcel.readInt();
        total_results = parcel.readInt();
        results = new ArrayList<>();
        parcel.readList(results, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(page);
        parcel.writeInt(total_pages);
        parcel.writeInt(total_results);
        parcel.writeArray(results.toArray());
    }

    public static final Parcelable.Creator<ReviewJson> CREATOR = new Creator<ReviewJson>() {
        @Override
        public ReviewJson createFromParcel(Parcel in) {
            return new ReviewJson(in);
        }

        @Override
        public ReviewJson[] newArray(int size) {
            return new ReviewJson[size];
        }
    };

    public static class Review implements Parcelable {

        private String id;
        private String author;
        private String content;
        private String url;

        public Review(Parcel parcel) {
            id = parcel.readString();
            author = parcel.readString();
            content = parcel.readString();
            id = parcel.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(author);
            parcel.writeString(content);
            parcel.writeString(url);
        }

        public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
            @Override
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            @Override
            public Review[] newArray(int size) {
                return new Review[size];
            }
        };
    }
}
