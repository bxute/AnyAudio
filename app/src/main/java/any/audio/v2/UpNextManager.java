package any.audio.v2;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ankit on 5/31/2017.
 */

public class UpNextManager {

    /**
     * modes of player
     */
    private static final int MODE_QUEUE = 0;
    private static final int MODE_AUTOPLAY = 1;

    /**
     * player`s next item behavior
     */
    private static final int REPEAT_ALL = 2;
    private static final int REPEAT_NONE = 3;

    private int[] indexes;
    private int mCurrentPlayerMode = MODE_AUTOPLAY;
    private int mCurrentRepeatMode = REPEAT_ALL;
    private int mCurrentPlayingIndex = -1;

    /**
     * @IntDef PlayerMode for strict params check
     */
    @Retention(RetentionPolicy.SOURCE)

    @IntDef({MODE_QUEUE, MODE_AUTOPLAY})
    public @interface PlayerMode {
    }

    /**
     * @IntDef RepeatMode for strict params check
     */
    @IntDef({REPEAT_ALL, REPEAT_NONE})
    public @interface RepeatMode {
    }

    private static int mQueueLength = 0;

    /**
     * Holds the current items in the queue. Add,remove will be done on this list
     */
    private ArrayList<Object> originalQueueItems = new ArrayList<>();

    /**
     * Holds the current suffled items
     */
    private ArrayList<Object> currentSuffledQueueItems = new ArrayList<>();

    /**
     * Holds the current autoplay list
     */
    private ArrayList<Object> currentAutoPlayList = new ArrayList<>();


    public void initManager(){
        //todo: load all the old queue elements and mode of play from shared pref.
    }

    /**
     * @param playerMode is set when user switch the Queue-> Auto-play or vice-verca
     */
    public void setPlayerMode(@PlayerMode int playerMode){
        this.mCurrentPlayerMode = playerMode;
    }

    /**
     * @param repeatMode is set when user tap for different repeat options
     */
    public void setRepeatMode(@RepeatMode int repeatMode){
        this.mCurrentRepeatMode = repeatMode;
    }

    /**
     * Suffles the current list
     */
    public ArrayList<Object> suffle(){
        suffleQueueItems();
        return getSuffledItems();
    }

    /**
     * @param autoPlayList Playlist Manager assigns the latest fetched auto-playlist
     */
    public void setAutoPlayList(ArrayList<Object> autoPlayList) {
        this.currentAutoPlayList = autoPlayList;
    }

    /**
     * @param item is added to original queue after checking its previous exitence
     *             After addition, current suffled items are refreshed(suffled again)
     *             and return the message
     */
    public boolean addItemToQueue(Object item) {

        if (!itemAlreadyEnqueued(item)) {
            originalQueueItems.add(item);
            mQueueLength++;
            suffleQueueItems();
            return true;
        }

        return false;
    }

    /**
     * @param item is checked in the original queue for its pre-existence
     * @return if found then - yes else no
     */
    private boolean itemAlreadyEnqueued(Object item) {
        for (Object o :
                originalQueueItems) {
                suffleQueueItems();
            if (o.equals(item))
                return true;
        }
        return false;
    }

    /**
     *  It suffles a list of integers say [0,1,2.......N] where (N+1) items are in the original queue
     *  Instead of suffling the actual Media Object, we suffle only the indexes.
     *
     */
    private void suffleQueueItems() {

        indexes = new int[mQueueLength];
        Random rnd = ThreadLocalRandom.current();
        int i=0;

        while(i<mQueueLength){
            indexes[i] = i++;
        }
        while(i>0){
            int index = rnd.nextInt(i + 1);
            int a = indexes[index];
            indexes[index] = indexes[i];
            indexes[i--] = a;
        }

    }

    /**
     * @return the latest suffled items based on the items in the original queue
     *
     */
    private ArrayList<Object> getSuffledItems(){
        int i=0;
        currentSuffledQueueItems.clear();
        while(i<mQueueLength){
            currentSuffledQueueItems.add(i,originalQueueItems.get(indexes[i]));
            i++;
        }
        return currentSuffledQueueItems;
    }

    /**
     * @return the current auto-playlist
     */
    private ArrayList<Object> getCurrentAutoPlayList(){
        return currentAutoPlayList;
    }

    /**
     * Clears the playlist
     */
    private void clearAutoPlaylist(){
        currentAutoPlayList.clear();
    }

    /**
     * Clears the original queue
     */
    private void clearQueueItems(){
        originalQueueItems.clear();
    }

    /**
     * @param index element is removed from original queue
     */
    private void removeItem(int index){

        originalQueueItems.remove(index);
        mQueueLength--;
        suffleQueueItems();

    }

    public Object getUpNext(){
        return null;
    }



}
