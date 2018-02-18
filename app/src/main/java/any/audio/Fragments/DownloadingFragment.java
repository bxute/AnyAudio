package any.audio.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import any.audio.Adapters.DownloadingAdapter;
import any.audio.Config.Constants;
import any.audio.Interfaces.DownloadCancelListener;
import any.audio.Models.DownloadingItemModel;
import any.audio.R;
import any.audio.SharedPreferences.SharedPrefrenceUtils;
import any.audio.helpers.TaskHandler;

/**
 * Created by Ankit on 2/8/2017.
 */

public class DownloadingFragment extends Fragment {

    private ListView liveDownloadListView;
    private DownloadingAdapter adapter;


    DownloadCancelListener downloadCancelListener = new DownloadCancelListener() {
        @Override
        public void onDownloadCancel(String taskID) {
            cancelItem(taskID);

        }
    };

    private ProgressUpdateBroadcastReceiver receiver;
    private boolean mReceiverRegistered;
    private int mScrollState;
    private TextView emptyMessage;

    private void cancelItem(String taskID) {

        TaskHandler handler = TaskHandler.getInstance(getActivity());
        // remove specific task
        handler.removeTask(taskID);
        handler.setCancelled(true);

        adapter.setDownloadingList(getTasksList());
        liveDownloadListView.setAdapter(adapter);


    }

    public static double roundOf(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_active_task, container, false);
        liveDownloadListView = (ListView) fragmentView.findViewById(R.id.liveDownloadListView);
        emptyMessage = (TextView) fragmentView.findViewById(R.id.emptyDownloadingListMessage);
        liveDownloadListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                mScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        adapter = DownloadingAdapter.getInstance(getActivity());
        adapter.setOnDownloadCancelListener(downloadCancelListener);
        adapter.setDownloadingList(getTasksList());
        liveDownloadListView.setAdapter(adapter);

        return fragmentView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Context context1 = context;
        SharedPrefrenceUtils.getInstance(context).setActiveFragmentAttachedState(true);
        if (!mReceiverRegistered)
            registerForBroadcastListen(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        SharedPrefrenceUtils.getInstance(getActivity()).setActiveFragmentAttachedState(false);
        if (mReceiverRegistered)
            unRegisterBroadcast();

    }

    private ArrayList<DownloadingItemModel> getTasksList() {
        ArrayList<DownloadingItemModel> list = new ArrayList<>();
        SharedPrefrenceUtils utils = SharedPrefrenceUtils.getInstance(getActivity());
        //get tasks list from taskhandler
        //get title from sf
        ArrayList<String> taskIDs = TaskHandler.getInstance(getActivity()).getTaskSequence();
        for (String t_id : taskIDs) {
            String title = utils.getTaskTitle(t_id);
            String thumbnailUrl = utils.getTaskThumbnail(t_id);
            String artist = utils.getTaskArtist(t_id);
            String status = utils.getTaskStatus(t_id);
            list.add(new DownloadingItemModel(t_id,thumbnailUrl,title,artist,"0",status,"0"));
        }

        if(list.size()==0) {
            emptyMessage.setVisibility(View.VISIBLE);
        }else{
            emptyMessage.setVisibility(View.GONE);
        }

        return list;
    }

    private int getPosition(String taskID) {
        int pos = -1;
        ArrayList<DownloadingItemModel> list = getTasksList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).taskId.equals(taskID)) {
                pos = i;
                return pos;
            }
        }
        return pos;
    }

    private void updateItem(int position, int progress, String contentSize) {

        if (position != -1) {
            ArrayList<DownloadingItemModel> old_list = getTasksList();

            for (int i = 0; i < old_list.size(); i++) {
                if (i == position) {
                    DownloadingItemModel data = old_list.get(i);
                    old_list.set(i, new DownloadingItemModel(data.taskId,data.thumbnailUrl,data.title,
                            data.artist,""+progress,data.downloadingState,inMB(contentSize)+" MB"));
                }
            }

            adapter.setDownloadingList(old_list);
            //liveDownloadListView.setAdapter(adapter);

            int start = liveDownloadListView.getFirstVisiblePosition();
            int end = liveDownloadListView.getLastVisiblePosition();

            if(mScrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
            if (start <= position && end >= position) {
                View view = liveDownloadListView.getChildAt(position);
                liveDownloadListView.getAdapter().getView(position, view, liveDownloadListView);
            }
        } else {
            // refressing the tasks list
            adapter.setDownloadingList(getTasksList());
            liveDownloadListView.setAdapter(adapter);
        }
    }



    private double inMB(String bytes) {
        if (bytes != null) {
            double inBytes = Double.parseDouble(bytes);
            double inMB = ((inBytes / 1024) / 1024);
            inMB = roundOf(inMB, 2);

            return inMB;

        } else return 0;
    }

    private void registerForBroadcastListen(Context activity) {
        receiver = new ProgressUpdateBroadcastReceiver();
        activity.registerReceiver(receiver, new IntentFilter(Constants.ACTION_DOWNLOAD_UPDATE));
        mReceiverRegistered = true;

    }

    private void unRegisterBroadcast() {
        getActivity().unregisterReceiver(receiver);
        mReceiverRegistered = false;
    }

    public void makeToast(String msg) {

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

    }

    public void log(String msg) {
        Log.d("DownloadsFragment", msg);
    }

    public class ProgressUpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constants.ACTION_DOWNLOAD_UPDATE)) {

                if( intent.getStringExtra(Constants.EXTRAA_BROADCAST_TYPE).equals(Constants.BROADCAST_TYPE_PROGRESS)){
                    // update the progress
                    final String taskID = intent.getStringExtra(Constants.EXTRA_TASK_ID);
                    final String progress = intent.getStringExtra(Constants.EXTRA_PROGRESS);
                    final String contentSize = intent.getStringExtra(Constants.EXTRA_CONTENT_SIZE);

                    //update in batches of 700ms
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateItemProgress(getPosition(taskID), Integer.valueOf(progress), contentSize);
                        }
                    }, 700);

                }else{
                    // update the view state
                    final String taskID = intent.getStringExtra(Constants.EXTRA_TASK_ID);
                    final String state = intent.getStringExtra(Constants.EXTRAA_DOWNLOAD_VIEW_STATE);
                    updateItemState(getPosition(taskID),state);

                }

            }
        }
    }

    private void updateItemState(int position, String state) {

        if (position != -1) {
            ArrayList<DownloadingItemModel> old_list = getTasksList();

            for (int i = 0; i < old_list.size(); i++) {

                if (i == position) {
                    Log.d("TaskState","updated task-title: "+old_list.get(i).title+" state "+old_list.get(i).downloadingState);
                    DownloadingItemModel data = old_list.get(i);
                    old_list.set(i, new DownloadingItemModel(
                            data.taskId,
                            data.thumbnailUrl,
                            data.title,
                            data.artist,
                            "0", // progresss
                            state, // new State
                            "--" // content size
                    ));
                }else {
                    Log.d("TaskState"," task-title: "+old_list.get(i).title+" state "+old_list.get(i).downloadingState);
                }
            }


            adapter.setDownloadingList(old_list);
            int start = liveDownloadListView.getFirstVisiblePosition();
            int end = liveDownloadListView.getLastVisiblePosition();

            if(mScrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
            if (start <= position && end >= position) {
                View view = liveDownloadListView.getChildAt(position);
                liveDownloadListView.getAdapter().getView(position, view, liveDownloadListView);

            }
        } else {
            // refressing the tasks list
            adapter.setDownloadingList(getTasksList());
            liveDownloadListView.setAdapter(adapter);

        }
    }

    private void updateItemProgress(int position, int progress, String contentSize) {

        if (position != -1) {
            ArrayList<DownloadingItemModel> old_list = getTasksList();

            for (int i = 0; i < old_list.size(); i++) {
                if (i == position) {
                    DownloadingItemModel data = old_list.get(i);
                    old_list.set(i, new DownloadingItemModel(data.taskId,data.thumbnailUrl,data.title,
                            data.artist,""+progress,data.downloadingState,inMB(contentSize)+" MB"));
                }
            }

            adapter.setDownloadingList(old_list);
            //liveDownloadListView.setAdapter(adapter);

            int start = liveDownloadListView.getFirstVisiblePosition();
            int end = liveDownloadListView.getLastVisiblePosition();


            if(mScrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
            if (start <= position && end >= position) {
                View view = liveDownloadListView.getChildAt(position);
                liveDownloadListView.getAdapter().getView(position, view, liveDownloadListView);
            }

        } else {
            // refressing the tasks list
            adapter.setDownloadingList(getTasksList());
            liveDownloadListView.setAdapter(adapter);
        }
    }

    public interface NewDownloadItemArrivalListener {
        void onNewItemAdded();
    }

}

